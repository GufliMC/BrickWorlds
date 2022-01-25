package com.gufli.brickworlds;

import com.google.gson.Gson;
import com.gufli.brickutils.translation.SimpleTranslationManager;
import com.gufli.brickworlds.commands.BrickWorldsCommand;
import com.gufli.brickworlds.listeners.PlayerJoinListener;
import com.gufli.brickworlds.listeners.PlayerSpawnListener;
import com.gufli.brickworlds.world.WorldInstanceContainer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extensions.Extension;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BrickWorlds extends Extension {

    private static final Gson gson = new Gson();

    private final Set<EventListener<?>> eventListeners = new HashSet<>();
    private final Set<Command> commands = new HashSet<>();

    private BrickWorldManager worldManager;

    private Task autoSaveTask;

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        // load config
        BrickWorldsConfig config;
        try {
            config = loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // TRANSLATIONS
        SimpleTranslationManager tm = new SimpleTranslationManager(this, Locale.ENGLISH);
        tm.loadTranslations(this, "languages");

        // load worlds directory
        File worldsDirectory = new File("worlds");
        if (!worldsDirectory.exists()) {
            worldsDirectory.mkdirs();
        }

        // create world manager
        worldManager = new BrickWorldManager(worldsDirectory);
        WorldAPI.setWorldManager(worldManager);

        // load worlds
        if ( config.loadOnStartup() != null ) {
            loadWorlds(config.loadOnStartup());
        }

        // default world
        Optional<WorldInstanceContainer> defaultWorld = Optional.empty();
        if ( config.defaultWorld() != null ) {
            defaultWorld = worldManager.worldByName(config.defaultWorld())
                    .map(world -> (WorldInstanceContainer) world);
        }

        if ( defaultWorld.isPresent() ) {
            getLogger().info("Setting world '" + defaultWorld.get().worldInfo().name() + "' as default.");
            eventListeners.add(new PlayerJoinListener(defaultWorld.get()));
            eventListeners.add(new PlayerSpawnListener(defaultWorld.get()));
        }

        commands.add(new BrickWorldsCommand());

        // register listeners
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        eventListeners.forEach(eventHandler::addListener);

        // register commands
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commands.forEach(commandManager::register);

        // autosaving
        int minutes = config.autoSaveInterval();
        if ( minutes > 0 ) {
            Duration interval = Duration.of(minutes, ChronoUnit.MINUTES);
            autoSaveTask = MinecraftServer.getSchedulerManager().buildTask(worldManager::saveAll)
                    .delay(interval).repeat(interval)
                    .executionType(ExecutionType.ASYNC).schedule();
        }

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        // unregister listeners
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        eventListeners.forEach(eventHandler::removeListener);

        // unregister commands
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commands.forEach(commandManager::unregister);

        if (worldManager != null) {
            worldManager.shutdown();
        }

        if ( autoSaveTask != null ) {
            autoSaveTask.cancel();
        }

        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getOrigin().getName() + " v" + getOrigin().getVersion();
    }

    private BrickWorldsConfig loadConfig() throws IOException {
        try (
                InputStream is = getResource("config.json");
                InputStreamReader isr = new InputStreamReader(is)
        ) {
            return gson.fromJson(isr, BrickWorldsConfig.class);
        }
    }

    private void loadWorlds(Collection<String> worlds) {
        for (String world : worlds) {
            try {
                worldManager.loadWorld(world);
            } catch (IllegalArgumentException ex) {
                getLogger().warn("Cannot find world '" + world + "'.");
            }
        }
    }

}

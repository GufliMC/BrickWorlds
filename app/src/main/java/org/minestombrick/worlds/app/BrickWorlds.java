package org.minestombrick.worlds.app;

import com.google.gson.Gson;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extensions.Extension;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Task;
import org.minestombrick.commandtools.api.command.CommandGroupBuilder;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.i18n.api.namespace.I18nNamespace;
import org.minestombrick.worlds.api.WorldAPI;
import org.minestombrick.worlds.api.world.WorldInstance;
import org.minestombrick.worlds.app.commands.*;
import org.minestombrick.worlds.app.listeners.PlayerJoinListener;
import org.minestombrick.worlds.app.listeners.PlayerSpawnListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public class BrickWorlds extends Extension {

    private static final Gson gson = new Gson();

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
        I18nNamespace namespace = new I18nNamespace(this, Locale.ENGLISH);
        namespace.loadValues(this, "languages");
        I18nAPI.get().register(namespace);

        // load worlds directory
        File worldsDirectory = new File("worlds");
        if (!worldsDirectory.exists()) {
            worldsDirectory.mkdirs();
        }

        // create world manager
        worldManager = new BrickWorldManager(worldsDirectory);
        WorldAPI.setWorldManager(worldManager);

        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();

        // load worlds
        if (config.loadOnStartup() != null) {
            loadWorlds(config.loadOnStartup());
        }

        // default world
        Optional<WorldInstance> defaultWorld = Optional.empty();
        if (config.defaultWorld() != null) {
            defaultWorld = worldManager.worldByName(config.defaultWorld());
        }

        if (defaultWorld.isPresent()) {
            getLogger().info("Setting world '" + defaultWorld.get().worldInfo().name() + "' as default.");
            eventHandler.addListener(new PlayerJoinListener(defaultWorld.get()));
            eventHandler.addListener(new PlayerSpawnListener(defaultWorld.get()));
        }

        // register commands
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(CommandGroupBuilder.of(new Command("brickworlds", "bw"))
                        .withCommand(new ListCommand())
                        .withCommand(new LoadCommand())
                        .withCommand(new SaveAllCommand())
                        .withCommand(new SaveCommand())
                        .withCommand(new SetSpawnCommand())
                        .withCommand(new TeleportCommand())
                .build()
        );

        // autosaving
        int minutes = config.autoSaveInterval();
        if (minutes > 0) {
            Duration interval = Duration.of(minutes, ChronoUnit.MINUTES);
            autoSaveTask = MinecraftServer.getSchedulerManager().buildTask(worldManager::saveAll)
                    .delay(interval).repeat(interval)
                    .executionType(ExecutionType.ASYNC).schedule();
        }

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        if (worldManager != null) {
            worldManager.shutdown();
        }

        if (autoSaveTask != null) {
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

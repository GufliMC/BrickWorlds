package com.gufli.brickworlds;

import com.google.gson.Gson;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BrickWorlds extends Extension {

    private static final Gson gson = new Gson();

    private final Set<EventListener<?>> eventListeners = new HashSet<>();
    private final Set<Command> commands = new HashSet<>();

    private BrickWorldManager worldManager;

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

        // load worlds directory
        File worldsDirectory = new File("worlds");
        if (!worldsDirectory.exists()) {
            worldsDirectory.mkdirs();
        }

        // create world manager
        worldManager = new BrickWorldManager(worldsDirectory);

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
            eventListeners.add(new PlayerJoinListener(defaultWorld.get()));
            eventListeners.add(new PlayerSpawnListener(defaultWorld.get()));
        }

        commands.add(new BrickWorldsCommand(worldManager));

        // register listeners
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        eventListeners.forEach(eventHandler::addListener);

        // register commands
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commands.forEach(commandManager::register);

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        if (worldManager != null) {
            worldManager.shutdown();
        }

        // unregister listeners
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        eventListeners.forEach(eventHandler::removeListener);

        // unregister commands
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commands.forEach(commandManager::unregister);

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

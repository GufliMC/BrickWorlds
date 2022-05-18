package com.guflimc.brick.worlds.minestom;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.SimpleCommandMeta;
import com.google.gson.Gson;
import com.guflimc.brick.i18n.minestom.api.MinestomI18nAPI;
import com.guflimc.brick.i18n.minestom.api.namespace.MinestomNamespace;
import com.guflimc.brick.worlds.minestom.api.MinestomWorldAPI;
import com.guflimc.brick.worlds.minestom.api.world.MinestomGeneratorRegsitry;
import com.guflimc.brick.worlds.minestom.api.world.World;
import com.guflimc.brick.worlds.minestom.commands.MinestomBrickWorldsCommands;
import com.guflimc.brick.worlds.minestom.generators.FlatGenerator;
import com.guflimc.brick.worlds.minestom.generators.VoidGenerator;
import com.guflimc.brick.worlds.minestom.listeners.PlayerJoinListener;
import com.guflimc.brick.worlds.minestom.listeners.PlayerSpawnListener;
import com.wollymc.brick.worlds.common.commands.BrickWorldsCommands;
import io.github.openminigameserver.cloudminestom.MinestomCommandManager;
import io.github.openminigameserver.cloudminestom.MinestomCommandRegistrationHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public class MinestomBrickWorlds extends Extension {

    private static final Gson gson = new Gson();

    private MinestomBrickWorldManager worldManager;
    private Task autoSaveTask;

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        // load config
        MinestomBrickWorldsConfig config;
        try {
            config = loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // TRANSLATIONS
        MinestomNamespace namespace = new MinestomNamespace(this, Locale.ENGLISH);
        namespace.loadValues(this, "languages");
        MinestomI18nAPI.get().register(namespace);

        // load worlds directory
        File worldsDirectory = new File("worlds");
        if (!worldsDirectory.exists()) {
            worldsDirectory.mkdirs();
        }

        // create world manager
        worldManager = new MinestomBrickWorldManager(worldsDirectory);
        MinestomWorldAPI.setWorldManager(worldManager);

        // register generators
        MinestomGeneratorRegsitry.register("flat", args -> new FlatGenerator(Block.STONE, 5));
        MinestomGeneratorRegsitry.register("void", args -> new VoidGenerator());

        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();

        // load worlds
        if (config.loadOnStartup() != null) {
            loadWorlds(config.loadOnStartup());
        }

        // default world
        Optional<World> defaultWorld = Optional.empty();
        if (config.defaultWorld() != null) {
            defaultWorld = worldManager.worldByName(config.defaultWorld());
        }

        if (defaultWorld.isPresent()) {
            getLogger().info("Setting world '" + defaultWorld.get().info().name() + "' as default.");
            eventHandler.addListener(new PlayerJoinListener(defaultWorld.get().asInstance()));
            eventHandler.addListener(new PlayerSpawnListener(defaultWorld.get()));
        }

        // register commands
        MinestomCommandManager<CommandSender> commandManager = new MinestomCommandManager<>(
                CommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(),
                Function.identity()
        );

        AnnotationParser<CommandSender> annotationParser = new AnnotationParser<>(
                commandManager,
                CommandSender.class,
                parameters -> SimpleCommandMeta.empty()
        );

        annotationParser.parse(new BrickWorldsCommands(worldManager));
        annotationParser.parse(new MinestomBrickWorldsCommands(worldManager));

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

    private MinestomBrickWorldsConfig loadConfig() throws IOException {
        try (
                InputStream is = getResource("config.json");
                InputStreamReader isr = new InputStreamReader(is)
        ) {
            return gson.fromJson(isr, MinestomBrickWorldsConfig.class);
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

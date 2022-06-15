package com.guflimc.brick.worlds.minestom;

import com.guflimc.brick.worlds.api.world.WorldInfo;
import com.guflimc.brick.worlds.minestom.api.MinestomWorldManager;
import com.guflimc.brick.worlds.minestom.api.event.WorldLoadEvent;
import com.guflimc.brick.worlds.minestom.api.world.MinestomGeneratorRegsitry;
import com.guflimc.brick.worlds.minestom.api.world.MinestomWorld;
import com.guflimc.brick.worlds.minestom.world.BrickWorldInstance;
import com.wollymc.brick.worlds.common.AbstractWorldManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MinestomBrickWorldManager extends AbstractWorldManager<MinestomWorld> implements MinestomWorldManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(MinestomBrickWorldManager.class);

    private final InstanceManager instanceManager = MinecraftServer.getInstanceManager();

    public MinestomBrickWorldManager(File worldsDirectory) {
        super(worldsDirectory);
    }

    void shutdown() {
        try {
            saveAll().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<MinestomWorld> worldByName(@NotNull String name) {
        return loadedWorlds().stream().filter(world -> world.info().name().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Collection<MinestomWorld> loadedWorlds() {
        return instanceManager.getInstances().stream()
                .filter(inst -> inst instanceof MinestomWorld)
                .map(inst -> (MinestomWorld) inst)
                .collect(Collectors.toList());
    }

    @Override
    public CompletableFuture<Void> saveAll() {
        return CompletableFuture.allOf(loadedWorlds().stream().map(MinestomWorld::save)
                .toArray(CompletableFuture[]::new));
    }

    @Override
    public CompletableFuture<Void> saveWorld(@NotNull String name) {
        return worldByName(name).map(MinestomWorld::save)
                .orElse(CompletableFuture.completedFuture(null));
    }

    @Override
    public CompletableFuture<Void> deleteWorld(@NotNull String name) {
        worldByName(name).ifPresent(world -> {
            instanceManager.unregisterInstance(world.asInstance());
            world.directory().delete();
        });
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<MinestomWorld> createWorld(@NotNull String name) {
        return createWorld(name, "void");
    }

    @Override
    public CompletableFuture<MinestomWorld> createWorld(@NotNull String name, String generatorId, String[] generatorArgs) {
        if (worldInfoByName(name).isPresent()) {
            throw new IllegalArgumentException("A world with that name already exists.");
        }

        LOGGER.info("Creating world '" + name + "' in " + worldsDirectory.getName() + " directory.");

        File directory = new File(worldsDirectory, name);
        int index = 0;
        while (directory.exists()) {
            directory = new File(worldsDirectory, name + "-" + (++index));
        }

        WorldInfo info = WorldInfo.of(infoFile(directory));
        info.setName(name);

        BrickWorldInstance world = new BrickWorldInstance(directory, info);
        MinestomGeneratorRegsitry.createGenerator(generatorId, generatorArgs)
                .ifPresent(generator -> world.asInstance().setGenerator(generator));

        world.info().setGenerator(generatorId);
        world.save();

        instanceManager.registerInstance(world);

        return CompletableFuture.completedFuture(world);
    }

    private CompletableFuture<MinestomWorld> loadWorld(@NotNull File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException("A world with that name does not exist.");
        }

        WorldInfo info = WorldInfo.of(infoFile(directory));
        LOGGER.info("Loading world '" + info.name() + "' from " + worldsDirectory.getName() + " directory.");

        BrickWorldInstance instance = new BrickWorldInstance(directory, info);
        instanceManager.registerInstance(instance);

        MinecraftServer.getGlobalEventHandler().call(new WorldLoadEvent(instance));

        return CompletableFuture.completedFuture(instance);
    }

    @Override
    public CompletableFuture<MinestomWorld> loadWorld(@NotNull String name) {
        File[] files = worldsDirectory.listFiles();
        if (files == null) {
            throw new IllegalStateException("Worlds directory does not exist.");
        }

        // find by world info
        for (File directory : files) {
            WorldInfo info = WorldInfo.of(infoFile(directory));
            if (info.name().equalsIgnoreCase(name)) {
                return loadWorld(directory);
            }
        }

        // find by directory name
        for (File directory : files) {
            if (directory.getName().equalsIgnoreCase(name)) {
                return loadWorld(directory);
            }
        }

        throw new IllegalArgumentException("A world with that name does not exist.");
    }

    @Override
    public CompletableFuture<Void> unloadWorld(@NotNull String name) {
        worldByName(name).ifPresent(world ->
                instanceManager.unregisterInstance(world.asInstance()));
        return CompletableFuture.completedFuture(null);
    }

}

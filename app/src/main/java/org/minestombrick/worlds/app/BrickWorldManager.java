package org.minestombrick.worlds.app;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.InstanceManager;
import org.jetbrains.annotations.NotNull;
import org.minestombrick.worlds.api.WorldManager;
import org.minestombrick.worlds.api.world.WorldInfo;
import org.minestombrick.worlds.api.world.WorldInstance;
import org.minestombrick.worlds.api.generator.VoidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BrickWorldManager implements WorldManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(BrickWorldManager.class);

    private final File worldsDirectory;

    private final InstanceManager instanceManager = MinecraftServer.getInstanceManager();

    public BrickWorldManager(File worldsDirectory) {
        this.worldsDirectory = worldsDirectory;
    }

    void shutdown() {
        try {
            saveAll().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<WorldInstance> worlds() {
        return instanceManager.getInstances().stream()
                .filter(inst -> inst instanceof WorldInstance)
                .map(inst -> (WorldInstance) inst)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<WorldInstance> worldByName(String name) {
        return instanceManager.getInstances().stream()
                .filter(inst -> inst instanceof WorldInstance)
                .map(inst -> (WorldInstance) inst)
                .filter(inst -> inst.worldInfo().name().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public CompletableFuture<Void> saveAll() {
        return CompletableFuture.allOf(
                instanceManager.getInstances().stream()
                        .filter(inst -> inst instanceof WorldInstance)
                        .map(inst -> (WorldInstance) inst)
                        .map(WorldInstance::save).toArray(CompletableFuture[]::new));
    }

    @Override
    public void deleteWorld(@NotNull WorldInstance world) {
        instanceManager.unregisterInstance(world);
        world.directory().delete();
    }

    @Override
    public WorldInstance createWorld(@NotNull String name) {
        return createWorld(name, new VoidGenerator());
    }

    @Override
    public WorldInstance createWorld(@NotNull String name, @NotNull ChunkGenerator generator) {
        if (worldByName(name).isPresent()) {
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

        WorldInstance instance = new WorldInstance(directory, info);
        instance.setChunkGenerator(generator);

        instance.worldInfo().setGenerator(generator.getClass().getSimpleName());
        instance.save();

        instanceManager.registerInstance(instance);
        return instance;
    }

    @Override
    public WorldInstance loadWorld(@NotNull File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException("A world with that name does not exist.");
        }

        WorldInfo info = WorldInfo.of(infoFile(directory));
        LOGGER.info("Loading world '" + info.name() + "' from " + worldsDirectory.getName() + " directory.");

        WorldInstance instance = new WorldInstance(directory, info);
        instanceManager.registerInstance(instance);
        return instance;
    }

    @Override
    public WorldInstance loadWorld(@NotNull String name) {
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

    private File infoFile(File worldDirectory) {
        return new File(worldDirectory, "worldinfo.json");
    }

}

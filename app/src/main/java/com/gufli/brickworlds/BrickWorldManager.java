package com.gufli.brickworlds;

import com.gufli.brickworlds.generators.VoidGenerator;
import com.gufli.brickworlds.world.WorldInstanceContainer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class BrickWorldManager implements WorldManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(BrickWorldManager.class);

    private final File worldsDirectory;

    private final InstanceManager instanceManager = MinecraftServer.getInstanceManager();

    public BrickWorldManager(File worldsDirectory) {
        this.worldsDirectory = worldsDirectory;
    }

    void shutdown() {
        saveAll();
    }

    @Override
    public Collection<World> worlds() {
        return instanceManager.getInstances().stream()
                .filter(inst -> inst instanceof World)
                .map(inst -> (World) inst)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<World> worldByName(String name) {
        return instanceManager.getInstances().stream()
                .filter(inst -> inst instanceof World)
                .map(inst -> (World) inst)
                .filter(inst -> inst.worldInfo().name().equalsIgnoreCase(name))
                .findFirst();
    }

    public void saveAll() {
        instanceManager.getInstances().stream()
                .filter(inst -> inst instanceof WorldInstanceContainer)
                .map(inst -> (WorldInstanceContainer) inst)
                .forEach(WorldInstanceContainer::save);
    }

    @Override
    public void deleteWorld(@NotNull World world) {
        WorldInstanceContainer instance = (WorldInstanceContainer) world;
        instanceManager.unregisterInstance(instance);
        instance.directory().delete();
    }

    @Override
    public World createWorld(@NotNull String name) {
        return createWorld(name, new VoidGenerator());
    }

    @Override
    public World createWorld(@NotNull String name, @NotNull WorldGenerator generator) {
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

        WorldInstanceContainer instance = new WorldInstanceContainer(directory, info);
        instance.setChunkGenerator(generator);

        instance.worldInfo().setGenerator(generator.name());
        instance.save();

        instanceManager.registerInstance(instance);
        return instance;
    }

    @Override
    public World loadWorld(@NotNull File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException("A world with that name does not exist.");
        }

        WorldInfo info = WorldInfo.of(infoFile(directory));
        LOGGER.info("Loading world '" + info.name() + "' from " + worldsDirectory.getName() + " directory.");

        WorldInstanceContainer instance = new WorldInstanceContainer(directory, info);
        instanceManager.registerInstance(instance);
        return instance;
    }

    public World loadWorld(@NotNull String name) {
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

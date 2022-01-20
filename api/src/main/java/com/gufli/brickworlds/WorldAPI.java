package com.gufli.brickworlds;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

public class WorldAPI {

    private WorldAPI() {}

    private static WorldManager worldManager;

    public static void setWorldManager(WorldManager manager) {
        worldManager = manager;
    }

    public static Collection<World> worlds() {
        return worldManager.worlds();
    }

    public static Optional<World> worldByName(String name) {
        return worldManager.worldByName(name);
    }

    public static void saveAll() {
        worldManager.saveAll();
    }

    public static void deleteWorld(@NotNull World world) {
        worldManager.deleteWorld(world);
    }

    public static World createWorld(@NotNull String name) {
        return worldManager.createWorld(name);
    }

    public static World createWorld(@NotNull String name, @NotNull WorldGenerator generator) {
        return worldManager.createWorld(name, generator);
    }

    public static World loadWorld(@NotNull File directory) {
        return worldManager.loadWorld(directory);
    }

}

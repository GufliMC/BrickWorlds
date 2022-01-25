package com.gufli.brickworlds;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

    public static CompletableFuture<Void> saveAll() {
        return worldManager.saveAll();
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

    public static World loadWorld(@NotNull String name) {
        return worldManager.loadWorld(name);
    }

}

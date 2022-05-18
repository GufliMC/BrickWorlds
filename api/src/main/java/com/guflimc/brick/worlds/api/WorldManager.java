package com.guflimc.brick.worlds.api;

import com.guflimc.brick.worlds.api.world.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface WorldManager {

    Collection<WorldInfo> availableWorlds();

    Collection<WorldInfo> loadedWorlds();

    Optional<WorldInfo> worldInfoByName(@NotNull String name);

    Optional<WorldInfo> worldInfoByDirectory(@NotNull File directory);

    CompletableFuture<Void> createWorld(@NotNull String name);

    CompletableFuture<Void> createWorld(@NotNull String name, String generator, String[] generatorArgs);

    default CompletableFuture<Void> createWorld(@NotNull String name, String generator) {
        return createWorld(name, generator, new String[0]);
    }

    CompletableFuture<Void> deleteWorld(@NotNull String name);

    CompletableFuture<Void> saveAll();

    CompletableFuture<Void> saveWorld(@NotNull String name);

    CompletableFuture<Void> loadWorld(@NotNull String name);

    CompletableFuture<Void> unloadWorld(@NotNull String name);

}

package com.guflimc.brick.worlds.api;

import com.guflimc.brick.worlds.api.world.World;
import com.guflimc.brick.worlds.api.world.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface WorldManager<T extends World> {

    Collection<WorldInfo> availableWorlds();

    Collection<T> loadedWorlds();

    Optional<T> worldByName(@NotNull String name);

    Optional<WorldInfo> worldInfoByName(@NotNull String name);

    Optional<WorldInfo> worldInfoByDirectory(@NotNull File directory);

    CompletableFuture<T> createWorld(@NotNull String name);

    CompletableFuture<T> createWorld(@NotNull String name, String generator, String[] generatorArgs);

    default CompletableFuture<T> createWorld(@NotNull String name, String generator) {
        return createWorld(name, generator, new String[0]);
    }

    CompletableFuture<Void> deleteWorld(@NotNull String name);

    CompletableFuture<Void> saveAll();

    CompletableFuture<Void> saveWorld(@NotNull String name);

    CompletableFuture<T> loadWorld(@NotNull String name);

    CompletableFuture<Void> unloadWorld(@NotNull String name);

}

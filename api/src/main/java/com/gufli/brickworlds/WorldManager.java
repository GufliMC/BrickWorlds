package com.gufli.brickworlds;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface WorldManager {

    Collection<World> worlds();

    Optional<World> worldByName(String name);

    CompletableFuture<Void> saveAll();

    void deleteWorld(@NotNull World world);

    World createWorld(@NotNull String name);

    World createWorld(@NotNull String name, @NotNull WorldGenerator generator);

    World loadWorld(@NotNull File directory);

    World loadWorld(@NotNull String name);

}

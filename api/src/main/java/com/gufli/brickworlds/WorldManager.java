package com.gufli.brickworlds;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

public interface WorldManager {

    Optional<World> worldByName(String name);

    void saveAll();

    void deleteWorld(@NotNull World world);

    World createWorld(@NotNull String name);

    World createWorld(@NotNull String name, @NotNull WorldGenerator generator);

    World loadWorld(@NotNull File directory);

}

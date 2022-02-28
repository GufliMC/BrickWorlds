package org.minestombrick.worlds.api;

import net.minestom.server.instance.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.minestombrick.worlds.api.world.WorldInstance;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface WorldManager {

    Collection<WorldInstance> worlds();

    Optional<WorldInstance> worldByName(String name);

    CompletableFuture<Void> saveAll();

    void deleteWorld(@NotNull WorldInstance world);

    WorldInstance createWorld(@NotNull String name);

    WorldInstance createWorld(@NotNull String name, @NotNull ChunkGenerator generator);

    WorldInstance loadWorld(@NotNull File directory);

    WorldInstance loadWorld(@NotNull String name);

}

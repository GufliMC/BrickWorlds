package com.wollymc.brick.worlds.common;

import com.guflimc.brick.worlds.api.WorldManager;
import com.guflimc.brick.worlds.api.world.World;
import com.guflimc.brick.worlds.api.world.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractWorldManager<T extends World> implements WorldManager<T> {

    protected final File worldsDirectory;

    protected AbstractWorldManager(File worldsDirectory) {
        this.worldsDirectory = worldsDirectory;
    }

    @Override
    public Collection<WorldInfo> availableWorlds() {
        File[] files = worldsDirectory.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(files).map(this::worldInfo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WorldInfo> worldInfoByName(@NotNull String name) {
        return find(name);
    }

    @Override
    public Optional<WorldInfo> worldInfoByDirectory(@NotNull File directory) {
        if ( !directory.exists() ) {
            return Optional.empty();
        }

        return Optional.of(worldInfo(directory));
    }

    protected final Optional<WorldInfo> find(String name) {
        File[] files = worldsDirectory.listFiles();
        if (files == null) {
            return Optional.empty();
        }

        for (File directory : files) {
            WorldInfo info = worldInfo(directory);
            if ( info.name().equalsIgnoreCase(name) ) {
                return Optional.of(info);
            }
        }

        return Optional.empty();
    }

    protected final WorldInfo worldInfo(File directory) {
        return WorldInfo.of(infoFile(directory));
    }

    protected final File infoFile(File directory) {
        return new File(directory, "worldinfo.json");
    }
}

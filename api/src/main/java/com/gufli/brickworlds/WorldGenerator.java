package com.gufli.brickworlds;

import net.minestom.server.instance.ChunkGenerator;

public abstract class WorldGenerator implements ChunkGenerator {

    private final String name;

    public WorldGenerator(String name) {
        this.name = name;
    }

    public final String name() {
        return name;
    }
}

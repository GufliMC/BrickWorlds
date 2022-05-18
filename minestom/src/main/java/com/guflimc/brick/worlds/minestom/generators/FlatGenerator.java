package com.guflimc.brick.worlds.minestom.generators;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public class FlatGenerator implements Generator {

    private final Block type;
    private final int height;

    public FlatGenerator(Block type, int height) {
        this.type = type;
        this.height = height;
    }

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.modifier().fillHeight(0, 1, Block.BEDROCK);
        unit.modifier().fillHeight(1, height, type);
    }

}
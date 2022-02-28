package org.minestombrick.worlds.api.generator;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlatGenerator implements ChunkGenerator {

    private final Block type;
    private final int height;

    public FlatGenerator(Block type, int height) {
        this.type = type;
        this.height = height;
    }


    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        for ( int x = 0; x < Chunk.CHUNK_SIZE_X; x++ ) {
            for ( int z = 0; z < Chunk.CHUNK_SIZE_Z; z++ ) {
                batch.setBlock(x, 0, z, Block.BEDROCK);

                for ( int y = 1; y <= height; y++ ) {
                    batch.setBlock(x, y, z, type);
                }
            }
        }
    }

    @Override
    public List<ChunkPopulator> getPopulators() {
        return null;
    }
}
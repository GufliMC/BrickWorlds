package org.minestombrick.worlds.api.generator;

import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VoidGenerator implements ChunkGenerator {

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        if ( chunkX != 0 || chunkZ != 0 ) {
            return;
        }

        batch.setBlock(0, 60, 0, Block.BEDROCK);
    }

    @Override
    public List<ChunkPopulator> getPopulators() {
        return null;
    }
}
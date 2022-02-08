package com.gufli.brickworlds.world;

import com.gufli.brickworlds.World;
import com.gufli.brickworlds.WorldInfo;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WorldInstanceContainer extends InstanceContainer implements World {

    private final static Logger LOGGER = LoggerFactory.getLogger(WorldInstanceContainer.class);

    private final File directory;

    private final WorldInfo worldInfo;

    public WorldInstanceContainer(@NotNull File directory, WorldInfo worldInfo) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD);
        setChunkLoader(new AnvilLoader(directory.toPath()));
        enableAutoChunkLoad(true);

        this.directory = directory;
        this.worldInfo = worldInfo;

        // TODO apply generator from worldinfo file

        if ( !directory.exists() ) {
            directory.mkdirs();
            save();
        }
    }

    @Override
    public WorldInfo worldInfo() {
        return worldInfo;
    }

    public File directory() {
        return directory;
    }

    @Override
    public CompletableFuture<Void> save() {
        LOGGER.info(String.format("Saving %s...", worldInfo.name()));
        long millis = System.currentTimeMillis();
        return CompletableFuture.allOf(
                saveInstance(),
                saveChunksToStorage(),
                CompletableFuture.runAsync(worldInfo::save)
        ).thenRun(() -> {
            float time = (System.currentTimeMillis() - millis) / 1000f;
            LOGGER.info(String.format("Saved %s in %.2fs.", worldInfo.name(), time));
        });
    }

    @Override
    public void teleport(Player player) {
        Pos spawn = worldInfo().spawn();
        if (spawn == null) {
            spawn = new Pos(0, 1, 0);
        }

        loadChunk(spawn).join();

        while (!getBlock(spawn).isAir()
                || !getBlock(spawn.add(0, 1, 0)).isAir()) {
            spawn = spawn.add(0, 2, 0);
        }

        if (player.getInstance() != this) {
            player.setInstance(this);
        }
        player.teleport(spawn);
    }

}

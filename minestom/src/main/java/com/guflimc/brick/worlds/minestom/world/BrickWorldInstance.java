package com.guflimc.brick.worlds.minestom.world;

import com.guflimc.brick.worlds.api.math.Position;
import com.guflimc.brick.worlds.api.world.WorldInfo;
import com.guflimc.brick.worlds.minestom.api.world.MinestomWorld;
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

public class BrickWorldInstance extends InstanceContainer implements MinestomWorld {

    private final static Logger LOGGER = LoggerFactory.getLogger(BrickWorldInstance.class);

    private final File directory;

    private final WorldInfo worldInfo;

    public BrickWorldInstance(@NotNull File directory, WorldInfo worldInfo) {
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

    public WorldInfo info() {
        return worldInfo;
    }

    public File directory() {
        return directory;
    }

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

    public void teleport(Player player) {
        Pos spawn;

        Position spawnPosition = info().spawn();
        if ( spawnPosition != null ) {
            spawn = new Pos(spawnPosition.x(), spawnPosition.y(), spawnPosition.z(), spawnPosition.yaw(), spawnPosition.pitch());
        } else {
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

package com.gufli.brickworlds.world;

import com.google.gson.Gson;
import com.gufli.brickworlds.World;
import com.gufli.brickworlds.WorldInfo;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class WorldInstanceContainer extends InstanceContainer implements World {

    private final static Gson gson = new Gson();

    private final File directory;

    private final WorldInfo worldInfo;

    public WorldInstanceContainer(@NotNull File directory, WorldInfo worldInfo) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD);
        setChunkLoader(new AnvilLoader(directory.toPath()));
        enableAutoChunkLoad(true);

        this.directory = directory;
        this.worldInfo = worldInfo;

        // TODO apply generator from worldinfo file
    }

    @Override
    public WorldInfo worldInfo() {
        return worldInfo;
    }

    public File directory() {
        return directory;
    }

    @Override
    public void save() {
        saveInstance();
        saveChunksToStorage();
        worldInfo.save();
    }

}

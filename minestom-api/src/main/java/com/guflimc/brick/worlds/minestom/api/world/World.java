package com.guflimc.brick.worlds.minestom.api.world;

import com.guflimc.brick.worlds.api.world.WorldInfo;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface World {

    WorldInfo info();

    File directory();

    CompletableFuture<Void> save();

    void teleport(Player player);

    default Instance asInstance() {
        return (Instance) this;
    }

}

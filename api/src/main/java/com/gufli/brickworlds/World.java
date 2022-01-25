package com.gufli.brickworlds;

import net.minestom.server.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface World {

    WorldInfo worldInfo();

    CompletableFuture<Void> save();

    void teleport(Player player);

}

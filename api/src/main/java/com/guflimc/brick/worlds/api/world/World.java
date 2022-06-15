package com.guflimc.brick.worlds.api.world;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface World {

    WorldInfo info();

    File directory();

    CompletableFuture<Void> save();

}

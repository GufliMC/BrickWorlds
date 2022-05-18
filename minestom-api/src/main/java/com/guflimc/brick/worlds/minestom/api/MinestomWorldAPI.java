package com.guflimc.brick.worlds.minestom.api;

public class MinestomWorldAPI {

    private MinestomWorldAPI() {}

    private static MinestomWorldManager worldManager;

    public static void setWorldManager(MinestomWorldManager manager) {
        worldManager = manager;
    }

    //

    public static MinestomWorldManager get() {
        return worldManager;
    }

}

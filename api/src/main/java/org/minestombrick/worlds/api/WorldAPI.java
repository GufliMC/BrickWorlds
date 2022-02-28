package org.minestombrick.worlds.api;

public class WorldAPI {

    private WorldAPI() {}

    private static WorldManager worldManager;

    public static void setWorldManager(WorldManager manager) {
        worldManager = manager;
    }

    //

    public static WorldManager get() {
        return worldManager;
    }

}

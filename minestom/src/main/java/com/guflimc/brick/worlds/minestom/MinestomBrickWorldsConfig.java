package com.guflimc.brick.worlds.minestom;

import java.util.List;

public class MinestomBrickWorldsConfig {

    private String defaultWorld;
    private List<String> loadOnStartup;
    private int autoSaveInterval;

    public String defaultWorld() {
        return defaultWorld;
    }

    public List<String> loadOnStartup() {
        return loadOnStartup;
    }

    public int autoSaveInterval() {
        return autoSaveInterval;
    }

}

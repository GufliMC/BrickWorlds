package org.minestombrick.worlds.app;

import java.util.List;

public class BrickWorldsConfig {

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

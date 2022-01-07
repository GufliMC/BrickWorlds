package com.gufli.brickworlds;

import java.util.List;

public class BrickWorldsConfig {

    private String defaultWorld;
    private List<String> loadOnStartup;

    public String defaultWorld() {
        return defaultWorld;
    }

    public List<String> loadOnStartup() {
        return loadOnStartup;
    }

}

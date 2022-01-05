package com.gufli.brickworlds;

import net.minestom.server.extensions.Extension;

public class BrickWorlds extends Extension {

    private String nameAndVersion() {
        return getOrigin().getName() + " v" + getOrigin().getVersion();
    }

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

}

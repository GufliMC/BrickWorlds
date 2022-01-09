package com.gufli.brickworlds;

import net.minestom.server.entity.Player;

public interface World {

    WorldInfo worldInfo();

    void save();

    void teleport(Player player);

}

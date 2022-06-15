package com.guflimc.brick.worlds.minestom.api.world;

import com.guflimc.brick.worlds.api.world.World;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public interface MinestomWorld extends World {

    void teleport(Player player);

    default Instance asInstance() {
        return (Instance) this;
    }

}

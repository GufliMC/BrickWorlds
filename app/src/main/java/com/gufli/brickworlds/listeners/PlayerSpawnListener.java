package com.gufli.brickworlds.listeners;


import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {

    private final Pos spawn;

    public PlayerSpawnListener(Instance defaultInstance) {
        try {
            defaultInstance.loadChunk(0, 0).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Pos spawn = new Pos(0, 1, 0);
        while (!defaultInstance.getBlock(spawn).isAir()
                || !defaultInstance.getBlock(spawn.add(0, 1, 0)).isAir()) {
            spawn = spawn.add(0, 2, 0);
        }

        this.spawn = spawn;
    }

    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSpawnEvent event) {
        if ( event.isFirstSpawn() ) {
            event.getPlayer().teleport(spawn);
        }
        return Result.SUCCESS;
    }
}

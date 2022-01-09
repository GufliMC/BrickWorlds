package com.gufli.brickworlds.listeners;


import com.gufli.brickworlds.world.WorldInstanceContainer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {

    private final WorldInstanceContainer instance;

    public PlayerSpawnListener(WorldInstanceContainer defaultInstance) {
        this.instance = defaultInstance;
    }

    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSpawnEvent event) {
        if ( event.isFirstSpawn() ) {
            instance.teleport(event.getPlayer());
        }
        return Result.SUCCESS;
    }
}

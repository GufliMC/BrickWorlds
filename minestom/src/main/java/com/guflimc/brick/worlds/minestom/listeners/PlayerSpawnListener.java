package com.guflimc.brick.worlds.minestom.listeners;


import com.guflimc.brick.worlds.minestom.api.world.MinestomWorld;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {

    private final MinestomWorld instance;

    public PlayerSpawnListener(MinestomWorld defaultInstance) {
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

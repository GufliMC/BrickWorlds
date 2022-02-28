package org.minestombrick.worlds.app.listeners;


import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.minestombrick.worlds.api.world.WorldInstance;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {

    private final WorldInstance instance;

    public PlayerSpawnListener(WorldInstance defaultInstance) {
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

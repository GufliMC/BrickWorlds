package org.minestombrick.worlds.app.listeners;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements EventListener<PlayerLoginEvent> {

    private final Instance defaultInstance;

    public PlayerJoinListener(Instance defaultInstance) {
        this.defaultInstance = defaultInstance;
    }

    @Override
    public @NotNull Class<PlayerLoginEvent> eventType() {
        return PlayerLoginEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerLoginEvent event) {
        event.setSpawningInstance(defaultInstance);
        return Result.SUCCESS;
    }
}

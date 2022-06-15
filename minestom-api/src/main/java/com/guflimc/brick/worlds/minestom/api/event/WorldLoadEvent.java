package com.guflimc.brick.worlds.minestom.api.event;

import com.guflimc.brick.worlds.minestom.api.world.MinestomWorld;
import net.minestom.server.event.Event;

public record WorldLoadEvent(MinestomWorld world) implements Event {
}

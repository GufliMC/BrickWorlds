package com.guflimc.brick.worlds.minestom.api;

import com.guflimc.brick.worlds.api.WorldManager;
import com.guflimc.brick.worlds.minestom.api.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface MinestomWorldManager extends WorldManager {

    Collection<World> worlds();

    Optional<World> worldByName(@NotNull String name);

}

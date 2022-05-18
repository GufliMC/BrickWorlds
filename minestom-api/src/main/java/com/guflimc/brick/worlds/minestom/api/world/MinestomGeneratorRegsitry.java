package com.guflimc.brick.worlds.minestom.api.world;

import net.minestom.server.instance.generator.Generator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MinestomGeneratorRegsitry {

    private final static Map<String, GeneratorFactory> generators = new ConcurrentHashMap<>();

    public static Optional<Generator> createGenerator(String id, String[] args) {
        if ( !generators.containsKey(id) ) {
            return Optional.empty();
        }

        return Optional.of(generators.get(id).create(args));
    }

    public static void register(String id, GeneratorFactory factory) {
        generators.put(id, factory);
    }

    public static Collection<String> generators() {
        return Collections.unmodifiableSet(generators.keySet());
    }

    @FunctionalInterface
    public interface GeneratorFactory {
        Generator create(String[] args);
    }

}

package org.minestombrick.worlds.api.generator;

import net.minestom.server.instance.ChunkGenerator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GeneratorRegsitry {

    private final static Map<String, GeneratorFactory> generators = new ConcurrentHashMap<>();

    public static Optional<ChunkGenerator> createGenerator(String id, String[] args) {
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
    public static interface GeneratorFactory {
        ChunkGenerator create(String[] args);
    }

}

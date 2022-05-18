package com.guflimc.brick.worlds.api.world;

import com.google.gson.Gson;
import com.guflimc.brick.worlds.api.math.Position;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldInfo {

    private final static Gson gson = new Gson();

    //

    private transient File file;

    private String name;
    private String generator;
    private List<Object> generatorArgs;
    private List<String> authors;
    private Position spawn;

    private WorldInfo() {
    }

    //

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String generator() {
        return generator;
    }

    public List<Object> generatorAgs() {
        return Collections.unmodifiableList(generatorArgs);
    }

    public List<String> authors() {
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(String name) {
        authors.add(name);
    }

    public void setAuthoers(List<String> authors) {
        this.authors = new ArrayList<>(authors);
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public void setGenerator(String generator, List<Object> generatorArgs) {
        setGenerator(generator);
        this.generatorArgs = new ArrayList<>(generatorArgs);
    }

    public Position spawn() {
        return spawn;
    }

    public void setSpawn(Position pos) {
        this.spawn = pos;
    }

    public File directory() {
        return file == null ? null : file.getParentFile();
    }

    //

    public void save() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileWriter fw = new FileWriter(file)) {
                gson.toJson(this, fw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WorldInfo of(@NotNull File file) {
        WorldInfo info = null;
        if (file.exists()) {
            try (FileReader fr = new FileReader(file)) {
                info = gson.fromJson(fr, WorldInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (info == null) {
            info = new WorldInfo();
        }

        if (info.name == null) {
            info.name = file.getParentFile().getName();
        }

        info.file = file;
        return info;
    }

}

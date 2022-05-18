package com.wollymc.brick.worlds.common.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import com.guflimc.brick.i18n.api.I18nAPI;
import com.guflimc.brick.worlds.api.WorldManager;
import com.guflimc.brick.worlds.api.world.WorldInfo;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.stream.Collectors;

public class BrickWorldsCommands {

    protected final WorldManager manager;

    public BrickWorldsCommands(WorldManager manager) {
        this.manager = manager;
    }

    @Suggestions("world")
    public List<String> worldSuggestion(CommandContext<Audience> sender, String input) {
        return manager.availableWorlds().stream()
                .map(WorldInfo::name)
                .filter(name -> name.startsWith(input))
                .collect(Collectors.toList());
    }

    @Suggestions("loadedWorld")
    public List<String> loadedWorldSuggestion(CommandContext<Audience> sender, String input) {
        return manager.loadedWorlds().stream()
                .map(WorldInfo::name)
                .filter(name -> name.startsWith(input))
                .collect(Collectors.toList());
    }

    @CommandMethod("bw list")
    public void list(Audience sender) {
        List<Component> result = new ArrayList<>();
        Collection<String> loadedWorlds = manager.loadedWorlds().stream()
                .map(WorldInfo::name).toList();

        manager.availableWorlds().stream().sorted(Comparator.comparing(WorldInfo::name))
                .filter(info -> loadedWorlds.contains(info.name()))
                .map(info -> Component.text(info.name(), NamedTextColor.GREEN))
                .forEach(result::add);

        manager.availableWorlds().stream().sorted(Comparator.comparing(WorldInfo::name))
                .filter(info -> !loadedWorlds.contains(info.name()))
                .map(info -> Component.text(info.name(), NamedTextColor.RED))
                .forEach(result::add);

        I18nAPI.get(this).send(sender, "cmd.list", result);
    }

    @CommandMethod("bw load <world>")
    public void load(Audience sender, @Argument(value = "world", suggestions = "world") String world) {
        if (manager.worldInfoByName(world).isEmpty()) {
            I18nAPI.get(this).send(sender, "cmd.error.args.world", world);
            return;
        }

        manager.loadWorld(world);

        I18nAPI.get(this).send(sender, "cmd.load", world);
    }

    @CommandMethod("bw info <world>")
    public void info(Audience sender, @Argument(value = "world", suggestions = "world") String world) {
        Optional<WorldInfo> infoOptional = manager.worldInfoByName(world);
        if (infoOptional.isEmpty()) {
            I18nAPI.get(this).send(sender, "cmd.error.args.world", world);
            return;
        }

        WorldInfo info = infoOptional.get();
        I18nAPI.get(this).send(sender, "cmd.info.result.name", info.name());
        I18nAPI.get(this).send(sender, "cmd.info.result.authors", info.authors());
        I18nAPI.get(this).send(sender, "cmd.info.result.generator", info.generator());
    }

    @CommandMethod("bw unload <world>")
    public void unload(Audience sender, @Argument(value = "world", suggestions = "loadedWorld") String world) {
        manager.unloadWorld(world);

        I18nAPI.get(this).send(sender, "cmd.unload", world);
    }

    @CommandMethod("bw save-all")
    public void saveAll(Audience sender) {
        I18nAPI.get(this).send(sender, "cmd.save-all");
        manager.saveAll();
    }

}

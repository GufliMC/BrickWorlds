package com.guflimc.brick.worlds.minestom.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import com.guflimc.brick.i18n.api.I18nAPI;
import com.guflimc.brick.worlds.api.math.Position;
import com.guflimc.brick.worlds.minestom.MinestomBrickWorldManager;
import com.guflimc.brick.worlds.minestom.api.world.MinestomGeneratorRegsitry;
import com.guflimc.brick.worlds.minestom.api.world.World;
import net.kyori.adventure.audience.Audience;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MinestomBrickWorldsCommands {

    private final MinestomBrickWorldManager manager;

    public MinestomBrickWorldsCommands(MinestomBrickWorldManager manager) {
        this.manager = manager;
    }

    @Suggestions("generator")
    public List<String> generatorSuggestion(CommandContext<Audience> sender, String input) {
        return MinestomGeneratorRegsitry.generators().stream()
                .filter(name -> name.startsWith(input))
                .collect(Collectors.toList());
    }

    @CommandMethod("bw save")
    public void save(Player sender) {
        if ( sender.getInstance() instanceof World world ) {
            I18nAPI.get(this).send(sender, "cmd.save", world.info().name());
            world.save();
        }
    }

    @CommandMethod("bw setspawn")
    public void setspawn(Player sender) {
        if ( sender.getInstance() instanceof World world ) {
            Pos pos = sender.getPosition();
            world.info().setSpawn(new Position(pos.x(), pos.y(), pos.z(), pos.yaw(), pos.pitch()));
            I18nAPI.get(this).send(sender, "cmd.setspawn", world.info().name());
        }
    }

    @CommandMethod("bw teleport|tp <world>")
    public void teleport(Player sender, @Argument(value = "world", suggestions = "loadedWorld") String world) {
        Optional<World> worldOptional = manager.worldByName(world);
        if ( worldOptional.isEmpty() ) {
            I18nAPI.get(this).send(sender, "cmd.error.args.world", world);
            return;
        }

        worldOptional.get().teleport(sender);
        I18nAPI.get(this).send(sender, "cmd.teleport", worldOptional.get().info().name());
    }

    @CommandMethod("bw create <world>")
    public void create(Audience sender, @Argument(value = "world") String world) {
        if ( manager.worldInfoByName(world).isPresent() ) {
            I18nAPI.get(this).send(sender, "cmd.create.exists", world);
            return;
        }

        manager.createWorld(world);
        I18nAPI.get(this).send(sender, "cmd.create", world);
    }

    @CommandMethod("bw create <world> <generator>")
    public void create(Audience sender, @Argument(value = "world") String world, @Argument(value="generator", suggestions = "generator") String generator) {
        if ( manager.worldInfoByName(world).isPresent() ) {
            I18nAPI.get(this).send(sender, "cmd.create.exists", world);
            return;
        }

        manager.createWorld(world, generator);
        I18nAPI.get(this).send(sender, "cmd.create", world);
    }

}

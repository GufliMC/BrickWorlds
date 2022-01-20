package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.World;
import com.gufli.brickworlds.commands.arguments.ArgumentWorld;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.Optional;

public class LoadCommand extends BrickCommand {

    private final BrickWorldManager worldManager;

    public LoadCommand(BrickWorldManager worldManager) {
        super("load");
        this.worldManager = worldManager;

        // condition
        setCondition(b -> b.permission("brickworlds.load"));

        // usage
        setInvalidUsageMessage("cmd.load.usage");

        ArgumentWord world = new ArgumentWord("world");

        // syntax
        addSyntax(this::execute, world);
    }

    private void execute(CommandSender sender, CommandContext context) {
        String worldName = context.get("world");

        World world;
        try {
            world = worldManager.loadWorld(worldName);
        } catch (IllegalArgumentException ex) {
            TranslationManager.get().send(sender, "cmd.load.invalid", worldName);
            return;
        }

        TranslationManager.get().send(sender, "cmd.load", world.worldInfo().name());
    }
}

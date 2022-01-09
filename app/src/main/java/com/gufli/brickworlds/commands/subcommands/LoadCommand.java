package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.World;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.Optional;

public class LoadCommand extends Command {

    private final BrickWorldManager worldManager;

    public LoadCommand(BrickWorldManager worldManager) {
        super("load");
        this.worldManager = worldManager;

        // condition
        setCondition((sender, commandString) -> sender instanceof ConsoleSender ||
                sender.hasPermission("brickworlds.load") ||
                (sender instanceof Player p && p.getPermissionLevel() == 4)
            );

        ArgumentWord world = new ArgumentWord("world");

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /bw load <world>"); // TODO
        });

        // syntax
        addSyntax(this::execute, world);
    }

    private void execute(CommandSender sender, CommandContext context) {
        String worldName = context.get("world");

        World world;
        try {
            world = worldManager.loadWorld(worldName);
        } catch (IllegalArgumentException ex) {
            sender.sendMessage(Component.text("The world '" + worldName + "' does not exist."));
            return;
        }

        sender.sendMessage(Component.text("The world '" + world.worldInfo().name() + "' has been loaded."));
    }
}

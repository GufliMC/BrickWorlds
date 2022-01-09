package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.World;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.Optional;

public class TeleportCommand extends Command {

    private final BrickWorldManager worldManager;

    public TeleportCommand(BrickWorldManager worldManager) {
        super("teleport", "tp");
        this.worldManager = worldManager;

        // condition
        setCondition((sender, commandString) -> sender instanceof Player p && (
                p.hasPermission("brickworlds.teleport") ||
                p.getPermissionLevel() == 4
        ));

        ArgumentWord world = new ArgumentWord("world")
                .from(worldManager.worlds().stream().map(w -> w.worldInfo().name()).toArray(String[]::new));

        setArgumentCallback((sender, exception) -> {
            sender.sendMessage("The world '" + exception.getInput() + "' does not exist.");
        }, world);

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /bw teleport <world>"); // TODO
        });

        // syntax
        addSyntax(this::execute, world);
    }

    private void execute(CommandSender sender, CommandContext context) {
        String worldName = context.get("world");
        Optional<World> world = worldManager.worldByName(worldName);
        if (world.isEmpty()) {
            return;
        }

        world.get().teleport((Player) sender);
        sender.sendMessage(Component.text("You have been teleported to world '" + world.get().worldInfo().name() + "'."));
    }
}

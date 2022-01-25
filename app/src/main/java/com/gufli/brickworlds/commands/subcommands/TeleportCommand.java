package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.World;
import com.gufli.brickworlds.commands.arguments.ArgumentWorld;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

public class TeleportCommand extends BrickCommand {

    public TeleportCommand() {
        super("teleport", "tp");

        // condition
        setCondition(b -> b.permission("brickworlds.teleport"));

        // usage
        setInvalidUsageMessage("cmd.teleport.usage");

        ArgumentWorld world = new ArgumentWorld("world");
        setInvalidArgumentMessage(world, "cmd.error.args.world");

        // syntax
        addSyntax(this::execute, world);
    }

    private void execute(CommandSender sender, CommandContext context) {
        World world = context.get("world");
        world.teleport((Player) sender);
        TranslationManager.get().send(sender, "cmd.teleport", world.worldInfo().name());
    }
}

package org.minestombrick.worlds.app.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.worlds.api.world.WorldInstance;
import org.minestombrick.worlds.app.commands.arguments.ArgumentWorld;

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
        WorldInstance world = context.get("world");
        world.teleport((Player) sender);
        I18nAPI.get(this).send(sender, "cmd.teleport", world.worldInfo().name());
    }
}

package org.minestombrick.worlds.app.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.worlds.api.world.WorldInstance;
import org.minestombrick.worlds.app.commands.arguments.ArgumentWorld;

public class SaveCommand extends BrickCommand {

    public SaveCommand() {
        super("save");

        // condition
        setCondition(b -> b.permission("brickworlds.save"));

        // usage
        setInvalidUsageMessage("cmd.save.usage");

        ArgumentWorld worldArg = new ArgumentWorld("world");
        setInvalidArgumentMessage(worldArg, "cmd.error.args.world");

        // syntax
        addSyntax(this::execute, worldArg);

        addConditionalSyntax(b -> b.playerOnly(), (sender, context) -> {
            Player player = (Player) sender;
            if ( player.getInstance() instanceof WorldInstance world ) {
                I18nAPI.get(this).send(sender, "cmd.save", world.worldInfo().name());
                world.save();
            }
        });
    }

    private void execute(CommandSender sender, CommandContext context) {
        WorldInstance world = context.get("world");
        I18nAPI.get(this).send(sender, "cmd.save", world.worldInfo().name());
        world.save();
    }
}

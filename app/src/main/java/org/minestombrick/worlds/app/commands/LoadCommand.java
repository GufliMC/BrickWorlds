package org.minestombrick.worlds.app.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.worlds.api.WorldAPI;
import org.minestombrick.worlds.api.world.WorldInstance;

public class LoadCommand extends BrickCommand {


    public LoadCommand() {
        super("load");

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

        WorldInstance world;
        try {
            world = WorldAPI.get().loadWorld(worldName);
        } catch (IllegalArgumentException ex) {
            I18nAPI.get(this).send(sender, "cmd.load.invalid", worldName);
            return;
        }

        I18nAPI.get(this).send(sender, "cmd.load", world.worldInfo().name());
    }
}

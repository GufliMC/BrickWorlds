package org.minestombrick.worlds.app.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.worlds.api.WorldAPI;

public class SaveAllCommand extends BrickCommand {

    public SaveAllCommand() {
        super("save-all");

        // condition
        setCondition(b -> b.permission("brickworlds.save-all"));

        // usage
        setDefaultExecutor(this::execute);
    }

    private void execute(CommandSender sender, CommandContext context) {
        I18nAPI.get(this).send(sender, "cmd.save-all");
        WorldAPI.get().saveAll();
    }
}

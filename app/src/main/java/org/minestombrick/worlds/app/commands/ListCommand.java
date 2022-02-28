package org.minestombrick.worlds.app.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.worlds.api.WorldAPI;

import java.util.stream.Collectors;

public class ListCommand extends BrickCommand {

    public ListCommand() {
        super("list");

        // condition
        setCondition(b -> b.permission("brickworlds.list"));

        // syntax
        addSyntax(this::execute);
    }

    private void execute(CommandSender sender, CommandContext context) {
        I18nAPI.get(this).send(sender, "cmd.list",
                WorldAPI.get().worlds().stream()
                        .map(w -> w.worldInfo().name())
                        .collect(Collectors.joining(", "))
        );
    }
}

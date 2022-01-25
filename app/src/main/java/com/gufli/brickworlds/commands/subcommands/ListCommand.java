package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import com.gufli.brickworlds.WorldAPI;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;

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
        TranslationManager.get().send(sender, "cmd.list",
                WorldAPI.worlds().stream()
                        .map(w -> w.worldInfo().name())
                        .collect(Collectors.joining(", "))
        );
    }
}

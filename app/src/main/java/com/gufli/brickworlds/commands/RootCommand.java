package com.gufli.brickworlds.commands;

import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RootCommand extends Command {

    public RootCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);

        setCondition(((sender, commandString) ->
                getSubcommands().stream().anyMatch(sub -> sub.getCondition() == null ||
                        sub.getCondition().canUse(sender, commandString))));
    }

}

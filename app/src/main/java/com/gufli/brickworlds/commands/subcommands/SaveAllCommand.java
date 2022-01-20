package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import com.gufli.brickworlds.BrickWorldManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

public class SaveAllCommand extends BrickCommand {

    private final BrickWorldManager worldManager;

    public SaveAllCommand(BrickWorldManager worldManager) {
        super("save-all");
        this.worldManager = worldManager;

        // condition
        setCondition(b -> b.permission("brickworlds.save-all"));

        // usage
        setDefaultExecutor(this::execute);
    }

    private void execute(CommandSender sender, CommandContext context) {
        TranslationManager.get().send(sender, "cmd.save-all");
        worldManager.saveAll();
    }
}

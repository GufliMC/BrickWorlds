package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickworlds.BrickWorldManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public class SaveAllCommand extends Command {

    private final BrickWorldManager worldManager;

    public SaveAllCommand(BrickWorldManager worldManager) {
        super("save-all");
        this.worldManager = worldManager;

        // condition
        setCondition((sender, commandString) -> sender instanceof ConsoleSender ||
                sender.hasPermission("brickworlds.save-all"));

        // usage
        setDefaultExecutor(this::execute);
    }

    private void execute(CommandSender sender, CommandContext context) {
        sender.sendMessage(Component.text("Saving all worlds..."));
        worldManager.saveAll();
    }
}

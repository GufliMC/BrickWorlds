package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.World;
import com.gufli.brickworlds.world.WorldInstanceContainer;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.Optional;

public class SetSpawnCommand extends Command {

    private final BrickWorldManager worldManager;

    public SetSpawnCommand(BrickWorldManager worldManager) {
        super("setspawn");
        this.worldManager = worldManager;

        // condition
        setCondition((sender, commandString) -> sender instanceof Player &&
                sender.hasPermission("brickworlds.setspawn"));

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /bw setspawn"); // TODO
        });

        // syntax
        addSyntax(this::execute);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;

        if ( !(player.getInstance() instanceof WorldInstanceContainer wic) ) {
            sender.sendMessage("Your current world is not being tracked.");
            return;
        }

        wic.worldInfo().setSpawn(player.getPosition());
        wic.worldInfo().save();

        sender.sendMessage(Component.text("The spawn of this world has been set to your current location."));
    }
}

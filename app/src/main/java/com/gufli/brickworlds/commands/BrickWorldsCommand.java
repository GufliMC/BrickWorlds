package com.gufli.brickworlds.commands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.commands.subcommands.*;

public class BrickWorldsCommand extends BrickCommand {

    public BrickWorldsCommand(BrickWorldManager worldManager) {
        super("brickworlds", "bw");

        setupCommandGroupDefaults();

        addSubcommand(new ListCommand(worldManager));
        addSubcommand(new SaveAllCommand(worldManager));
        addSubcommand(new SaveCommand(worldManager));
        addSubcommand(new TeleportCommand(worldManager));
        addSubcommand(new SetSpawnCommand(worldManager));
        addSubcommand(new LoadCommand(worldManager));
    }

}

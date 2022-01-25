package com.gufli.brickworlds.commands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.commands.subcommands.*;

public class BrickWorldsCommand extends BrickCommand {

    public BrickWorldsCommand() {
        super("brickworlds", "bw");

        setupCommandGroupDefaults();

        addSubcommand(new ListCommand());
        addSubcommand(new SaveAllCommand());
        addSubcommand(new SaveCommand());
        addSubcommand(new TeleportCommand());
        addSubcommand(new SetSpawnCommand());
        addSubcommand(new LoadCommand());
    }

}

package com.gufli.brickworlds.commands;

import com.gufli.brickworlds.BrickWorldManager;

public class BrickWorldsCommand extends RootCommand {

    public BrickWorldsCommand(BrickWorldManager worldManager) {
        super("brickworlds", "bw");

        addSubcommand(new SaveAllCommand(worldManager));
    }

}

package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.World;
import com.gufli.brickworlds.commands.arguments.ArgumentWorld;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;

public class SaveCommand extends BrickCommand {

    private final BrickWorldManager worldManager;

    public SaveCommand(BrickWorldManager worldManager) {
        super("save");
        this.worldManager = worldManager;

        // condition
        setCondition(b -> b.permission("brickworlds.save"));

        // usage
        setInvalidUsageMessage("cmd.save.usage");

        ArgumentWorld world = new ArgumentWorld("world");
        setInvalidArgumentMessage(world, "cmd.error.args.world");

        // syntax
        addSyntax(this::execute, world);
    }

    private void execute(CommandSender sender, CommandContext context) {
        World world = context.get("world");
        TranslationManager.get().send(sender, "cmd.save", world.worldInfo().name());
        world.save();
    }
}

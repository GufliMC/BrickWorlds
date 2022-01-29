package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import com.gufli.brickworlds.World;
import com.gufli.brickworlds.WorldAPI;
import com.gufli.brickworlds.commands.arguments.ArgumentWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.ExecutionType;

import java.util.concurrent.CompletableFuture;

public class SaveCommand extends BrickCommand {

    public SaveCommand() {
        super("save");

        // condition
        setCondition(b -> b.permission("brickworlds.save"));

        // usage
        setInvalidUsageMessage("cmd.save.usage");

        ArgumentWorld worldArg = new ArgumentWorld("world");
        setInvalidArgumentMessage(worldArg, "cmd.error.args.world");

        // syntax
        addSyntax(this::execute, worldArg);

        addConditionalSyntax(b -> b.playerOnly(), (sender, context) -> {
            Player player = (Player) sender;
            if ( player.getInstance() instanceof World world ) {
                TranslationManager.get().send(sender, "cmd.save", world.worldInfo().name());
                world.save();
            }
        });
    }

    private void execute(CommandSender sender, CommandContext context) {
        World world = context.get("world");
        TranslationManager.get().send(sender, "cmd.save", world.worldInfo().name());
        world.save();
    }
}

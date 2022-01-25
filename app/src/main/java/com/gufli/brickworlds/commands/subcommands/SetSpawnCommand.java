package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.world.WorldInstanceContainer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

public class SetSpawnCommand extends BrickCommand {

    public SetSpawnCommand() {
        super("setspawn");

        // condition
        setCondition(b -> b.permission("brickworlds.setspawn"));

        // usage
        setInvalidUsageMessage("cmd.setspawn.usage");

        // syntax
        addSyntax(this::execute);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;

        if (!(player.getInstance() instanceof WorldInstanceContainer wic)) {
            TranslationManager.get().send(sender, "cmd.setspawn.invalid");
            return;
        }

        wic.worldInfo().setSpawn(player.getPosition());
        wic.worldInfo().save();

        TranslationManager.get().send(sender, "cmd.setspawn", wic.worldInfo().name());
    }
}

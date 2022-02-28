package org.minestombrick.worlds.app.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.worlds.api.world.WorldInstance;

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

        if (!(player.getInstance() instanceof WorldInstance wi)) {
            I18nAPI.get(this).send(sender, "cmd.setspawn.invalid");
            return;
        }

        wi.worldInfo().setSpawn(player.getPosition());
        wi.worldInfo().save();

        I18nAPI.get(this).send(sender, "cmd.setspawn", wi.worldInfo().name());
    }
}

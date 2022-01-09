package com.gufli.brickworlds.commands.subcommands;

import com.gufli.brickworlds.BrickWorldManager;
import com.gufli.brickworlds.World;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.Optional;

public class SaveCommand extends Command {

    private final BrickWorldManager worldManager;

    public SaveCommand(BrickWorldManager worldManager) {
        super("save");
        this.worldManager = worldManager;

        // condition
        setCondition((sender, commandString) -> sender instanceof ConsoleSender ||
                sender.hasPermission("brickworlds.save"));

        ArgumentWord world = new ArgumentWord("world")
                .from(worldManager.worlds().stream().map(w -> w.worldInfo().name()).toArray(String[]::new));

        setArgumentCallback((sender, exception) -> {
            sender.sendMessage("The world '" + exception.getInput() + "' does not exist.");
        }, world);

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /bw save <world>"); // TODO
        });

        // syntax
        addSyntax(this::execute, world);
    }

    private void execute(CommandSender sender, CommandContext context) {
        String worldName = context.get("world");
        Optional<World> world = worldManager.worldByName(worldName);
        if (world.isEmpty()) {
            return;
        }

        sender.sendMessage(Component.text("Saving the world '" + world.get().worldInfo().name() + "'..."));
        world.get().save();
    }
}

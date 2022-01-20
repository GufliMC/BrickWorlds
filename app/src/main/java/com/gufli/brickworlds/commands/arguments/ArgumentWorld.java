package com.gufli.brickworlds.commands.arguments;

import com.gufli.brickworlds.World;
import com.gufli.brickworlds.WorldAPI;
import net.minestom.server.command.builder.NodeMaker;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.network.packet.server.play.DeclareCommandsPacket;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArgumentWorld extends Argument<World> {

    public static final int WORLD_NOT_EXIST = 1;

    public ArgumentWorld(@NotNull String id) {
        super(id);
    }

    @Override
    public @NotNull World parse(@NotNull String input) throws ArgumentSyntaxException {
        return WorldAPI.worldByName(input).orElseThrow(() ->
                new ArgumentSyntaxException("World does not exist.", input, WORLD_NOT_EXIST));
    }

    @Override
    public void processNodes(@NotNull NodeMaker nodeMaker, boolean executable) {
        List<World> groups = new ArrayList<>(WorldAPI.worlds());

        // Create a primitive array for mapping
        DeclareCommandsPacket.Node[] nodes = new DeclareCommandsPacket.Node[groups.size()];

        // Create a node for each restrictions as literal
        for (int i = 0; i < nodes.length; i++) {
            DeclareCommandsPacket.Node argumentNode = new DeclareCommandsPacket.Node();

            argumentNode.flags = DeclareCommandsPacket.getFlag(DeclareCommandsPacket.NodeType.LITERAL,
                    executable, false, false);
            argumentNode.name = groups.get(i).worldInfo().name();
            nodes[i] = argumentNode;
        }
        nodeMaker.addNodes(nodes);
    }
}

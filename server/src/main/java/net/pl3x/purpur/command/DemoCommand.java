package net.pl3x.purpur.command;

import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.server.level.EntityPlayer;
import net.pl3x.purpur.PurpurConfig;
import org.bukkit.craftbukkit.util.CraftChatMessage;

import java.util.Collection;
import java.util.Collections;

public class DemoCommand {
    public static void register(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> dispatcher) {
        dispatcher.register(CommandDispatcher.literal("demo")
                .requires((listener) -> {
                    return listener.hasPermission(2);
                })
                .executes((context) -> {
                    return execute(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException()));
                })
                .then(CommandDispatcher.argument("targets", ArgumentEntity.players())
                        .executes((context) -> {
                            return execute(context.getSource(), ArgumentEntity.getPlayers(context, "targets"));
                        })
                )
        ).setPermission("bukkit.command.demo");
    }

    private static int execute(CommandListenerWrapper sender, Collection<EntityPlayer> targets) {
        for (EntityPlayer player : targets) {
            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.demo(), 0);
            player.playerConnection.sendPacket(packet);
            String output = String.format(PurpurConfig.demoCommandOutput, player.getProfile().getName());
            sender.sendMessage(CraftChatMessage.fromStringOrNull(output), false);
        }
        return targets.size();
    }
}

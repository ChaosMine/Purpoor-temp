package net.pl3x.purpur.command;

import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.level.EntityPlayer;
import net.pl3x.purpur.task.TPSBarTask;

public class TPSBarCommand {
    public static void register(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> dispatcher) {
        dispatcher.register(CommandDispatcher.literal("tpsbar")
                .requires((listener) -> {
                    return listener.hasPermission(2);
                })
                .executes((context) -> {
                    return execute(context.getSource(), context.getSource().getPlayerOrException());
                })
        ).setPermission("bukkit.command.tpsbar");
    }

    private static int execute(CommandListenerWrapper sender, EntityPlayer player) {
        if (player != null) {
            TPSBarTask.togglePlayer(player.getBukkitEntity());
            return 1;
        }
        return 0;
    }
}

package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.level.EnumGamemode;

import java.util.Iterator;

public class CommandGamemodeDefault {

    public static void a(CommandDispatcher<CommandListenerWrapper> commanddispatcher) {
        LiteralArgumentBuilder<CommandListenerWrapper> literalargumentbuilder = (LiteralArgumentBuilder) net.minecraft.commands.CommandDispatcher.a("defaultgamemode").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        });
        EnumGamemode[] aenumgamemode = EnumGamemode.values();
        int i = aenumgamemode.length;

        for (int j = 0; j < i; ++j) {
            EnumGamemode enumgamemode = aenumgamemode[j];

            if (enumgamemode != EnumGamemode.NOT_SET) {
                literalargumentbuilder.then(net.minecraft.commands.CommandDispatcher.a(enumgamemode.b()).executes((commandcontext) -> {
                    return a((CommandListenerWrapper) commandcontext.getSource(), enumgamemode);
                }));
            }
        }

        commanddispatcher.register(literalargumentbuilder);
    }

    private static int a(CommandListenerWrapper commandlistenerwrapper, EnumGamemode enumgamemode) {
        int i = 0;
        MinecraftServer minecraftserver = commandlistenerwrapper.getServer();

        minecraftserver.a(enumgamemode);
        if (minecraftserver.getForceGamemode()) {
            Iterator iterator = minecraftserver.getPlayerList().getPlayers().iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                if (entityplayer.playerInteractManager.getGameMode() != enumgamemode) {
                    // Paper start - handle event cancelling the change
                    org.bukkit.event.player.PlayerGameModeChangeEvent event = entityplayer.setGamemode(enumgamemode, org.bukkit.event.player.PlayerGameModeChangeEvent.Cause.DEFAULT_GAMEMODE, net.kyori.adventure.text.Component.text("Failed to set the gamemode of '" + entityplayer.getName() + "'", net.kyori.adventure.text.format.NamedTextColor.RED));
                    if (event != null && event.isCancelled()) {
                        commandlistenerwrapper.sendMessage(io.papermc.paper.adventure.PaperAdventure.asVanilla(event.cancelMessage()), false);
                        continue;
                    }
                    // Paper end
                    ++i;
                }
            }
        }

        commandlistenerwrapper.sendMessage(new ChatMessage("commands.defaultgamemode.success", new Object[]{enumgamemode.c()}), true);
        return i;
    }
}

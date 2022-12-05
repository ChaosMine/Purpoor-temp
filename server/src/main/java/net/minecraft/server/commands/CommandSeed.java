package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.earthcomputer.secureseed.Globals;
import net.minecraft.EnumChatFormat;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.ChatClickable;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.ChatComponentUtils;
import net.minecraft.network.chat.ChatHoverable;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;

public class CommandSeed {

    public static void a(CommandDispatcher<CommandListenerWrapper> commanddispatcher, boolean flag) {
        commanddispatcher.register((net.minecraft.commands.CommandDispatcher.a("seed").requires((commandlistenerwrapper) -> {
            return !flag || commandlistenerwrapper.hasPermission(2);
        })).executes((commandcontext) -> {
            Globals.setupGlobals(commandcontext.getSource().getWorld());
            String seedStr = Globals.seedToString(Globals.worldSeed);
            IChatBaseComponent result = ChatComponentUtils.a(new ChatComponentText(seedStr)).format(style -> {
                return style.setColor(EnumChatFormat.GREEN)
                  .setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.COPY_TO_CLIPBOARD, seedStr))
                  .setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, new ChatMessage("chat.copy.click")))
                  .setInsertion(seedStr);
            });
            commandcontext.getSource().sendMessage(new ChatMessage("commands.seed.success", result), false);
            return (int) Globals.worldSeed[0];
        }));
    }
}

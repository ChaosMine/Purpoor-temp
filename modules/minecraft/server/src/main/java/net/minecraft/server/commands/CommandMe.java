package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.SystemUtils;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;

public class CommandMe {
	public static void a(CommandDispatcher<CommandListenerWrapper> commanddispatcher) {
		commanddispatcher.register(net.minecraft.commands.CommandDispatcher.a("me")
			.then(net.minecraft.commands.CommandDispatcher.a("action", StringArgumentType.greedyString()).executes(ctx -> {
				String s = StringArgumentType.getString(ctx, "action");
				Entity entity = ctx.getSource().getEntity();
				MinecraftServer minecraftserver = ctx.getSource().getServer();

				if (entity != null) {
					minecraftserver.getPlayerList().sendMessage(a(ctx, s), ChatMessageType.CHAT, entity.getUniqueID());
				} else {
					minecraftserver.getPlayerList().sendMessage(a(ctx, s), ChatMessageType.SYSTEM, SystemUtils.b);
				}

				return 1;
			})));
	}

	private static IChatBaseComponent a(CommandContext<CommandListenerWrapper> commandcontext, String s) {
		return new ChatMessage("chat.type.emote", commandcontext.getSource().getScoreboardDisplayName(), s);
	}
}

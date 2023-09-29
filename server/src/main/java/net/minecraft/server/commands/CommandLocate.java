package net.minecraft.server.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import java.util.Map.Entry;

import net.minecraft.EnumChatFormat;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatClickable;
import net.minecraft.network.chat.ChatComponentUtils;
import net.minecraft.network.chat.ChatHoverable;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;

public class CommandLocate {
	private static final SimpleCommandExceptionType a = new SimpleCommandExceptionType(new ChatMessage("commands.locate.failed"));

	public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> var0) {
		LiteralArgumentBuilder<CommandListenerWrapper> var1 = CommandDispatcher.a("locate").requires(var0x -> var0x.hasPermission(2));

		for (Entry<String, StructureGenerator<?>> var3 : StructureGenerator.a.entrySet()) {
			var1 = var1.then(CommandDispatcher.a(var3.getKey()).executes(var1x -> a(var1x.getSource(), var3.getValue())));
		}

		var0.register(var1);
	}

	private static int a(CommandListenerWrapper var0, StructureGenerator<?> var1) throws CommandSyntaxException {
		BlockPosition var2 = new BlockPosition(var0.getPosition());
		BlockPosition var3 = var0.getWorld().a(var1, var2, 100, false);
		if (var3 == null) {
			throw a.create();
		} else {
			return a(var0, var1.i(), var2, var3, "commands.locate.success");
		}
	}

	public static int a(CommandListenerWrapper var0, String var1, BlockPosition var2, BlockPosition var3, String var4) {
		int var5 = MathHelper.d(a(var2.getX(), var2.getZ(), var3.getX(), var3.getZ()));
		IChatBaseComponent var6 = ChatComponentUtils.a(new ChatMessage("chat.coordinates", var3.getX(), "~", var3.getZ()))
			.format(
				var1x -> var1x.setColor(EnumChatFormat.GREEN)
					.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, "/tp @s " + var3.getX() + " ~ " + var3.getZ()))
					.setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, new ChatMessage("chat.coordinates.tooltip")))
			);
		var0.sendMessage(new ChatMessage(var4, var1, var6, var5), false);
		return var5;
	}

	private static float a(int var0, int var1, int var2, int var3) {
		int var4 = var2 - var0;
		int var5 = var3 - var1;
		return MathHelper.c((float) (var4 * var4 + var5 * var5));
	}
}

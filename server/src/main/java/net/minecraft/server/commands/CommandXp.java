package net.minecraft.server.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.player.EntityHuman;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

public class CommandXp {
	private static final SimpleCommandExceptionType a = new SimpleCommandExceptionType(new ChatMessage("commands.experience.set.points.invalid"));

	public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> var0) {
		LiteralCommandNode<CommandListenerWrapper> var1 = var0.register(
			CommandDispatcher.a("experience")
				.requires(var0x -> var0x.hasPermission(2))
				.then(
					CommandDispatcher.a("add")
						.then(
							CommandDispatcher.a("targets", ArgumentEntity.d())
								.then(
									CommandDispatcher.a("amount", IntegerArgumentType.integer())
										.executes(
											var0x -> a(
												var0x.getSource(),
												ArgumentEntity.f(var0x, "targets"),
												IntegerArgumentType.getInteger(var0x, "amount"),
												Unit.POINTS
											)
										)
										.then(
											CommandDispatcher.a("points")
												.executes(
													var0x -> a(
														var0x.getSource(),
														ArgumentEntity.f(var0x, "targets"),
														IntegerArgumentType.getInteger(var0x, "amount"),
														Unit.POINTS
													)
												)
										)
										.then(
											CommandDispatcher.a("levels")
												.executes(
													var0x -> a(
														var0x.getSource(),
														ArgumentEntity.f(var0x, "targets"),
														IntegerArgumentType.getInteger(var0x, "amount"),
														CommandXp.Unit.LEVELS
													)
												)
										)
								)
						)
				)
				.then(
					CommandDispatcher.a("set")
						.then(
							CommandDispatcher.a("targets", ArgumentEntity.d())
								.then(
									CommandDispatcher.a("amount", IntegerArgumentType.integer(0))
										.executes(
											var0x -> b(
												var0x.getSource(),
												ArgumentEntity.f(var0x, "targets"),
												IntegerArgumentType.getInteger(var0x, "amount"),
												Unit.POINTS
											)
										)
										.then(
											CommandDispatcher.a("points")
												.executes(
													var0x -> b(
														var0x.getSource(),
														ArgumentEntity.f(var0x, "targets"),
														IntegerArgumentType.getInteger(var0x, "amount"),
														Unit.POINTS
													)
												)
										)
										.then(
											CommandDispatcher.a("levels")
												.executes(
													var0x -> b(
														var0x.getSource(),
														ArgumentEntity.f(var0x, "targets"),
														IntegerArgumentType.getInteger(var0x, "amount"),
														CommandXp.Unit.LEVELS
													)
												)
										)
								)
						)
				)
				.then(
					CommandDispatcher.a("query")
						.then(
							CommandDispatcher.a("targets", ArgumentEntity.c())
								.then(
									CommandDispatcher.a("points")
										.executes(var0x -> a(var0x.getSource(), ArgumentEntity.e(var0x, "targets"), CommandXp.Unit.POINTS))
								)
								.then(
									CommandDispatcher.a("levels")
										.executes(var0x -> a(var0x.getSource(), ArgumentEntity.e(var0x, "targets"), CommandXp.Unit.LEVELS))
								)
						)
				)
		);
		var0.register(CommandDispatcher.a("xp").requires(var0x -> var0x.hasPermission(2)).redirect(var1));
	}

	private static int a(CommandListenerWrapper var0, EntityPlayer var1, Unit var2) {
		int var3 = var2.f.applyAsInt(var1);
		var0.sendMessage(new ChatMessage("commands.experience.query." + var2.e, var1.getScoreboardDisplayName(), var3), false);
		return var3;
	}

	private static int a(CommandListenerWrapper var0, Collection<? extends EntityPlayer> var1, int var2, Unit var3) {
		for (EntityPlayer entityPlayer : var1) {
			var3.c.accept(entityPlayer, var2);
		}
		if (var1.size() == 1) {
			var0.sendMessage(new ChatMessage("commands.experience.add." + var3.e + ".success.single", var2, var1.iterator().next().getScoreboardDisplayName()), true);
		} else {
			var0.sendMessage(new ChatMessage("commands.experience.add." + var3.e + ".success.multiple", var2, var1.size()), true);
		}
		return var1.size();
	}

	private static int b(CommandListenerWrapper var0, Collection<? extends EntityPlayer> var1, int var2, Unit var3) throws CommandSyntaxException {
		int var4 = 0;
		for (EntityPlayer entityPlayer : var1) {
			if (!var3.d.test(entityPlayer, var2)) continue;
			++var4;
		}
		if (var4 == 0) {
			throw a.create();
		}
		if (var1.size() == 1) {
			var0.sendMessage(new ChatMessage("commands.experience.set." + var3.e + ".success.single", var2, var1.iterator().next().getScoreboardDisplayName()), true);
		} else {
			var0.sendMessage(new ChatMessage("commands.experience.set." + var3.e + ".success.multiple", var2, var1.size()), true);
		}
		return var1.size();
	}

	enum Unit {
		POINTS("points", EntityHuman::giveExp, (var0, var1) -> {
			if (var1 >= var0.getExpToLevel()) {
				return false;
			}
			var0.a(var1);
			return true;
		}, var0 -> MathHelper.d(var0.exp * (float) var0.getExpToLevel())),
		LEVELS("levels", EntityPlayer::levelDown, (var0, var1) -> {
			var0.b(var1);
			return true;
		}, var0 -> var0.expLevel);

		public final BiConsumer<EntityPlayer, Integer> c;
		public final BiPredicate<EntityPlayer, Integer> d;
		public final String e;
		private final ToIntFunction<EntityPlayer> f;

		Unit(String var2, BiConsumer<EntityPlayer, Integer> var3, BiPredicate<EntityPlayer, Integer> var4, ToIntFunction<EntityPlayer> var5) {
			this.c = var3;
			this.e = var2;
			this.d = var4;
			this.f = var5;
		}
	}
}

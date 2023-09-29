package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.ICompletionProvider;
import net.minecraft.network.chat.ChatComponentUtils;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.server.packs.repository.ResourcePackLoader;
import net.minecraft.server.packs.repository.ResourcePackRepository;
import net.minecraft.commands.CommandDispatcher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDatapack {
	private static final DynamicCommandExceptionType a = new DynamicCommandExceptionType(var0 -> new ChatMessage("commands.datapack.unknown", var0));
	private static final DynamicCommandExceptionType b = new DynamicCommandExceptionType(var0 -> new ChatMessage("commands.datapack.enable.failed", var0));
	private static final DynamicCommandExceptionType c = new DynamicCommandExceptionType(var0 -> new ChatMessage("commands.datapack.disable.failed", var0));
	private static final SuggestionProvider<CommandListenerWrapper> d = (var0, var1) -> ICompletionProvider.b(
		var0.getSource().getServer().getResourcePackRepository().d().stream().map(StringArgumentType::escapeIfRequired), var1
	);
	private static final SuggestionProvider<CommandListenerWrapper> e = (var0, var1) -> {
		ResourcePackRepository var2 = var0.getSource().getServer().getResourcePackRepository();
		Collection<String> var3 = var2.d();
		return ICompletionProvider.b(var2.b().stream().filter(var1x -> !var3.contains(var1x)).map(StringArgumentType::escapeIfRequired), var1);
	};

	public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> var0) {
		var0.register(
			CommandDispatcher.a("datapack")
				.requires(var0x -> var0x.hasPermission(2))
				.then(
					CommandDispatcher.a("enable")
						.then(
							CommandDispatcher.a("name", StringArgumentType.string())
								.suggests(e)
								.executes(
									var0x -> a(
										var0x.getSource(),
										a(var0x, "name", true),
										(var0xx, var1) -> var1.h().a(var0xx, var1, var0xxx -> var0xxx, false)
									)
								)
								.then(
									CommandDispatcher.a("after")
										.then(
											CommandDispatcher.a("existing", StringArgumentType.string())
												.suggests(d)
												.executes(
													var0x -> a(
														var0x.getSource(),
														a(var0x, "name", true),
														(var1, var2) -> var1.add(var1.indexOf(a(var0x, "existing", false)) + 1, var2)
													)
												)
										)
								)
								.then(
									CommandDispatcher.a("before")
										.then(
											CommandDispatcher.a("existing", StringArgumentType.string())
												.suggests(d)
												.executes(
													var0x -> a(
														var0x.getSource(),
														a(var0x, "name", true),
														(var1, var2) -> var1.add(var1.indexOf(a(var0x, "existing", false)), var2)
													)
												)
										)
								)
								.then(CommandDispatcher.a("last").executes(var0x -> a(var0x.getSource(), a(var0x, "name", true), List::add)))
								.then(
									CommandDispatcher.a("first")
										.executes(var0x -> a(var0x.getSource(), a(var0x, "name", true), (var0xx, var1) -> var0xx.add(0, var1)))
								)
						)
				)
				.then(
					CommandDispatcher.a("disable")
						.then(
							CommandDispatcher.a("name", StringArgumentType.string())
								.suggests(d)
								.executes(var0x -> a(var0x.getSource(), a(var0x, "name", false)))
						)
				)
				.then(
					CommandDispatcher.a("list")
						.executes(var0x -> a(var0x.getSource()))
						.then(CommandDispatcher.a("available").executes(var0x -> b(var0x.getSource())))
						.then(CommandDispatcher.a("enabled").executes(var0x -> c(var0x.getSource())))
				)
		);
	}

	private static int a(CommandListenerWrapper var0, ResourcePackLoader var1, CommandDatapack.a var2) throws CommandSyntaxException {
		ResourcePackRepository var3 = var0.getServer().getResourcePackRepository();
		List<ResourcePackLoader> var4 = Lists.newArrayList(var3.e());
		var2.apply(var4, var1);
		var0.sendMessage(new ChatMessage("commands.datapack.modify.enable", var1.a(true)), true);
		CommandReload.a(var4.stream().map(ResourcePackLoader::e).collect(Collectors.toList()), var0);
		return var4.size();
	}

	private static int a(CommandListenerWrapper var0, ResourcePackLoader var1) {
		ResourcePackRepository var2 = var0.getServer().getResourcePackRepository();
		List<ResourcePackLoader> var3 = Lists.newArrayList(var2.e());
		var3.remove(var1);
		var0.sendMessage(new ChatMessage("commands.datapack.modify.disable", var1.a(true)), true);
		CommandReload.a(var3.stream().map(ResourcePackLoader::e).collect(Collectors.toList()), var0);
		return var3.size();
	}

	private static int a(CommandListenerWrapper var0) {
		return c(var0) + b(var0);
	}

	private static int b(CommandListenerWrapper var0) {
		ResourcePackRepository var1 = var0.getServer().getResourcePackRepository();
		var1.a();
		Collection<? extends ResourcePackLoader> var2 = var1.e();
		Collection<? extends ResourcePackLoader> var3 = var1.c();
		List<ResourcePackLoader> var4 = var3.stream().filter(var1x -> !var2.contains(var1x)).collect(Collectors.toList());
		if (var4.isEmpty()) {
			var0.sendMessage(new ChatMessage("commands.datapack.list.available.none"), false);
		} else {
			var0.sendMessage(
				new ChatMessage("commands.datapack.list.available.success", var4.size(), ChatComponentUtils.b(var4, var0x -> var0x.a(false))), false
			);
		}

		return var4.size();
	}

	private static int c(CommandListenerWrapper var0) {
		ResourcePackRepository var1 = var0.getServer().getResourcePackRepository();
		var1.a();
		Collection<? extends ResourcePackLoader> var2 = var1.e();
		if (var2.isEmpty()) {
			var0.sendMessage(new ChatMessage("commands.datapack.list.enabled.none"), false);
		} else {
			var0.sendMessage(new ChatMessage("commands.datapack.list.enabled.success", var2.size(), ChatComponentUtils.b(var2, var0x -> var0x.a(true))), false);
		}

		return var2.size();
	}

	private static ResourcePackLoader a(CommandContext<CommandListenerWrapper> var0, String var1, boolean var2) throws CommandSyntaxException {
		String var3 = StringArgumentType.getString(var0, var1);
		ResourcePackRepository var4 = var0.getSource().getServer().getResourcePackRepository();
		ResourcePackLoader var5 = var4.a(var3);
		if (var5 == null) {
			throw a.create(var3);
		} else {
			boolean var6 = var4.e().contains(var5);
			if (var2 && var6) {
				throw b.create(var3);
			} else if (!var2 && !var6) {
				throw c.create(var3);
			} else {
				return var5;
			}
		}
	}

	interface a {
		void apply(List<ResourcePackLoader> var1, ResourcePackLoader var2) throws CommandSyntaxException;
	}
}

package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.SystemUtils;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.ICompletionProvider;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.world.entity.EnumItemSlot;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ArgumentInventorySlot implements ArgumentType<Integer> {

	private static final Collection<String> a = Arrays.asList("container.5", "12", "weapon");
	private static final DynamicCommandExceptionType b = new DynamicCommandExceptionType((object) -> {
		return new ChatMessage("slot.unknown", object);
	});
	private static final Map<String, Integer> c = SystemUtils.a(new HashMap<>(), map -> {
		int i;

		for (i = 0; i < 54; ++i) {
			map.put("container." + i, i);
		}

		for (i = 0; i < 9; ++i) {
			map.put("hotbar." + i, i);
		}

		for (i = 0; i < 27; ++i) {
			map.put("inventory." + i, 9 + i);
		}

		for (i = 0; i < 27; ++i) {
			map.put("enderchest." + i, 200 + i);
		}

		for (i = 0; i < 8; ++i) {
			map.put("villager." + i, 300 + i);
		}

		for (i = 0; i < 15; ++i) {
			map.put("horse." + i, 500 + i);
		}

		map.put("weapon", 98);
		map.put("weapon.mainhand", 98);
		map.put("weapon.offhand", 99);
		map.put("armor.head", 100 + EnumItemSlot.HEAD.b());
		map.put("armor.chest", 100 + EnumItemSlot.CHEST.b());
		map.put("armor.legs", 100 + EnumItemSlot.LEGS.b());
		map.put("armor.feet", 100 + EnumItemSlot.FEET.b());
		map.put("horse.saddle", 400);
		map.put("horse.armor", 401);
		map.put("horse.chest", 499);
	});

	public ArgumentInventorySlot() {
	}

	public static ArgumentInventorySlot a() {
		return new ArgumentInventorySlot();
	}

	public static int a(CommandContext<CommandListenerWrapper> commandcontext, String s) {
		return commandcontext.getArgument(s, Integer.class);
	}

	public Integer parse(StringReader stringreader) throws CommandSyntaxException {
		String s = stringreader.readUnquotedString();

		if (!ArgumentInventorySlot.c.containsKey(s)) {
			throw ArgumentInventorySlot.b.create(s);
		} else {
			return ArgumentInventorySlot.c.get(s);
		}
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandcontext, SuggestionsBuilder suggestionsbuilder) {
		return ICompletionProvider.b(ArgumentInventorySlot.c.keySet(), suggestionsbuilder);
	}

	public Collection<String> getExamples() {
		return ArgumentInventorySlot.a;
	}
}

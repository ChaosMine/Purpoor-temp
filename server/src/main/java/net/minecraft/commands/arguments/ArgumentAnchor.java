package net.minecraft.commands.arguments;

import com.google.common.collect.Maps;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class ArgumentAnchor implements ArgumentType<ArgumentAnchor.Anchor> {
	private static final Collection<String> a = Arrays.asList("eyes", "feet");
	private static final DynamicCommandExceptionType b =
    new DynamicCommandExceptionType(o -> new ChatMessage("argument.anchor.invalid", o));

	public ArgumentAnchor() {}

	public static ArgumentAnchor.Anchor a(CommandContext<CommandListenerWrapper> commandcontext, String s) {
		return commandcontext.getArgument(s, Anchor.class);
	}

	public static ArgumentAnchor a() {
		return new ArgumentAnchor();
	}

	public ArgumentAnchor.Anchor parse(StringReader stringreader) throws CommandSyntaxException {
		int i = stringreader.getCursor();
		String s = stringreader.readUnquotedString();
		ArgumentAnchor.Anchor argumentanchor_anchor = ArgumentAnchor.Anchor.a(s);

		if (argumentanchor_anchor == null) {
			stringreader.setCursor(i);
			throw ArgumentAnchor.b.createWithContext(stringreader, s);
		} else {
			return argumentanchor_anchor;
		}
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandcontext, SuggestionsBuilder suggestionsbuilder) {
		return ICompletionProvider.b(Anchor.c.keySet(), suggestionsbuilder);
	}

	public Collection<String> getExamples() {
		return ArgumentAnchor.a;
	}

	public enum Anchor {
		FEET("feet", (vec3d, entity) -> vec3d),
		EYES("eyes", (vec3d, entity) -> new Vec3D(vec3d.x, vec3d.y + (double) entity.getHeadHeight(), vec3d.z)),
		;

		public static Anchor @Unmodifiable [] VALUES = values();

		private static final Map<String, ArgumentAnchor.Anchor> c = SystemUtils.a(Maps.newHashMap(), map -> {
			var i = VALUES.length;

			for (var j = 0; j < i; ++j) {
				var anchor = VALUES[j];
				map.put(anchor.d, anchor);
			}
		});
		private final String d;
		private final BiFunction<Vec3D, Entity, Vec3D> e;

		Anchor(String s, BiFunction<Vec3D, Entity, Vec3D> bifunction) {
			this.d = s;
			this.e = bifunction;
		}

		@Nullable
		public static ArgumentAnchor.Anchor a(String s) {
			return Anchor.c.get(s);
		}

		public Vec3D a(Entity entity) {
			return this.e.apply(entity.getPositionVector(), entity);
		}

		public Vec3D a(CommandListenerWrapper commandlistenerwrapper) {
			Entity entity = commandlistenerwrapper.getEntity();

			return entity == null
				? commandlistenerwrapper.getPosition()
				: this.e.apply(commandlistenerwrapper.getPosition(), entity);
		}
	}
}

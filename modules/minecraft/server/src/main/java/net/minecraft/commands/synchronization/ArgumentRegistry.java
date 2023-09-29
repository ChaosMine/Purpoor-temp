package net.minecraft.commands.synchronization;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.SharedConstants;
import net.minecraft.commands.arguments.ArgumentAnchor;
import net.minecraft.commands.arguments.ArgumentAngle;
import net.minecraft.commands.arguments.ArgumentChat;
import net.minecraft.commands.arguments.ArgumentChatComponent;
import net.minecraft.commands.arguments.ArgumentChatFormat;
import net.minecraft.commands.arguments.ArgumentCriterionValue;
import net.minecraft.commands.arguments.ArgumentDimension;
import net.minecraft.commands.arguments.ArgumentEnchantment;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.commands.arguments.ArgumentEntitySummon;
import net.minecraft.commands.arguments.ArgumentInventorySlot;
import net.minecraft.commands.arguments.ArgumentMathOperation;
import net.minecraft.commands.arguments.ArgumentMinecraftKeyRegistered;
import net.minecraft.commands.arguments.ArgumentMobEffect;
import net.minecraft.commands.arguments.ArgumentNBTBase;
import net.minecraft.commands.arguments.ArgumentNBTKey;
import net.minecraft.commands.arguments.ArgumentNBTTag;
import net.minecraft.commands.arguments.ArgumentParticle;
import net.minecraft.commands.arguments.ArgumentProfile;
import net.minecraft.commands.arguments.ArgumentScoreboardCriteria;
import net.minecraft.commands.arguments.ArgumentScoreboardObjective;
import net.minecraft.commands.arguments.ArgumentScoreboardSlot;
import net.minecraft.commands.arguments.ArgumentScoreboardTeam;
import net.minecraft.commands.arguments.ArgumentScoreholder;
import net.minecraft.commands.arguments.ArgumentTime;
import net.minecraft.commands.arguments.ArgumentUUID;
import net.minecraft.commands.arguments.blocks.ArgumentBlockPredicate;
import net.minecraft.commands.arguments.blocks.ArgumentTile;
import net.minecraft.commands.arguments.coordinates.ArgumentPosition;
import net.minecraft.commands.arguments.coordinates.ArgumentRotation;
import net.minecraft.commands.arguments.coordinates.ArgumentRotationAxis;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2I;
import net.minecraft.commands.arguments.coordinates.ArgumentVec3;
import net.minecraft.commands.arguments.item.ArgumentItemPredicate;
import net.minecraft.commands.arguments.item.ArgumentItemStack;
import net.minecraft.commands.arguments.item.ArgumentTag;
import net.minecraft.commands.synchronization.brigadier.ArgumentSerializers;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.resources.MinecraftKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArgumentRegistry {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Map<Class<?>, ArgumentRegistry.a<?>> b = Maps.newHashMap();
	private static final Map<MinecraftKey, ArgumentRegistry.a<?>> c = Maps.newHashMap();

	public static <T extends ArgumentType<?>> void a(String s, Class<T> oclass, ArgumentSerializer<T> argumentserializer) {
		MinecraftKey minecraftkey = new MinecraftKey(s);

		if (ArgumentRegistry.b.containsKey(oclass)) {
			throw new IllegalArgumentException("Class " + oclass.getName() + " already has a serializer!");
		} else if (ArgumentRegistry.c.containsKey(minecraftkey)) {
			throw new IllegalArgumentException("'" + minecraftkey + "' is already a registered serializer!");
		} else {
			ArgumentRegistry.a<T> argumentregistry_a = new ArgumentRegistry.a<>(oclass, argumentserializer, minecraftkey);

			ArgumentRegistry.b.put(oclass, argumentregistry_a);
			ArgumentRegistry.c.put(minecraftkey, argumentregistry_a);
		}
	}

	public static void a() {
		ArgumentSerializers.a();
		a("entity", ArgumentEntity.class, new ArgumentEntity.a());
		a("game_profile", ArgumentProfile.class, new ArgumentSerializerVoid<>(ArgumentProfile::a));
		a("block_pos", ArgumentPosition.class, new ArgumentSerializerVoid<>(ArgumentPosition::a));
		a("column_pos", ArgumentVec2I.class, new ArgumentSerializerVoid<>(ArgumentVec2I::a));
		a("vec3", ArgumentVec3.class, new ArgumentSerializerVoid<>(ArgumentVec3::a));
		a("vec2", ArgumentVec2.class, new ArgumentSerializerVoid<>(ArgumentVec2::a));
		a("block_state", ArgumentTile.class, new ArgumentSerializerVoid<>(ArgumentTile::a));
		a("block_predicate", ArgumentBlockPredicate.class, new ArgumentSerializerVoid<>(ArgumentBlockPredicate::a));
		a("item_stack", ArgumentItemStack.class, new ArgumentSerializerVoid<>(ArgumentItemStack::a));
		a("item_predicate", ArgumentItemPredicate.class, new ArgumentSerializerVoid<>(ArgumentItemPredicate::a));
		a("color", ArgumentChatFormat.class, new ArgumentSerializerVoid<>(ArgumentChatFormat::a));
		a("component", ArgumentChatComponent.class, new ArgumentSerializerVoid<>(ArgumentChatComponent::a));
		a("message", ArgumentChat.class, new ArgumentSerializerVoid<>(ArgumentChat::a));
		a("nbt_compound_tag", ArgumentNBTTag.class, new ArgumentSerializerVoid<>(ArgumentNBTTag::a));
		a("nbt_tag", ArgumentNBTBase.class, new ArgumentSerializerVoid<>(ArgumentNBTBase::a));
		a("nbt_path", ArgumentNBTKey.class, new ArgumentSerializerVoid<>(ArgumentNBTKey::a));
		a("objective", ArgumentScoreboardObjective.class, new ArgumentSerializerVoid<>(ArgumentScoreboardObjective::a));
		a("objective_criteria", ArgumentScoreboardCriteria.class, new ArgumentSerializerVoid<>(ArgumentScoreboardCriteria::a));
		a("operation", ArgumentMathOperation.class, new ArgumentSerializerVoid<>(ArgumentMathOperation::a));
		a("particle", ArgumentParticle.class, new ArgumentSerializerVoid<>(ArgumentParticle::a));
		a("angle", ArgumentAngle.class, new ArgumentSerializerVoid<>(ArgumentAngle::a));
		a("rotation", ArgumentRotation.class, new ArgumentSerializerVoid<>(ArgumentRotation::a));
		a("scoreboard_slot", ArgumentScoreboardSlot.class, new ArgumentSerializerVoid<>(ArgumentScoreboardSlot::a));
		a("score_holder", ArgumentScoreholder.class, new ArgumentScoreholder.c());
		a("swizzle", ArgumentRotationAxis.class, new ArgumentSerializerVoid<>(ArgumentRotationAxis::a));
		a("team", ArgumentScoreboardTeam.class, new ArgumentSerializerVoid<>(ArgumentScoreboardTeam::a));
		a("item_slot", ArgumentInventorySlot.class, new ArgumentSerializerVoid<>(ArgumentInventorySlot::a));
		a("resource_location", ArgumentMinecraftKeyRegistered.class, new ArgumentSerializerVoid<>(ArgumentMinecraftKeyRegistered::a));
		a("mob_effect", ArgumentMobEffect.class, new ArgumentSerializerVoid<>(ArgumentMobEffect::a));
		a("function", ArgumentTag.class, new ArgumentSerializerVoid<>(ArgumentTag::a));
		a("entity_anchor", ArgumentAnchor.class, new ArgumentSerializerVoid<>(ArgumentAnchor::a));
		a("int_range", ArgumentCriterionValue.b.class, new ArgumentSerializerVoid<>(ArgumentCriterionValue::a));
		a("float_range", ArgumentCriterionValue.a.class, new ArgumentSerializerVoid<>(ArgumentCriterionValue::b));
		a("item_enchantment", ArgumentEnchantment.class, new ArgumentSerializerVoid<>(ArgumentEnchantment::a));
		a("entity_summon", ArgumentEntitySummon.class, new ArgumentSerializerVoid<>(ArgumentEntitySummon::a));
		a("dimension", ArgumentDimension.class, new ArgumentSerializerVoid<>(ArgumentDimension::a));
		a("time", ArgumentTime.class, new ArgumentSerializerVoid<>(ArgumentTime::a));
		a("uuid", ArgumentUUID.class, new ArgumentSerializerVoid<>(ArgumentUUID::a));
	}

	@Nullable
	private static ArgumentRegistry.a<?> a(MinecraftKey minecraftkey) {
		return ArgumentRegistry.c.get(minecraftkey);
	}

	@Nullable
	private static ArgumentRegistry.a<?> b(ArgumentType<?> argumenttype) {
		return ArgumentRegistry.b.get(argumenttype.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <T extends ArgumentType<?>> void a(PacketDataSerializer packetdataserializer, T t0) {
		var argumentregistry_a = (a<T>) b(t0);

		if (argumentregistry_a == null) {
			ArgumentRegistry.LOGGER.error("Could not serialize {} ({}) - will not be sent to client!", t0, t0.getClass());
			packetdataserializer.a(new MinecraftKey(""));
		} else {
			packetdataserializer.a(argumentregistry_a.c);
			argumentregistry_a.b.a(t0, packetdataserializer);
		}
	}

	@Nullable
	public static ArgumentType<?> a(PacketDataSerializer packetdataserializer) {
		MinecraftKey minecraftkey = packetdataserializer.p();
		ArgumentRegistry.a<?> argumentregistry_a = a(minecraftkey);

		if (argumentregistry_a == null) {
			ArgumentRegistry.LOGGER.error("Could not deserialize {}", minecraftkey);
			return null;
		} else {
			return argumentregistry_a.b.b(packetdataserializer);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T extends ArgumentType<?>> void a(JsonObject jsonobject, T t0) {
		var argumentregistry_a = (a<T>) b(t0);

		if (argumentregistry_a == null) {
			ArgumentRegistry.LOGGER.error("Could not serialize argument {} ({})!", t0, t0.getClass());
			jsonobject.addProperty("type", "unknown");
		} else {
			jsonobject.addProperty("type", "argument");
			jsonobject.addProperty("parser", argumentregistry_a.c.toString());
			JsonObject jsonobject1 = new JsonObject();

			argumentregistry_a.b.a(t0, jsonobject1);
			if (jsonobject1.size() > 0) {
				jsonobject.add("properties", jsonobject1);
			}
		}
	}

	public static <S> JsonObject a(CommandDispatcher<S> commanddispatcher, CommandNode<S> commandnode) {
		JsonObject jsonobject = new JsonObject();

		if (commandnode instanceof RootCommandNode) {
			jsonobject.addProperty("type", "root");
		} else if (commandnode instanceof LiteralCommandNode) {
			jsonobject.addProperty("type", "literal");
		} else if (commandnode instanceof ArgumentCommandNode) {
			a(jsonobject, ((ArgumentCommandNode) commandnode).getType());
		} else {
			ArgumentRegistry.LOGGER.error("Could not serialize node {} ({})!", commandnode, commandnode.getClass());
			jsonobject.addProperty("type", "unknown");
		}

		JsonObject jsonobject1 = new JsonObject();

		for (CommandNode<S> sCommandNode : commandnode.getChildren()) {
			CommandNode<S> commandnode1 = sCommandNode;

			jsonobject1.add(commandnode1.getName(), a(commanddispatcher, commandnode1));
		}

		if (jsonobject1.size() > 0) {
			jsonobject.add("children", jsonobject1);
		}

		if (commandnode.getCommand() != null) {
			jsonobject.addProperty("executable", true);
		}

		if (commandnode.getRedirect() != null) {
			Collection<String> collection = commanddispatcher.getPath(commandnode.getRedirect());

			if (!collection.isEmpty()) {
				JsonArray jsonarray = new JsonArray();
				Iterator iterator1 = collection.iterator();

				while (iterator1.hasNext()) {
					String s = (String) iterator1.next();

					jsonarray.add(s);
				}

				jsonobject.add("redirect", jsonarray);
			}
		}

		return jsonobject;
	}

	public static boolean a(ArgumentType<?> argumenttype) {
		return b(argumenttype) != null;
	}

	public static <T> Set<ArgumentType<?>> a(CommandNode<T> commandnode) {
		Set<CommandNode<T>> set = Sets.newIdentityHashSet();
		Set<ArgumentType<?>> set1 = Sets.newHashSet();

		a(commandnode, set1, set);
		return set1;
	}

	private static <T> void a(CommandNode<T> commandnode, Set<ArgumentType<?>> set, Set<CommandNode<T>> set1) {
		if (set1.add(commandnode)) {
			if (commandnode instanceof ArgumentCommandNode) {
				set.add(((ArgumentCommandNode) commandnode).getType());
			}

			commandnode.getChildren().forEach((commandnode1) -> {
				a(commandnode1, set, set1);
			});
			CommandNode<T> commandnode1 = commandnode.getRedirect();

			if (commandnode1 != null) {
				a(commandnode1, set, set1);
			}
		}
	}

	static class a<T extends ArgumentType<?>> {

		public final Class<T> a;
		public final ArgumentSerializer<T> b;
		public final MinecraftKey c;

		private a(Class<T> oclass, ArgumentSerializer<T> argumentserializer, MinecraftKey minecraftkey) {
			this.a = oclass;
			this.b = argumentserializer;
			this.c = minecraftkey;
		}
	}
}

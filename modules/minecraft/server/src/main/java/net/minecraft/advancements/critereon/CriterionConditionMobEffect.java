package net.minecraft.advancements.critereon;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;

public class CriterionConditionMobEffect {

    public static final CriterionConditionMobEffect a = new CriterionConditionMobEffect(Collections.emptyMap());
    private final Map<MobEffectList, a_> b;

    public CriterionConditionMobEffect(Map<MobEffectList, a_> map) {
        this.b = map;
    }

    public static CriterionConditionMobEffect a() {
        return new CriterionConditionMobEffect(Maps.newLinkedHashMap());
    }

    public CriterionConditionMobEffect a(MobEffectList mobeffectlist) {
        this.b.put(mobeffectlist, new a_());
        return this;
    }

    public boolean a(Entity entity) {
        return this == CriterionConditionMobEffect.a ? true : (entity instanceof EntityLiving ? this.a(((EntityLiving) entity).di()) : false);
    }

    public boolean a(EntityLiving entityliving) {
        return this == CriterionConditionMobEffect.a ? true : this.a(entityliving.di());
    }

    public boolean a(Map<MobEffectList, MobEffect> map) {
        if (this == CriterionConditionMobEffect.a) {
            return true;
        } else {
            Iterator iterator = this.b.entrySet().iterator();

            Entry entry;
            MobEffect mobeffect;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                entry = (Entry) iterator.next();
                mobeffect = map.get(entry.getKey());
            } while (((a_) entry.getValue()).a(mobeffect));

            return false;
        }
    }

    public static CriterionConditionMobEffect a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = ChatDeserializer.m(jsonelement, "effects");
            Map<MobEffectList, a_> map = Maps.newLinkedHashMap();
            Iterator iterator = jsonobject.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<String, JsonElement> entry = (Entry) iterator.next();
                MinecraftKey minecraftkey = new MinecraftKey(entry.getKey());
                MobEffectList mobeffectlist = IRegistry.MOB_EFFECT.getOptional(minecraftkey).orElseThrow(() -> {
                    return new JsonSyntaxException("Unknown effect '" + minecraftkey + "'");
                });
                a_ criterionconditionmobeffect_a = CriterionConditionMobEffect.a_.a(ChatDeserializer.m(entry.getValue(), entry.getKey()));

                map.put(mobeffectlist, criterionconditionmobeffect_a);
            }

            return new CriterionConditionMobEffect(map);
        } else {
            return CriterionConditionMobEffect.a;
        }
    }

    public JsonElement b() {
        if (this == CriterionConditionMobEffect.a) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject jsonobject = new JsonObject();

					for (var entry : this.b.entrySet()) {
						jsonobject.add(IRegistry.MOB_EFFECT.getKey(entry.getKey()).toString(), entry.getValue().a());
					}

            return jsonobject;
        }
    }

    public static class a_ {

        private final CriterionConditionValue.IntegerRange a;
        private final CriterionConditionValue.IntegerRange b;
        @Nullable
        private final Boolean c;
        @Nullable
        private final Boolean d;

        public a_(CriterionConditionValue.IntegerRange criterionconditionvalue_integerrange, CriterionConditionValue.IntegerRange criterionconditionvalue_integerrange1, @Nullable Boolean obool, @Nullable Boolean obool1) {
            this.a = criterionconditionvalue_integerrange;
            this.b = criterionconditionvalue_integerrange1;
            this.c = obool;
            this.d = obool1;
        }

        public a_() {
            this(CriterionConditionValue.IntegerRange.e, CriterionConditionValue.IntegerRange.e, null, null);
        }

        public boolean a(@Nullable MobEffect mobeffect) {
            return mobeffect == null ? false : (!this.a.d(mobeffect.getAmplifier()) ? false : (!this.b.d(mobeffect.getDuration()) ? false : (this.c != null && this.c != mobeffect.isAmbient() ? false : this.d == null || this.d == mobeffect.isShowParticles())));
        }

        public JsonElement a() {
            JsonObject jsonobject = new JsonObject();

            jsonobject.add("amplifier", this.a.d());
            jsonobject.add("duration", this.b.d());
            jsonobject.addProperty("ambient", this.c);
            jsonobject.addProperty("visible", this.d);
            return jsonobject;
        }

        public static a_ a(JsonObject jsonobject) {
            CriterionConditionValue.IntegerRange criterionconditionvalue_integerrange = CriterionConditionValue.IntegerRange.a(jsonobject.get("amplifier"));
            CriterionConditionValue.IntegerRange criterionconditionvalue_integerrange1 = CriterionConditionValue.IntegerRange.a(jsonobject.get("duration"));
            Boolean obool = jsonobject.has("ambient") ? ChatDeserializer.j(jsonobject, "ambient") : null;
            Boolean obool1 = jsonobject.has("visible") ? ChatDeserializer.j(jsonobject, "visible") : null;

            return new a_(criterionconditionvalue_integerrange, criterionconditionvalue_integerrange1, obool, obool1);
        }
    }
}

package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.tags.Tag;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameterSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameters;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionEntityProperty;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.scores.ScoreboardTeamBase;

public class CriterionConditionEntity {

    public static final CriterionConditionEntity a = new CriterionConditionEntity(CriterionConditionEntityType.a, CriterionConditionDistance.a, CriterionConditionLocation.a, CriterionConditionMobEffect.a, CriterionConditionNBT.a, CriterionConditionEntityFlags.a, CriterionConditionEntityEquipment.a, CriterionConditionPlayer.a, CriterionConditionInOpenWater.a, null, null);
    private final CriterionConditionEntityType b;
    private final CriterionConditionDistance c;
    private final CriterionConditionLocation d;
    private final CriterionConditionMobEffect e;
    private final CriterionConditionNBT f;
    private final CriterionConditionEntityFlags g;
    private final CriterionConditionEntityEquipment h;
    private final CriterionConditionPlayer i;
    private final CriterionConditionInOpenWater j;
    private final CriterionConditionEntity k;
    private final CriterionConditionEntity l;
    @Nullable
    private final String m;
    @Nullable
    private final MinecraftKey n;

    private CriterionConditionEntity(CriterionConditionEntityType criterionconditionentitytype, CriterionConditionDistance criterionconditiondistance, CriterionConditionLocation criterionconditionlocation, CriterionConditionMobEffect criterionconditionmobeffect, CriterionConditionNBT criterionconditionnbt, CriterionConditionEntityFlags criterionconditionentityflags, CriterionConditionEntityEquipment criterionconditionentityequipment, CriterionConditionPlayer criterionconditionplayer, CriterionConditionInOpenWater criterionconditioninopenwater, @Nullable String s, @Nullable MinecraftKey minecraftkey) {
        this.b = criterionconditionentitytype;
        this.c = criterionconditiondistance;
        this.d = criterionconditionlocation;
        this.e = criterionconditionmobeffect;
        this.f = criterionconditionnbt;
        this.g = criterionconditionentityflags;
        this.h = criterionconditionentityequipment;
        this.i = criterionconditionplayer;
        this.j = criterionconditioninopenwater;
        this.k = this;
        this.l = this;
        this.m = s;
        this.n = minecraftkey;
    }

    private CriterionConditionEntity(CriterionConditionEntityType criterionconditionentitytype, CriterionConditionDistance criterionconditiondistance, CriterionConditionLocation criterionconditionlocation, CriterionConditionMobEffect criterionconditionmobeffect, CriterionConditionNBT criterionconditionnbt, CriterionConditionEntityFlags criterionconditionentityflags, CriterionConditionEntityEquipment criterionconditionentityequipment, CriterionConditionPlayer criterionconditionplayer, CriterionConditionInOpenWater criterionconditioninopenwater, CriterionConditionEntity criterionconditionentity, CriterionConditionEntity criterionconditionentity1, @Nullable String s, @Nullable MinecraftKey minecraftkey) {
        this.b = criterionconditionentitytype;
        this.c = criterionconditiondistance;
        this.d = criterionconditionlocation;
        this.e = criterionconditionmobeffect;
        this.f = criterionconditionnbt;
        this.g = criterionconditionentityflags;
        this.h = criterionconditionentityequipment;
        this.i = criterionconditionplayer;
        this.j = criterionconditioninopenwater;
        this.k = criterionconditionentity;
        this.l = criterionconditionentity1;
        this.m = s;
        this.n = minecraftkey;
    }

    public boolean a(EntityPlayer entityplayer, @Nullable Entity entity) {
        return this.a(entityplayer.getWorldServer(), entityplayer.getPositionVector(), entity);
    }

    public boolean a(WorldServer worldserver, @Nullable Vec3D vec3d, @Nullable Entity entity) {
        if (this == CriterionConditionEntity.a) {
            return true;
        } else if (entity == null) {
            return false;
        } else if (!this.b.a(entity.getEntityType())) {
            return false;
        } else {
            if (vec3d == null) {
                if (this.c != CriterionConditionDistance.a) {
                    return false;
                }
            } else if (!this.c.a(vec3d.x, vec3d.y, vec3d.z, entity.locX(), entity.locY(), entity.locZ())) {
                return false;
            }

            if (!this.d.a(worldserver, entity.locX(), entity.locY(), entity.locZ())) {
                return false;
            } else if (!this.e.a(entity)) {
                return false;
            } else if (!this.f.a(entity)) {
                return false;
            } else if (!this.g.a(entity)) {
                return false;
            } else if (!this.h.a(entity)) {
                return false;
            } else if (!this.i.a(entity)) {
                return false;
            } else if (!this.j.a(entity)) {
                return false;
            } else if (!this.k.a(worldserver, vec3d, entity.getVehicle())) {
                return false;
            } else if (!this.l.a(worldserver, vec3d, entity instanceof EntityInsentient ? ((EntityInsentient) entity).getGoalTarget() : null)) {
                return false;
            } else {
                if (this.m != null) {
                    ScoreboardTeamBase scoreboardteambase = entity.getScoreboardTeam();

                    if (scoreboardteambase == null || !this.m.equals(scoreboardteambase.getName())) {
                        return false;
                    }
                }

                return this.n == null || entity instanceof EntityCat && ((EntityCat) entity).eU().equals(this.n);
            }
        }
    }

    public static CriterionConditionEntity a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = ChatDeserializer.m(jsonelement, "entity");
            CriterionConditionEntityType criterionconditionentitytype = CriterionConditionEntityType.a(jsonobject.get("type"));
            CriterionConditionDistance criterionconditiondistance = CriterionConditionDistance.a(jsonobject.get("distance"));
            CriterionConditionLocation criterionconditionlocation = CriterionConditionLocation.a(jsonobject.get("location"));
            CriterionConditionMobEffect criterionconditionmobeffect = CriterionConditionMobEffect.a(jsonobject.get("effects"));
            CriterionConditionNBT criterionconditionnbt = CriterionConditionNBT.a(jsonobject.get("nbt"));
            CriterionConditionEntityFlags criterionconditionentityflags = CriterionConditionEntityFlags.a(jsonobject.get("flags"));
            CriterionConditionEntityEquipment criterionconditionentityequipment = CriterionConditionEntityEquipment.a(jsonobject.get("equipment"));
            CriterionConditionPlayer criterionconditionplayer = CriterionConditionPlayer.a(jsonobject.get("player"));
            CriterionConditionInOpenWater criterionconditioninopenwater = CriterionConditionInOpenWater.a(jsonobject.get("fishing_hook"));
            CriterionConditionEntity criterionconditionentity = a(jsonobject.get("vehicle"));
            CriterionConditionEntity criterionconditionentity1 = a(jsonobject.get("targeted_entity"));
            String s = ChatDeserializer.a(jsonobject, "team", (String) null);
            MinecraftKey minecraftkey = jsonobject.has("catType") ? new MinecraftKey(ChatDeserializer.h(jsonobject, "catType")) : null;

            return (new a_()).a(criterionconditionentitytype).a(criterionconditiondistance).a(criterionconditionlocation).a(criterionconditionmobeffect).a(criterionconditionnbt).a(criterionconditionentityflags).a(criterionconditionentityequipment).a(criterionconditionplayer).a(criterionconditioninopenwater).a(s).a(criterionconditionentity).b(criterionconditionentity1).b(minecraftkey).b();
        } else {
            return CriterionConditionEntity.a;
        }
    }

    public JsonElement a() {
        if (this == CriterionConditionEntity.a) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject jsonobject = new JsonObject();

            jsonobject.add("type", this.b.a());
            jsonobject.add("distance", this.c.a());
            jsonobject.add("location", this.d.a());
            jsonobject.add("effects", this.e.b());
            jsonobject.add("nbt", this.f.a());
            jsonobject.add("flags", this.g.a());
            jsonobject.add("equipment", this.h.a());
            jsonobject.add("player", this.i.a());
            jsonobject.add("fishing_hook", this.j.a());
            jsonobject.add("vehicle", this.k.a());
            jsonobject.add("targeted_entity", this.l.a());
            jsonobject.addProperty("team", this.m);
            if (this.n != null) {
                jsonobject.addProperty("catType", this.n.toString());
            }

            return jsonobject;
        }
    }

    public static LootTableInfo b(EntityPlayer entityplayer, Entity entity) {
        return (new LootTableInfo.Builder(entityplayer.getWorldServer())).set(LootContextParameters.THIS_ENTITY, entity).set(LootContextParameters.ORIGIN, entityplayer.getPositionVector()).a(entityplayer.getRandom()).build(LootContextParameterSets.j);
    }

    public static class b_ {

        public static final b_ a = new b_(new LootItemCondition[0]);
        private final LootItemCondition[] b;
        private final Predicate<LootTableInfo> c;

        private b_(LootItemCondition[] alootitemcondition) {
            this.b = alootitemcondition;
            this.c = LootItemConditions.a((Predicate[]) alootitemcondition);
        }

        public static b_ a(LootItemCondition... alootitemcondition) {
            return new b_(alootitemcondition);
        }

        public static b_ a(JsonObject jsonobject, String s, LootDeserializationContext lootdeserializationcontext) {
            JsonElement jsonelement = jsonobject.get(s);

            return a(s, lootdeserializationcontext, jsonelement);
        }

        public static b_[] b(JsonObject jsonobject, String s, LootDeserializationContext lootdeserializationcontext) {
            JsonElement jsonelement = jsonobject.get(s);

            if (jsonelement != null && !jsonelement.isJsonNull()) {
                JsonArray jsonarray = ChatDeserializer.n(jsonelement, s);
                b_[] acriterionconditionentity_b = new b_[jsonarray.size()];

                for (int i = 0; i < jsonarray.size(); ++i) {
                    acriterionconditionentity_b[i] = a(s + "[" + i + "]", lootdeserializationcontext, jsonarray.get(i));
                }

                return acriterionconditionentity_b;
            } else {
                return new b_[0];
            }
        }

        private static b_ a(String s, LootDeserializationContext lootdeserializationcontext, @Nullable JsonElement jsonelement) {
            if (jsonelement != null && jsonelement.isJsonArray()) {
                LootItemCondition[] alootitemcondition = lootdeserializationcontext.a(jsonelement.getAsJsonArray(), lootdeserializationcontext.a().toString() + "/" + s, LootContextParameterSets.j);

                return new b_(alootitemcondition);
            } else {
                CriterionConditionEntity criterionconditionentity = CriterionConditionEntity.a(jsonelement);

                return a(criterionconditionentity);
            }
        }

        public static b_ a(CriterionConditionEntity criterionconditionentity) {
            if (criterionconditionentity == CriterionConditionEntity.a) {
                return CriterionConditionEntity.b_.a;
            } else {
                LootItemCondition lootitemcondition = LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, criterionconditionentity).build();

                return new b_(new LootItemCondition[]{lootitemcondition});
            }
        }

        public boolean a(LootTableInfo loottableinfo) {
            return this.c.test(loottableinfo);
        }

        public JsonElement a(LootSerializationContext lootserializationcontext) {
            return this.b.length == 0 ? JsonNull.INSTANCE : lootserializationcontext.a(this.b);
        }

        public static JsonElement a(b_[] acriterionconditionentity_b, LootSerializationContext lootserializationcontext) {
            if (acriterionconditionentity_b.length == 0) {
                return JsonNull.INSTANCE;
            } else {
                JsonArray jsonarray = new JsonArray();
                b_[] acriterionconditionentity_b1 = acriterionconditionentity_b;
                int i = acriterionconditionentity_b.length;

                for (int j = 0; j < i; ++j) {
                    b_ criterionconditionentity_b = acriterionconditionentity_b1[j];

                    jsonarray.add(criterionconditionentity_b.a(lootserializationcontext));
                }

                return jsonarray;
            }
        }
    }

    public static class a_ {

        private CriterionConditionEntityType a;
        private CriterionConditionDistance b;
        private CriterionConditionLocation c;
        private CriterionConditionMobEffect d;
        private CriterionConditionNBT e;
        private CriterionConditionEntityFlags f;
        private CriterionConditionEntityEquipment g;
        private CriterionConditionPlayer h;
        private CriterionConditionInOpenWater i;
        private CriterionConditionEntity j;
        private CriterionConditionEntity k;
        private String l;
        private MinecraftKey m;

        public a_() {
            this.a = CriterionConditionEntityType.a;
            this.b = CriterionConditionDistance.a;
            this.c = CriterionConditionLocation.a;
            this.d = CriterionConditionMobEffect.a;
            this.e = CriterionConditionNBT.a;
            this.f = CriterionConditionEntityFlags.a;
            this.g = CriterionConditionEntityEquipment.a;
            this.h = CriterionConditionPlayer.a;
            this.i = CriterionConditionInOpenWater.a;
            this.j = CriterionConditionEntity.a;
            this.k = CriterionConditionEntity.a;
        }

        public static a_ a() {
            return new a_();
        }

        public a_ a(EntityTypes<?> entitytypes) {
            this.a = CriterionConditionEntityType.b(entitytypes);
            return this;
        }

        public a_ a(Tag<EntityTypes<?>> tag) {
            this.a = CriterionConditionEntityType.a(tag);
            return this;
        }

        public a_ a(MinecraftKey minecraftkey) {
            this.m = minecraftkey;
            return this;
        }

        public a_ a(CriterionConditionEntityType criterionconditionentitytype) {
            this.a = criterionconditionentitytype;
            return this;
        }

        public a_ a(CriterionConditionDistance criterionconditiondistance) {
            this.b = criterionconditiondistance;
            return this;
        }

        public a_ a(CriterionConditionLocation criterionconditionlocation) {
            this.c = criterionconditionlocation;
            return this;
        }

        public a_ a(CriterionConditionMobEffect criterionconditionmobeffect) {
            this.d = criterionconditionmobeffect;
            return this;
        }

        public a_ a(CriterionConditionNBT criterionconditionnbt) {
            this.e = criterionconditionnbt;
            return this;
        }

        public a_ a(CriterionConditionEntityFlags criterionconditionentityflags) {
            this.f = criterionconditionentityflags;
            return this;
        }

        public a_ a(CriterionConditionEntityEquipment criterionconditionentityequipment) {
            this.g = criterionconditionentityequipment;
            return this;
        }

        public a_ a(CriterionConditionPlayer criterionconditionplayer) {
            this.h = criterionconditionplayer;
            return this;
        }

        public a_ a(CriterionConditionInOpenWater criterionconditioninopenwater) {
            this.i = criterionconditioninopenwater;
            return this;
        }

        public a_ a(CriterionConditionEntity criterionconditionentity) {
            this.j = criterionconditionentity;
            return this;
        }

        public a_ b(CriterionConditionEntity criterionconditionentity) {
            this.k = criterionconditionentity;
            return this;
        }

        public a_ a(@Nullable String s) {
            this.l = s;
            return this;
        }

        public a_ b(@Nullable MinecraftKey minecraftkey) {
            this.m = minecraftkey;
            return this;
        }

        public CriterionConditionEntity b() {
            return new CriterionConditionEntity(this.a, this.b, this.c, this.d, this.e, this.f, this.g, this.h, this.i, this.j, this.k, this.l, this.m);
        }
    }
}

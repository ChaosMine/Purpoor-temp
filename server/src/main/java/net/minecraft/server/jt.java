package net.minecraft.server;

import java.nio.file.Path;
import net.minecraft.core.IRegistry;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.TagsEntity;
import net.minecraft.world.entity.EntityTypes;

public class jt extends jw<EntityTypes<?>> {

    public jt(DebugReportGenerator debugreportgenerator) {
        super(debugreportgenerator, IRegistry.ENTITY_TYPE);
    }

    @Override
    protected void b() {
        this.a(TagsEntity.SKELETONS).a((Object[])(EntityTypes.SKELETON, EntityTypes.STRAY, EntityTypes.WITHER_SKELETON));
        this.a(TagsEntity.RADIERS).a((Object[])(EntityTypes.EVOKER, EntityTypes.PILLAGER, EntityTypes.RAVAGER, EntityTypes.VINDICATOR, EntityTypes.ILLUSIONER, EntityTypes.WITCH));
        this.a(TagsEntity.BEEHIVE_INHABITORS).a((Object)EntityTypes.BEE);
        this.a(TagsEntity.ARROWS).a((Object[])(EntityTypes.ARROW, EntityTypes.SPECTRAL_ARROW));
        this.a(TagsEntity.IMPACT_PROJECTILES).a(TagsEntity.ARROWS).a((Object[])(EntityTypes.SNOWBALL, EntityTypes.FIREBALL, EntityTypes.SMALL_FIREBALL, EntityTypes.EGG, EntityTypes.TRIDENT, EntityTypes.DRAGON_FIREBALL, EntityTypes.WITHER_SKULL));
    }

    @Override
    protected Path a(MinecraftKey minecraftkey) {
        return this.b.b().resolve("data/" + minecraftkey.getNamespace() + "/tags/entity_types/" + minecraftkey.getKey() + ".json");
    }

    @Override
    public String a() {
        return "Entity Type Tags";
    }
}

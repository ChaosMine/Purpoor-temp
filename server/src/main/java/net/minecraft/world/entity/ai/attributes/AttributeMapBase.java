package net.minecraft.world.entity.ai.attributes;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.SystemUtils;
import net.minecraft.core.IRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.resources.MinecraftKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class AttributeMapBase {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<AttributeBase, AttributeModifiable> attributeMap = new Object2ObjectOpenHashMap<>();
    private final Set<AttributeModifiable> c = new ObjectOpenHashSet<>();
    private final AttributeProvider d;

    private AttributeModifiable MAX_HEALTH;
    private AttributeModifiable FOLLOW_RANGE;
    private AttributeModifiable KNOCKBACK_RESISTANCE;
    private AttributeModifiable MOVEMENT_SPEED;
    private AttributeModifiable FLYING_SPEED;
    private AttributeModifiable ATTACK_DAMAGE;
    private AttributeModifiable ATTACK_KNOCKBACK;
    private AttributeModifiable ATTACK_SPEED;
    private AttributeModifiable ARMOR;
    private AttributeModifiable ARMOR_TOUGHNESS;
    private AttributeModifiable LUCK;
    private AttributeModifiable SPAWN_REINFORCEMENTS;
    private AttributeModifiable JUMP_STRENGTH;

    private Collection<AttributeModifiable> isB = null;
    private NBTTagList nbtC = null;

    public AttributeMapBase(AttributeProvider attributeprovider) {
        this.d = attributeprovider;
    }

    private void a(AttributeModifiable attributemodifiable) {
        if (attributemodifiable.getAttribute().b()) {
            this.c.add(attributemodifiable);
        }
    }

    public Set<AttributeModifiable> getAttributes() {
        return this.c;
    }

    public Collection<AttributeModifiable> b() {
        if (isB == null) {
            return new ObjectArrayList<>(isB = this.attributeMap.values().stream().filter(a -> a.getAttribute().b()).toList());
        }
        return new ObjectArrayList<>(isB);
    }

    @Nullable
    public AttributeModifiable a(AttributeBase attributebase) {
        isB = null;
        nbtC = null;
        return this.attributeMap.computeIfAbsent(attributebase, (attributebase1) -> {
            return this.d.a(this::a, attributebase1);
        });
    }

    public boolean b(AttributeBase attributebase) {
        return this.attributeMap.get(attributebase) != null || this.d.c(attributebase);
    }

    public boolean a(AttributeBase attributebase, UUID uuid) {
        AttributeModifiable attributemodifiable = this.attributeMap.get(attributebase);

        return attributemodifiable != null ? attributemodifiable.a(uuid) != null : this.d.b(attributebase, uuid);
    }

    private AttributeModifiable get(AttributeBase attributebase) {
        AttributeModifiable attributemodifiable;
        if (attributebase == GenericAttributes.MAX_HEALTH) {
            attributemodifiable = MAX_HEALTH;
        } else if (attributebase == GenericAttributes.FOLLOW_RANGE) {
            attributemodifiable = FOLLOW_RANGE;
        } else if (attributebase == GenericAttributes.KNOCKBACK_RESISTANCE) {
            attributemodifiable = KNOCKBACK_RESISTANCE;
        } else if (attributebase == GenericAttributes.MOVEMENT_SPEED) {
            attributemodifiable = MOVEMENT_SPEED;
        } else if (attributebase == GenericAttributes.FLYING_SPEED) {
            attributemodifiable = FLYING_SPEED;
        } else if (attributebase == GenericAttributes.ATTACK_DAMAGE) {
            attributemodifiable = ATTACK_DAMAGE;
        } else if (attributebase == GenericAttributes.ATTACK_KNOCKBACK) {
            attributemodifiable = ATTACK_KNOCKBACK;
        } else if (attributebase == GenericAttributes.ATTACK_SPEED) {
            attributemodifiable = ATTACK_SPEED;
        } else if (attributebase == GenericAttributes.ARMOR) {
            attributemodifiable = ARMOR;
        } else if (attributebase == GenericAttributes.ARMOR_TOUGHNESS) {
            attributemodifiable = ARMOR_TOUGHNESS;
        } else if (attributebase == GenericAttributes.LUCK) {
            attributemodifiable = LUCK;
        } else if (attributebase == GenericAttributes.SPAWN_REINFORCEMENTS) {
            attributemodifiable = SPAWN_REINFORCEMENTS;
        } else if (attributebase == GenericAttributes.JUMP_STRENGTH) {
            attributemodifiable = JUMP_STRENGTH;
        } else {
            attributemodifiable = this.attributeMap.get(attributebase);
        }

        return attributemodifiable;
    }

    public double c(AttributeBase attributebase) {
        AttributeModifiable attributemodifiable = get(attributebase);
        return attributemodifiable != null ? attributemodifiable.getValue() : this.d.a(attributebase);
    }

    public double d(AttributeBase attributebase) {
        AttributeModifiable attributemodifiable = get(attributebase);
        return attributemodifiable != null ? attributemodifiable.getBaseValue() : this.d.b(attributebase);
    }

    public double b(AttributeBase attributebase, UUID uuid) {
        AttributeModifiable attributemodifiable = get(attributebase);
        return attributemodifiable != null ? attributemodifiable.a(uuid).getAmount() : this.d.a(attributebase, uuid);
    }

    public void a(Multimap<AttributeBase, AttributeModifier> multimap) {
        multimap.asMap().forEach((attributebase, collection) -> {
            var f = get(attributebase);
            AttributeModifiable attributemodifiable = this.attributeMap.get(attributebase);

            if (attributemodifiable != null) {
                collection.forEach(attributemodifiable::removeModifier);
            }

            if (f != null && f != attributemodifiable) {
                collection.forEach(f::removeModifier);
            }
        });
    }

    public void b(Multimap<AttributeBase, AttributeModifier> multimap) {
        multimap.forEach((attributebase, attributemodifier) -> {
            var f = get(attributebase);
            AttributeModifiable attributemodifiable = this.a(attributebase);

            if (attributemodifiable != null) {
                attributemodifiable.removeModifier(attributemodifier);
                attributemodifiable.b(attributemodifier);
            }

            if (f != null && f != attributemodifiable) {
                f.removeModifier(attributemodifier);
                f.b(attributemodifier);
            }
        });
    }

    public NBTTagList c() {
        if (nbtC == null) {
            NBTTagList nbttaglist = new NBTTagList();

            for (AttributeModifiable attributemodifiable : this.attributeMap.values()) {
                nbttaglist.add(attributemodifiable.g());
            }

            return (nbtC = nbttaglist);
        }
        return nbtC;
    }

    public void a(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
            String s = nbttagcompound.getString("Name");

            SystemUtils.a(IRegistry.ATTRIBUTE.getOptional(MinecraftKey.a(s)), (attributebase) -> {
                AttributeModifiable attributemodifiable = this.a(attributebase);
                if (attributemodifiable != null) {
                    attributemodifiable.a(nbttagcompound);
                }

                if (attributebase == GenericAttributes.MAX_HEALTH) {
                    MAX_HEALTH = attributemodifiable;
                } else if (attributebase == GenericAttributes.FOLLOW_RANGE) {
                    FOLLOW_RANGE = attributemodifiable;
                } else if (attributebase == GenericAttributes.KNOCKBACK_RESISTANCE) {
                    KNOCKBACK_RESISTANCE = attributemodifiable;
                } else if (attributebase == GenericAttributes.MOVEMENT_SPEED) {
                    MOVEMENT_SPEED = attributemodifiable;
                } else if (attributebase == GenericAttributes.FLYING_SPEED) {
                    FLYING_SPEED = attributemodifiable;
                } else if (attributebase == GenericAttributes.ATTACK_DAMAGE) {
                    ATTACK_DAMAGE = attributemodifiable;
                } else if (attributebase == GenericAttributes.ATTACK_KNOCKBACK) {
                    ATTACK_KNOCKBACK = attributemodifiable;
                } else if (attributebase == GenericAttributes.ATTACK_SPEED) {
                    ATTACK_SPEED = attributemodifiable;
                } else if (attributebase == GenericAttributes.ARMOR) {
                    ARMOR = attributemodifiable;
                } else if (attributebase == GenericAttributes.ARMOR_TOUGHNESS) {
                    ARMOR_TOUGHNESS = attributemodifiable;
                } else if (attributebase == GenericAttributes.LUCK) {
                    LUCK = attributemodifiable;
                } else if (attributebase == GenericAttributes.SPAWN_REINFORCEMENTS) {
                    SPAWN_REINFORCEMENTS = attributemodifiable;
                } else if (attributebase == GenericAttributes.JUMP_STRENGTH) {
                    JUMP_STRENGTH = attributemodifiable;
                }
            }, () -> AttributeMapBase.LOGGER.warn("Ignoring unknown attribute '{}'", s));
        }
    }

    // Paper - start
    public void registerAttribute(AttributeBase attributeBase) {
        isB = null;
        nbtC = null;
        AttributeModifiable attributeModifiable = new AttributeModifiable(attributeBase, AttributeModifiable::getAttribute);
        attributeMap.put(attributeBase, attributeModifiable);

        if (attributeBase == GenericAttributes.MAX_HEALTH) {
            MAX_HEALTH = attributeModifiable;
        } else if (attributeBase == GenericAttributes.FOLLOW_RANGE) {
            FOLLOW_RANGE = attributeModifiable;
        } else if (attributeBase == GenericAttributes.KNOCKBACK_RESISTANCE) {
            KNOCKBACK_RESISTANCE = attributeModifiable;
        } else if (attributeBase == GenericAttributes.MOVEMENT_SPEED) {
            MOVEMENT_SPEED = attributeModifiable;
        } else if (attributeBase == GenericAttributes.FLYING_SPEED) {
            FLYING_SPEED = attributeModifiable;
        } else if (attributeBase == GenericAttributes.ATTACK_DAMAGE) {
            ATTACK_DAMAGE = attributeModifiable;
        } else if (attributeBase == GenericAttributes.ATTACK_KNOCKBACK) {
            ATTACK_KNOCKBACK = attributeModifiable;
        } else if (attributeBase == GenericAttributes.ATTACK_SPEED) {
            ATTACK_SPEED = attributeModifiable;
        } else if (attributeBase == GenericAttributes.ARMOR) {
            ARMOR = attributeModifiable;
        } else if (attributeBase == GenericAttributes.ARMOR_TOUGHNESS) {
            ARMOR_TOUGHNESS = attributeModifiable;
        } else if (attributeBase == GenericAttributes.LUCK) {
            LUCK = attributeModifiable;
        } else if (attributeBase == GenericAttributes.SPAWN_REINFORCEMENTS) {
            SPAWN_REINFORCEMENTS = attributeModifiable;
        } else if (attributeBase == GenericAttributes.JUMP_STRENGTH) {
            JUMP_STRENGTH = attributeModifiable;
        }
    }
    // Paper - end
}

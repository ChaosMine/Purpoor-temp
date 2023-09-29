package net.minecraft.world.entity.ai.sensing;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;

public class EntitySenses {

    private final EntityInsentient a;
    private final ObjectSet<Entity> b = new ObjectOpenHashSet<>();
    private final ObjectSet<Entity> c = new ObjectOpenHashSet<>();

    public EntitySenses(EntityInsentient entityinsentient) {
        this.a = entityinsentient;
    }

    public void a() {
        this.b.clear();
        this.c.clear();
    }

    public boolean a(Entity entity) {
        if (this.b.contains(entity)) {
            return true;
        } else if (this.c.contains(entity)) {
            return false;
        } else {
            boolean flag = this.a.hasLineOfSight(entity);

            if (flag) {
                this.b.add(entity);
            } else {
                this.c.add(entity);
            }

            return flag;
        }
    }
}

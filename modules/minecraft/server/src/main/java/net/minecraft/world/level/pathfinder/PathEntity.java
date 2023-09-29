package net.minecraft.world.level.pathfinder;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3D;

import javax.annotation.Nullable;
import java.util.List;

public class PathEntity {

    private final List<PathPoint> a; public List<PathPoint> getPoints() { return a; } // Paper - OBFHELPER
    private PathPoint[] b = new PathPoint[0];
    private PathPoint[] c = new PathPoint[0];
    private int e; public int getNextIndex() { return this.e; } // Paper - OBFHELPER
    private final BlockPosition f;
    private final float g;
    private final boolean h;
    public boolean hasNext() { return getNextIndex() < getPoints().size(); } // Paper

    public PathEntity(List<PathPoint> list, BlockPosition blockposition, boolean flag) {
        this.a = list;
        this.f = blockposition;
        this.g = list.isEmpty() ? Float.MAX_VALUE : this.a.get(this.a.size() - 1).c(this.f);
        this.h = flag;
    }

    public void a() {
        ++this.e;
    }

    public boolean b() {
        return this.e <= 0;
    }

    public boolean c() {
        return this.e >= this.a.size();
    }

    @Nullable
    public PathPoint getFinalPoint() { return d(); } @Nullable public PathPoint d() { // Paper - OBFHELPER
        return !this.a.isEmpty() ? this.a.get(this.a.size() - 1) : null;
    }

    public PathPoint a(int i) {
        return this.a.get(i);
    }

    public void b(int i) {
        if (this.a.size() > i) {
            this.a.subList(i, this.a.size()).clear();
        }

    }

    public void a(int i, PathPoint pathpoint) {
        this.a.set(i, pathpoint);
    }

    public int e() {
        return this.a.size();
    }

    public int f() {
        return this.e;
    }

    public void c(int i) {
        this.e = i;
    }

    public Vec3D a(Entity entity, int i) {
        PathPoint pathpoint = this.a.get(i);
        double d0 = (double) pathpoint.a + (double) ((int) (entity.getWidth() + 1.0F)) * 0.5D;
        double d1 = pathpoint.b;
        double d2 = (double) pathpoint.c + (double) ((int) (entity.getWidth() + 1.0F)) * 0.5D;

        return new Vec3D(d0, d1, d2);
    }

    public BlockPosition d(int i) {
        return this.a.get(i).a();
    }

    public Vec3D a(Entity entity) {
        return this.a(entity, this.e);
    }

    public BlockPosition getNext() { return g(); } public BlockPosition g() { // Paper - OBFHELPER
        return this.a.get(this.e).a();
    }

    public PathPoint h() {
        return this.a.get(this.e);
    }

    @Nullable
    public PathPoint i() {
        return this.e > 0 ? this.a.get(this.e - 1) : null;
    }

    public boolean a(@Nullable PathEntity pathentity) {
        if (pathentity == null) {
            return false;
        } else if (pathentity.a.size() != this.a.size()) {
            return false;
        } else {
            for (int i = 0; i < this.a.size(); ++i) {
                PathPoint pathpoint = this.a.get(i);
                PathPoint pathpoint1 = pathentity.a.get(i);

                if (pathpoint.a != pathpoint1.a || pathpoint.b != pathpoint1.b || pathpoint.c != pathpoint1.c) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean j() {
        return this.h;
    }

    public String toString() {
        return "Path(length=" + this.a.size() + ")";
    }

    public BlockPosition m() {
        return this.f;
    }

    public float n() {
        return this.g;
    }
}

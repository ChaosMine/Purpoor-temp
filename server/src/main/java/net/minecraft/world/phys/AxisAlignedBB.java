package net.minecraft.world.phys;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.util.MathHelper;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;

public class AxisAlignedBB {

    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    // Tuinity start
    public final boolean isEmpty() {
        return (this.maxX - this.minX) < net.minecraft.server.MCUtil.COLLISION_EPSILON && (this.maxY - this.minY) < net.minecraft.server.MCUtil.COLLISION_EPSILON && (this.maxZ - this.minZ) < net.minecraft.server.MCUtil.COLLISION_EPSILON;
    }

    public static AxisAlignedBB getBoxForChunk(int chunkX, int chunkZ) {
        double x = (double)(chunkX << 4);
        double z = (double)(chunkZ << 4);
        // use a bounding box bigger than the chunk to prevent entities from entering it on move
        return new AxisAlignedBB(x - 3*net.minecraft.server.MCUtil.COLLISION_EPSILON, Double.NEGATIVE_INFINITY, z - 3*net.minecraft.server.MCUtil.COLLISION_EPSILON, x + (16.0 + 3*net.minecraft.server.MCUtil.COLLISION_EPSILON), Double.POSITIVE_INFINITY, z + (16.0 + 3*net.minecraft.server.MCUtil.COLLISION_EPSILON), false);
    }

    /*
      A couple of rules for VoxelShape collisions:
      Two shapes only intersect if they are actually more than EPSILON units into each other. This also applies to movement
      checks.
      If the two shapes strictly collide, then the return value of a collide call will return a value in the opposite
      direction of the source move. However, this value will not be greater in magnitude than EPSILON. Collision code
      will automatically round it to 0.
     */

    public final boolean voxelShapeIntersect(AxisAlignedBB other) {
        return (this.minX - other.maxX) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (this.maxX - other.minX) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
               (this.minY - other.maxY) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (this.maxY - other.minY) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
               (this.minZ - other.maxZ) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (this.maxZ - other.minZ) > net.minecraft.server.MCUtil.COLLISION_EPSILON;
    }

    public final boolean voxelShapeIntersect(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return (this.minX - maxX) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (this.maxX - minX) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
               (this.minY - maxY) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (this.maxY - minY) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
               (this.minZ - maxZ) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (this.maxZ - minZ) > net.minecraft.server.MCUtil.COLLISION_EPSILON;
    }

    public static boolean voxelShapeIntersect(double minX1, double minY1, double minZ1, double maxX1, double maxY1, double maxZ1,
                                              double minX2, double minY2, double minZ2, double maxX2, double maxY2, double maxZ2) {
        return (minX1 - maxX2) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (maxX1 - minX2) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
                (minY1 - maxY2) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (maxY1 - minY2) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
                (minZ1 - maxZ2) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (maxZ1 - minZ2) > net.minecraft.server.MCUtil.COLLISION_EPSILON;
    }

    public static double collideX(AxisAlignedBB target, AxisAlignedBB source, double source_move) {
        if (source_move == 0.0) {
            return 0.0;
        }

        if ((source.minY - target.maxY) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (source.maxY - target.minY) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
                (source.minZ - target.maxZ) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (source.maxZ - target.minZ) > net.minecraft.server.MCUtil.COLLISION_EPSILON) {

            if (source_move >= 0.0) {
                double max_move = target.minX - source.maxX; // < 0.0 if no strict collision
                if (max_move < -net.minecraft.server.MCUtil.COLLISION_EPSILON) {
                    return source_move;
                }
                return Math.min(max_move, source_move);
            } else {
                double max_move = target.maxX - source.minX; // > 0.0 if no strict collision
                if (max_move > net.minecraft.server.MCUtil.COLLISION_EPSILON) {
                    return source_move;
                }
                return Math.max(max_move, source_move);
            }
        }
        return source_move;
    }

    public static double collideY(AxisAlignedBB target, AxisAlignedBB source, double source_move) {
        if (source_move == 0.0) {
            return 0.0;
        }

        if ((source.minX - target.maxX) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (source.maxX - target.minX) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
                (source.minZ - target.maxZ) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (source.maxZ - target.minZ) > net.minecraft.server.MCUtil.COLLISION_EPSILON) {
            if (source_move >= 0.0) {
                double max_move = target.minY - source.maxY; // < 0.0 if no strict collision
                if (max_move < -net.minecraft.server.MCUtil.COLLISION_EPSILON) {
                    return source_move;
                }
                return Math.min(max_move, source_move);
            } else {
                double max_move = target.maxY - source.minY; // > 0.0 if no strict collision
                if (max_move > net.minecraft.server.MCUtil.COLLISION_EPSILON) {
                    return source_move;
                }
                return Math.max(max_move, source_move);
            }
        }
        return source_move;
    }

    public static double collideZ(AxisAlignedBB target, AxisAlignedBB source, double source_move) {
        if (source_move == 0.0) {
            return 0.0;
        }

        if ((source.minX - target.maxX) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (source.maxX - target.minX) > net.minecraft.server.MCUtil.COLLISION_EPSILON &&
                (source.minY - target.maxY) < -net.minecraft.server.MCUtil.COLLISION_EPSILON && (source.maxY - target.minY) > net.minecraft.server.MCUtil.COLLISION_EPSILON) {
            if (source_move >= 0.0) {
                double max_move = target.minZ - source.maxZ; // < 0.0 if no strict collision
                if (max_move < -net.minecraft.server.MCUtil.COLLISION_EPSILON) {
                    return source_move;
                }
                return Math.min(max_move, source_move);
            } else {
                double max_move = target.maxZ - source.minZ; // > 0.0 if no strict collision
                if (max_move > net.minecraft.server.MCUtil.COLLISION_EPSILON) {
                    return source_move;
                }
                return Math.max(max_move, source_move);
            }
        }
        return source_move;
    }

    public final AxisAlignedBB offsetX(double dx) {
        return new AxisAlignedBB(this.minX + dx, this.minY, this.minZ, this.maxX + dx, this.maxY, this.maxZ, false);
    }

    public final AxisAlignedBB offsetY(double dy) {
        return new AxisAlignedBB(this.minX, this.minY + dy, this.minZ, this.maxX, this.maxY + dy, this.maxZ, false);
    }

    public final AxisAlignedBB offsetZ(double dz) {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ + dz, this.maxX, this.maxY, this.maxZ + dz, false);
    }

    public AxisAlignedBB(double d0, double d1, double d2, double d3, double d4, double d5, boolean dummy) {
        this.minX = d0;
        this.minY = d1;
        this.minZ = d2;
        this.maxX = d3;
        this.maxY = d4;
        this.maxZ = d5;
    }

    public final AxisAlignedBB expandUpwards(double dy) {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY + dy, this.maxZ, false);
    }

    public final  AxisAlignedBB cutUpwards(final double dy) { // dy > 0.0
        return new AxisAlignedBB(this.minX, this.maxY, this.minZ, this.maxX, this.maxY + dy, this.maxZ, false);
    }

    public final AxisAlignedBB cutDownwards(final double dy) { // dy < 0.0
        return new AxisAlignedBB(this.minX, this.minY + dy, this.minZ, this.maxX, this.minY, this.maxZ, false);
    }

    public final AxisAlignedBB expandUpwardsAndCutBelow(double dy) {
        return new AxisAlignedBB(this.minX, this.maxY, this.minZ, this.maxX, this.maxY + dy, this.maxZ, false);
    }
    // Tuinity end

    public AxisAlignedBB(double d0, double d1, double d2, double d3, double d4, double d5) {
        this.minX = Math.min(d0, d3);
        this.minY = Math.min(d1, d4);
        this.minZ = Math.min(d2, d5);
        this.maxX = Math.max(d0, d3);
        this.maxY = Math.max(d1, d4);
        this.maxZ = Math.max(d2, d5);
    }

    public AxisAlignedBB(BlockPosition blockposition) {
        this((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) (blockposition.getX() + 1), (double) (blockposition.getY() + 1), (double) (blockposition.getZ() + 1));
    }

    public AxisAlignedBB(BlockPosition blockposition, BlockPosition blockposition1) {
        this((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
    }

    public AxisAlignedBB(Vec3D vec3d, Vec3D vec3d1) {
        this(vec3d.x, vec3d.y, vec3d.z, vec3d1.x, vec3d1.y, vec3d1.z);
    }

    public static AxisAlignedBB a(StructureBoundingBox structureboundingbox) {
        return new AxisAlignedBB((double) structureboundingbox.a, (double) structureboundingbox.b, (double) structureboundingbox.c, (double) (structureboundingbox.d + 1), (double) (structureboundingbox.e + 1), (double) (structureboundingbox.f + 1));
    }

    public static AxisAlignedBB a(Vec3D vec3d) {
        return new AxisAlignedBB(vec3d.x, vec3d.y, vec3d.z, vec3d.x + 1.0D, vec3d.y + 1.0D, vec3d.z + 1.0D);
    }

    public double a(EnumDirection.EnumAxis enumdirection_enumaxis) {
        return enumdirection_enumaxis.a(this.minX, this.minY, this.minZ);
    }

    public double b(EnumDirection.EnumAxis enumdirection_enumaxis) {
        return enumdirection_enumaxis.a(this.maxX, this.maxY, this.maxZ);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof AxisAlignedBB)) {
            return false;
        } else {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB) object;

            return Double.compare(axisalignedbb.minX, this.minX) != 0 ? false : (Double.compare(axisalignedbb.minY, this.minY) != 0 ? false : (Double.compare(axisalignedbb.minZ, this.minZ) != 0 ? false : (Double.compare(axisalignedbb.maxX, this.maxX) != 0 ? false : (Double.compare(axisalignedbb.maxY, this.maxY) != 0 ? false : Double.compare(axisalignedbb.maxZ, this.maxZ) == 0))));
        }
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.minX);
        int j = (int) (i ^ i >>> 32);

        i = Double.doubleToLongBits(this.minY);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minZ);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxX);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxY);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxZ);
        j = 31 * j + (int) (i ^ i >>> 32);
        return j;
    }

    public AxisAlignedBB a(double d0, double d1, double d2) {
        double d3 = this.minX;
        double d4 = this.minY;
        double d5 = this.minZ;
        double d6 = this.maxX;
        double d7 = this.maxY;
        double d8 = this.maxZ;

        if (d0 < 0.0D) {
            d3 -= d0;
        } else if (d0 > 0.0D) {
            d6 -= d0;
        }

        if (d1 < 0.0D) {
            d4 -= d1;
        } else if (d1 > 0.0D) {
            d7 -= d1;
        }

        if (d2 < 0.0D) {
            d5 -= d2;
        } else if (d2 > 0.0D) {
            d8 -= d2;
        }

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB b(Vec3D vec3d) {
        return this.b(vec3d.x, vec3d.y, vec3d.z);
    }

    public final AxisAlignedBB expand(double x, double y, double z) { return b(x, y, z); } // Paper - OBFHELPER
    public AxisAlignedBB b(double d0, double d1, double d2) {
        double d3 = this.minX;
        double d4 = this.minY;
        double d5 = this.minZ;
        double d6 = this.maxX;
        double d7 = this.maxY;
        double d8 = this.maxZ;

        if (d0 < 0.0D) {
            d3 += d0;
        } else if (d0 > 0.0D) {
            d6 += d0;
        }

        if (d1 < 0.0D) {
            d4 += d1;
        } else if (d1 > 0.0D) {
            d7 += d1;
        }

        if (d2 < 0.0D) {
            d5 += d2;
        } else if (d2 > 0.0D) {
            d8 += d2;
        }

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    // Paper start
    public AxisAlignedBB grow(double d0) {
        return grow(d0, d0, d0);
    }
    // Paper end

    public AxisAlignedBB grow(double d0, double d1, double d2) {
        double d3 = this.minX - d0;
        double d4 = this.minY - d1;
        double d5 = this.minZ - d2;
        double d6 = this.maxX + d0;
        double d7 = this.maxY + d1;
        double d8 = this.maxZ + d2;

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB g(double d0) {
        return this.grow(d0, d0, d0);
    }

    public AxisAlignedBB a(AxisAlignedBB axisalignedbb) {
        double d0 = Math.max(this.minX, axisalignedbb.minX);
        double d1 = Math.max(this.minY, axisalignedbb.minY);
        double d2 = Math.max(this.minZ, axisalignedbb.minZ);
        double d3 = Math.min(this.maxX, axisalignedbb.maxX);
        double d4 = Math.min(this.maxY, axisalignedbb.maxY);
        double d5 = Math.min(this.maxZ, axisalignedbb.maxZ);

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB b(AxisAlignedBB axisalignedbb) {
        double d0 = Math.min(this.minX, axisalignedbb.minX);
        double d1 = Math.min(this.minY, axisalignedbb.minY);
        double d2 = Math.min(this.minZ, axisalignedbb.minZ);
        double d3 = Math.max(this.maxX, axisalignedbb.maxX);
        double d4 = Math.max(this.maxY, axisalignedbb.maxY);
        double d5 = Math.max(this.maxZ, axisalignedbb.maxZ);

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public final AxisAlignedBB offset(double d0, double d1, double d2) { return this.d(d0, d1, d2); } // Tuinity - OBFHELPER
    public AxisAlignedBB d(double d0, double d1, double d2) {
        return new AxisAlignedBB(this.minX + d0, this.minY + d1, this.minZ + d2, this.maxX + d0, this.maxY + d1, this.maxZ + d2);
    }

    public AxisAlignedBB a(BlockPosition blockposition) {
        return new AxisAlignedBB(this.minX + (double) blockposition.getX(), this.minY + (double) blockposition.getY(), this.minZ + (double) blockposition.getZ(), this.maxX + (double) blockposition.getX(), this.maxY + (double) blockposition.getY(), this.maxZ + (double) blockposition.getZ());
    }

    public final AxisAlignedBB offset(Vec3D vec3d) { return this.b(vec3d); } // Tuinity - OBFHELPER
    public AxisAlignedBB c(Vec3D vec3d) {
        return this.d(vec3d.x, vec3d.y, vec3d.z);
    }

    public final boolean intersects(AxisAlignedBB axisalignedbb) { return this.c(axisalignedbb); } // Paper - OBFHELPER
    public boolean c(AxisAlignedBB axisalignedbb) {
        return this.a(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    }

    public final boolean intersects(double d0, double d1, double d2, double d3, double d4, double d5) { return a(d0, d1, d2, d3, d4, d5); } // Paper - OBFHELPER
    public boolean a(double d0, double d1, double d2, double d3, double d4, double d5) {
        return this.minX < d3 && this.maxX > d0 && this.minY < d4 && this.maxY > d1 && this.minZ < d5 && this.maxZ > d2;
    }

    public final boolean contains(Vec3D vec3d) { return d(vec3d); } // Paper - OBFHELPER
    public boolean d(Vec3D vec3d) {
        return this.e(vec3d.x, vec3d.y, vec3d.z);
    }

    public final boolean contains(double d0, double d1, double d2) { return this.e(d0, d1, d2); } // Tuinity - OBFHELPER
    public boolean e(double d0, double d1, double d2) {
        return d0 >= this.minX && d0 < this.maxX && d1 >= this.minY && d1 < this.maxY && d2 >= this.minZ && d2 < this.maxZ;
    }

    public final double getAverageSideLength(){return a();} // Paper - OBFHELPER
    public double a() {
        double d0 = this.b();
        double d1 = this.c();
        double d2 = this.d();

        return (d0 + d1 + d2) / 3.0D;
    }

    public double b() {
        return this.maxX - this.minX;
    }

    public double c() {
        return this.maxY - this.minY;
    }

    public double d() {
        return this.maxZ - this.minZ;
    }

    public AxisAlignedBB shrink(double d0) {
        return this.g(-d0);
    }

    public final Optional<Vec3D> calculateIntercept(Vec3D vec3d, Vec3D vec3d1) { return b(vec3d, vec3d1); } // Paper - OBFHELPER
    public Optional<Vec3D> b(Vec3D vec3d, Vec3D vec3d1) {
        double[] adouble = new double[]{1.0D};
        double d0 = vec3d1.x - vec3d.x;
        double d1 = vec3d1.y - vec3d.y;
        double d2 = vec3d1.z - vec3d.z;
        EnumDirection enumdirection = a(this, vec3d, adouble, (EnumDirection) null, d0, d1, d2);

        if (enumdirection == null) {
            return Optional.empty();
        } else {
            double d3 = adouble[0];

            return Optional.of(vec3d.add(d3 * d0, d3 * d1, d3 * d2));
        }
    }

    @Nullable
    public static MovingObjectPositionBlock a(Iterable<AxisAlignedBB> iterable, Vec3D vec3d, Vec3D vec3d1, BlockPosition blockposition) {
        double[] adouble = new double[]{1.0D};
        EnumDirection enumdirection = null;
        double d0 = vec3d1.x - vec3d.x;
        double d1 = vec3d1.y - vec3d.y;
        double d2 = vec3d1.z - vec3d.z;

        AxisAlignedBB axisalignedbb;

        for (Iterator iterator = iterable.iterator(); iterator.hasNext(); enumdirection = a(axisalignedbb.a(blockposition), vec3d, adouble, enumdirection, d0, d1, d2)) {
            axisalignedbb = (AxisAlignedBB) iterator.next();
        }

        if (enumdirection == null) {
            return null;
        } else {
            double d3 = adouble[0];

            return new MovingObjectPositionBlock(vec3d.add(d3 * d0, d3 * d1, d3 * d2), enumdirection, blockposition, false);
        }
    }

    @Nullable
    private static EnumDirection a(AxisAlignedBB axisalignedbb, Vec3D vec3d, double[] adouble, @Nullable EnumDirection enumdirection, double d0, double d1, double d2) {
        if (d0 > 1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d0, d1, d2, axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxY, axisalignedbb.minZ, axisalignedbb.maxZ, EnumDirection.WEST, vec3d.x, vec3d.y, vec3d.z);
        } else if (d0 < -1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d0, d1, d2, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxY, axisalignedbb.minZ, axisalignedbb.maxZ, EnumDirection.EAST, vec3d.x, vec3d.y, vec3d.z);
        }

        if (d1 > 1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d1, d2, d0, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxZ, axisalignedbb.minX, axisalignedbb.maxX, EnumDirection.DOWN, vec3d.y, vec3d.z, vec3d.x);
        } else if (d1 < -1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d1, d2, d0, axisalignedbb.maxY, axisalignedbb.minZ, axisalignedbb.maxZ, axisalignedbb.minX, axisalignedbb.maxX, EnumDirection.UP, vec3d.y, vec3d.z, vec3d.x);
        }

        if (d2 > 1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d2, d0, d1, axisalignedbb.minZ, axisalignedbb.minX, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxY, EnumDirection.NORTH, vec3d.z, vec3d.x, vec3d.y);
        } else if (d2 < -1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d2, d0, d1, axisalignedbb.maxZ, axisalignedbb.minX, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxY, EnumDirection.SOUTH, vec3d.z, vec3d.x, vec3d.y);
        }

        return enumdirection;
    }

    @Nullable
    private static EnumDirection a(double[] adouble, @Nullable EnumDirection enumdirection, double d0, double d1, double d2, double d3, double d4, double d5, double d6, double d7, EnumDirection enumdirection1, double d8, double d9, double d10) {
        double d11 = (d3 - d8) / d0;
        double d12 = d9 + d11 * d1;
        double d13 = d10 + d11 * d2;

        if (0.0D < d11 && d11 < adouble[0] && d4 - 1.0E-7D < d12 && d12 < d5 + 1.0E-7D && d6 - 1.0E-7D < d13 && d13 < d7 + 1.0E-7D) {
            adouble[0] = d11;
            return enumdirection1;
        } else {
            return enumdirection;
        }
    }

    public String toString() {
        return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public Vec3D f() {
        return new Vec3D(MathHelper.d(0.5D, this.minX, this.maxX), MathHelper.d(0.5D, this.minY, this.maxY), MathHelper.d(0.5D, this.minZ, this.maxZ));
    }

    public static AxisAlignedBB g(double d0, double d1, double d2) {
        return new AxisAlignedBB(-d0 / 2.0D, -d1 / 2.0D, -d2 / 2.0D, d0 / 2.0D, d1 / 2.0D, d2 / 2.0D);
    }
}

package net.minecraft.core;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import java.util.stream.IntStream;
import javax.annotation.concurrent.Immutable;
import net.minecraft.SystemUtils;
import net.minecraft.util.MathHelper;

@Immutable
public class BaseBlockPosition implements Comparable<BaseBlockPosition> {

    public static final Codec<BaseBlockPosition> c = Codec.INT_STREAM.comapFlatMap((intstream) -> {
        return SystemUtils.a(intstream, 3).map((aint) -> {
            return new BaseBlockPosition(aint[0], aint[1], aint[2]);
        });
    }, (baseblockposition) -> {
        return IntStream.of(new int[]{baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ()});
    });
    public static final BaseBlockPosition ZERO = new BaseBlockPosition(0, 0, 0);
    protected int a; // Paper - OBFHELPER // Tuinity - private->protected - diff on change, this is the x coordinate - Also revert the decision to expose set on an immutable type
    protected int b; // Paper - OBFHELPER // Tuinity - private->protected - diff on change, this is the y coordinate - Also revert the decision to expose set on an immutable type
    protected int e; // Paper - OBFHELPER // Tuinity - private->protected - diff on change, this is the z coordinate - Also revert the decision to expose set on an immutable type

    // Paper start
    public boolean isValidLocation() {
        return getX() >= -30000000 && getZ() >= -30000000 && getX() < 30000000 && getZ() < 30000000 && getY() >= 0 && getY() < 256;
    }
    public boolean isInvalidYLocation() {
        return b < 0 || b >= 256;
    }
    // Paper end

    public BaseBlockPosition(int i, int j, int k) {
        this.a = i;
        this.b = j;
        this.e = k;
    }

    public BaseBlockPosition(double d0, double d1, double d2) {
        this(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
    }

    public final boolean equals(Object object) { // Paper
        if (this == object) {
            return true;
        } else if (!(object instanceof BaseBlockPosition)) {
            return false;
        } else {
            BaseBlockPosition baseblockposition = (BaseBlockPosition) object;

            return this.getX() != baseblockposition.getX() ? false : (this.getY() != baseblockposition.getY() ? false : this.getZ() == baseblockposition.getZ());
        }
    }

    public final int hashCode() { // Paper
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(BaseBlockPosition baseblockposition) {
        return this.getY() == baseblockposition.getY() ? (this.getZ() == baseblockposition.getZ() ? this.getX() - baseblockposition.getX() : this.getZ() - baseblockposition.getZ()) : this.getY() - baseblockposition.getY();
    }

    public final int getX() { // Paper
        return this.a;
    }

    public final int getY() { // Paper
        return this.b;
    }

    public final int getZ() { // Paper
        return this.e;
    }

    protected void o_unused(int i) { // Paper - protected -> public // Tuinity - not needed here - Also revert the decision to expose set on an immutable type
        this.a = i;
    }

    protected void p_unused(int i) { // Paper - protected -> public // Tuinity - not needed here - Also revert the decision to expose set on an immutable type
        this.b = i;
    }

    protected void q_unused(int i) { // Paper - protected -> public // Tuinity - not needed here - Also revert the decision to expose set on an immutable type
        this.e = i;
    }

    public BaseBlockPosition up() {
        return this.up(1);
    }

    public BaseBlockPosition up(int i) {
        return this.shift(EnumDirection.UP, i);
    }

    public BaseBlockPosition down() {
        return this.down(1);
    }

    public BaseBlockPosition down(int i) {
        return this.shift(EnumDirection.DOWN, i);
    }

    public BaseBlockPosition shift(EnumDirection enumdirection, int i) {
        return i == 0 ? this : new BaseBlockPosition(this.getX() + enumdirection.getAdjacentX() * i, this.getY() + enumdirection.getAdjacentY() * i, this.getZ() + enumdirection.getAdjacentZ() * i);
    }

    public BaseBlockPosition d(BaseBlockPosition baseblockposition) {
        return new BaseBlockPosition(this.getY() * baseblockposition.getZ() - this.getZ() * baseblockposition.getY(), this.getZ() * baseblockposition.getX() - this.getX() * baseblockposition.getZ(), this.getX() * baseblockposition.getY() - this.getY() * baseblockposition.getX());
    }

    public boolean a(BaseBlockPosition baseblockposition, double d0) {
        return this.distanceSquared((double) baseblockposition.getX(), (double) baseblockposition.getY(), (double) baseblockposition.getZ(), false) < d0 * d0;
    }

    public boolean a(IPosition iposition, double d0) {
        return this.distanceSquared(iposition.getX(), iposition.getY(), iposition.getZ(), true) < d0 * d0;
    }

    public final double distanceSquared(BaseBlockPosition baseblockposition) { return j(baseblockposition); } // Paper - OBFHELPER
    public double j(BaseBlockPosition baseblockposition) {
        return this.distanceSquared((double) baseblockposition.getX(), (double) baseblockposition.getY(), (double) baseblockposition.getZ(), true);
    }

    public double a(IPosition iposition, boolean flag) {
        return this.distanceSquared(iposition.getX(), iposition.getY(), iposition.getZ(), flag);
    }

    public double distanceSquared(double d0, double d1, double d2, boolean flag) {
        double d3 = flag ? 0.5D : 0.0D;
        double d4 = (double) this.getX() + d3 - d0;
        double d5 = (double) this.getY() + d3 - d1;
        double d6 = (double) this.getZ() + d3 - d2;

        return d4 * d4 + d5 * d5 + d6 * d6;
    }

    public int k(BaseBlockPosition baseblockposition) {
        float f = (float) Math.abs(baseblockposition.getX() - this.getX());
        float f1 = (float) Math.abs(baseblockposition.getY() - this.getY());
        float f2 = (float) Math.abs(baseblockposition.getZ() - this.getZ());

        return (int) (f + f1 + f2);
    }

    public int a(EnumDirection.EnumAxis enumdirection_enumaxis) {
        return enumdirection_enumaxis.a(this.a, this.b, this.e);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }
}

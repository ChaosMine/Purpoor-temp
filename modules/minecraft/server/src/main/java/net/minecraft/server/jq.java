package net.minecraft.server;

import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class jq implements jp.a {

    private static final Logger LOGGER = LogManager.getLogger();

    public jq() {}

    @Override
    public NBTTagCompound a(String s, NBTTagCompound nbttagcompound) {
        return s.startsWith("data/minecraft/structures/") ? b(s, a(nbttagcompound)) : nbttagcompound;
    }

    private static NBTTagCompound a(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKeyOfType("DataVersion", 99)) {
            nbttagcompound.setInt("DataVersion", 500);
        }

        return nbttagcompound;
    }

    private static NBTTagCompound b(String s, NBTTagCompound nbttagcompound) {
        DefinedStructure definedstructure = new DefinedStructure();
        int i = nbttagcompound.getInt("DataVersion");
        boolean flag = true;

        if (i < 2532) {
            jq.LOGGER.warn("SNBT Too old, do not forget to update: " + i + " < " + 2532 + ": " + s);
        }

        NBTTagCompound nbttagcompound1 = GameProfileSerializer.a(DataConverterRegistry.a(), DataFixTypes.STRUCTURE, nbttagcompound, i);

        definedstructure.b(nbttagcompound1);
        return definedstructure.a(new NBTTagCompound());
    }
}

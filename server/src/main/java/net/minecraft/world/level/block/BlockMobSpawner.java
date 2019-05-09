package net.minecraft.world.level.block;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.IBlockData;

// Purpur start
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
// Purpur end

public class BlockMobSpawner extends BlockTileEntity {

    protected BlockMobSpawner(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityMobSpawner();
    }

    // Purpur start
    @Override
    public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, TileEntity tileentity, ItemStack itemstack) {
        if (world.purpurConfig.silkTouchEnabled && entityhuman.getBukkitEntity().hasPermission("purpur.drop.spawners") && isSilkTouch(world, itemstack)) {
            MinecraftKey type = ((TileEntityMobSpawner) tileentity).getSpawner().getMobName();
            if (type != null) {
                final Component mobName = PaperAdventure.asAdventure(EntityTypes.getFromKey(type).getNameComponent());
                final TextReplacementConfig config = TextReplacementConfig.builder()
                        .matchLiteral("{mob}")
                        .replacement(mobName)
                        .build();

                NBTTagCompound display = new NBTTagCompound();
                boolean customDisplay = false;

                String name = world.purpurConfig.silkTouchSpawnerName;
                if (name != null && !name.isEmpty() && !name.equals("Spawner")) {
                    final Component displayName = PaperAdventure.LEGACY_AMPERSAND.deserialize(name).replaceText(config).decoration(ITALIC, false);
                    display.set("Name", NBTTagString.create(GsonComponentSerializer.gson().serialize(displayName)));
                    customDisplay = true;
                }

                List<String> lore = world.purpurConfig.silkTouchSpawnerLore;
                if (lore != null && !lore.isEmpty()) {
                    NBTTagList list = new NBTTagList();
                    for (String line : lore) {
                        final Component lineComponent = PaperAdventure.LEGACY_AMPERSAND.deserialize(line).replaceText(config).decoration(ITALIC, false);
                        list.add(NBTTagString.create(GsonComponentSerializer.gson().serialize(lineComponent)));
                    }
                    display.set("Lore", list);
                    customDisplay = true;
                }

                NBTTagCompound tag = new NBTTagCompound();
                if (customDisplay) {
                    tag.set("display", display);
                }
                tag.setString("Purpur.mob_type", type.toString());

                ItemStack item = new ItemStack(Blocks.SPAWNER.getItem());
                item.setTag(tag);

                dropItem(world, blockposition, item);
            }
        }
        super.a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
    }

    private boolean isSilkTouch(World world, ItemStack itemstack) {
        return itemstack != null && world.purpurConfig.silkTouchTools.contains(itemstack.getItem()) && EnchantmentManager.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) > 0;
    }
    // Purpur end

    @Override
    public void dropNaturally(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, ItemStack itemstack) {
        super.dropNaturally(iblockdata, worldserver, blockposition, itemstack);
        /* CraftBukkit start - Delegate to getExpDrop
        int i = 15 + worldserver.random.nextInt(15) + worldserver.random.nextInt(15);

        this.dropExperience(worldserver, blockposition, i);
        */
    }

    @Override
    public int getExpDrop(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, ItemStack itemstack) {
        if (isSilkTouch(worldserver, itemstack)) return 0; // Purpur
        int i = 15 + worldserver.random.nextInt(15) + worldserver.random.nextInt(15);

        return i;
        // CraftBukkit end
    }

    @Override
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }
}

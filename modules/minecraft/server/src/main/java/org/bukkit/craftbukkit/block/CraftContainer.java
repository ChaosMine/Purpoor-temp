package org.bukkit.craftbukkit.block;

import net.minecraft.world.ChestLock;
import net.minecraft.world.level.block.entity.TileEntityContainer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public abstract class CraftContainer<T extends TileEntityContainer> extends CraftBlockEntityState<T> implements Container {

    public CraftContainer(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftContainer(final Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public boolean isLocked() {
        return !this.getSnapshot().chestLock.key.isEmpty();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().chestLock.key;
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().chestLock = (key == null) ? ChestLock.a : new ChestLock(key);
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component customName() {
        final T be = this.getSnapshot();
        return be.hasCustomName() ? io.papermc.paper.adventure.PaperAdventure.asAdventure(be.getCustomName()) : null;
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.getSnapshot().setCustomName(customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null);
    }
    // Paper end

    @Override
    public String getCustomName() {
        T container = this.getSnapshot();
        return container.customName != null ? CraftChatMessage.fromComponent(container.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(T container) {
        super.applyTo(container);

        if (this.getSnapshot().customName == null) {
            container.setCustomName(null);
        }
    }
}

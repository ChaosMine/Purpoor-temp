package org.bukkit.craftbukkit.advancement;

import net.minecraft.advancements.AdvancementDisplay;
import org.bukkit.advancement.FrameType;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftAdvancementDisplay implements org.bukkit.advancement.AdvancementDisplay {
    private final AdvancementDisplay handle;

    public CraftAdvancementDisplay(AdvancementDisplay handle) {
        this.handle = handle;
    }

    public AdvancementDisplay getHandle() {
        return handle;
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(handle.getTitle());
    }

    @Override
    public String getDescription() {
        return CraftChatMessage.fromComponent(handle.getDescription());
    }

    @Override
    public FrameType getFrameType() {
        return handle.getFrameType().bukkit;
    }

    @Override
    public boolean shouldAnnounceToChat() {
        return handle.shouldAnnounceToChat();
    }

    @Override
    public void setShouldAnnounceToChat(boolean announce) {
        handle.setShouldAnnounceToChat(announce);
    }

    @Override
    public boolean isHidden() {
        return handle.isHidden();
    }
}

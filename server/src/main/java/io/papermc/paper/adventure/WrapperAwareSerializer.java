package io.papermc.paper.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.minecraft.network.chat.IChatBaseComponent;

final class WrapperAwareSerializer implements ComponentSerializer<Component, Component, IChatBaseComponent> {
    @Override
    public Component deserialize(final IChatBaseComponent input) {
        if (input instanceof AdventureComponent) {
            return ((AdventureComponent) input).wrapped;
        }
        return PaperAdventure.GSON.serializer().fromJson(IChatBaseComponent.ChatSerializer.toJsonTree(input), Component.class);
    }

    @Override
    public IChatBaseComponent serialize(final Component component) {
        return IChatBaseComponent.ChatSerializer.fromJsonTree(PaperAdventure.GSON.serializer().toJsonTree(component));
    }
}

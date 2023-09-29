package net.minecraft.network.chat;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;

public abstract class ChatBaseComponent implements IChatMutableComponent {

    protected final List<IChatBaseComponent> siblings = Lists.newArrayList();
    private ChatModifier f;

    public ChatBaseComponent() {
        this.f = ChatModifier.a;
    }

    @Override
    public IChatMutableComponent addSibling(IChatBaseComponent ichatbasecomponent) {
        this.siblings.add(ichatbasecomponent);
        return this;
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public List<IChatBaseComponent> getSiblings() {
        return this.siblings;
    }

    @Override
    public IChatMutableComponent setChatModifier(ChatModifier chatmodifier) {
        this.f = chatmodifier;
        return this;
    }

    @Override
    public ChatModifier getChatModifier() {
        return this.f;
    }

    @Override
    public abstract ChatBaseComponent g();

    @Override
    public final IChatMutableComponent mutableCopy() {
        ChatBaseComponent chatbasecomponent = this.g();

        chatbasecomponent.siblings.addAll(this.siblings);
        chatbasecomponent.setChatModifier(this.f);
        return chatbasecomponent;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChatBaseComponent chatbasecomponent)) {
            return false;
        } else {
            return this.siblings.equals(chatbasecomponent.siblings) && Objects.equals(this.getChatModifier(), chatbasecomponent.getChatModifier());
        }
    }

    public int hashCode() {
        return Objects.hash(this.getChatModifier(), this.siblings);
    }

    public String toString() {
        return "BaseComponent{style=" + this.f + ", siblings=" + this.siblings + '}';
    }
}

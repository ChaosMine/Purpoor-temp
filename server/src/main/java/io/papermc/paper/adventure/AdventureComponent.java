package io.papermc.paper.adventure;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.network.chat.ChatModifier;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.List;

public final class AdventureComponent implements IChatBaseComponent {
    final Component wrapped;
    private @MonotonicNonNull IChatBaseComponent converted;

    public AdventureComponent(final Component wrapped) {
        this.wrapped = wrapped;
    }

    public IChatBaseComponent deepConverted() {
        IChatBaseComponent converted = this.converted;
        if (converted == null) {
            converted = PaperAdventure.WRAPPER_AWARE_SERIALIZER.serialize(this.wrapped);
            this.converted = converted;
        }
        return converted;
    }

    public @Nullable IChatBaseComponent deepConvertedIfPresent() {
        return this.converted;
    }

    @Override
    public ChatModifier getChatModifier() {
        return this.deepConverted().getChatModifier();
    }

    @Override
    public String getText() {
        if (this.wrapped instanceof TextComponent) {
            return ((TextComponent) this.wrapped).content();
        } else {
            return this.deepConverted().getText();
        }
    }

    @Override
    public String getString() {
        return PaperAdventure.PLAIN.serialize(this.wrapped);
    }

    @Override
    public List<IChatBaseComponent> getSiblings() {
        return this.deepConverted().getSiblings();
    }

    @Override
    public IChatMutableComponent g() {
        return this.deepConverted().g();
    }

    @Override
    public IChatMutableComponent mutableCopy() {
        return this.deepConverted().mutableCopy();
    }

    public static class Serializer implements JsonSerializer<AdventureComponent> {
        @Override
        public JsonElement serialize(final AdventureComponent src, final Type type, final JsonSerializationContext context) {
            return PaperAdventure.GSON.serializer().toJsonTree(src.wrapped, Component.class);
        }
    }
}

package io.papermc.paper.adventure;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.util.AttributeKey;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.util.Codec;
import net.minecraft.EnumChatFormat;
import net.minecraft.locale.LocaleLanguage;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.world.BossBattle;
import net.minecraft.world.item.ItemStack;
import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PaperAdventure {
    public static final AttributeKey<Locale> LOCALE_ATTRIBUTE = AttributeKey.valueOf("adventure:locale");
    private static final Pattern LOCALIZATION_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?s");
    public static final ComponentFlattener FLATTENER = ComponentFlattener.basic().toBuilder()
        .complexMapper(TranslatableComponent.class, (translatable, consumer) -> {
            final @NonNull String translated = LocaleLanguage.a().a(translatable.key());

            final Matcher matcher = LOCALIZATION_PATTERN.matcher(translated);
            final List<Component> args = translatable.args();
            int argPosition = 0;
            int lastIdx = 0;
            while (matcher.find()) {
                // append prior
                if (lastIdx < matcher.start()) {
                    consumer.accept(Component.text(translated.substring(lastIdx, matcher.start())));
                }
                lastIdx = matcher.end();

                final @Nullable String argIdx = matcher.group(1);
                // calculate argument position
                if (argIdx != null) {
                    try {
                        final int idx = Integer.parseInt(argIdx) - 1;
                        if (idx < args.size()) {
                            consumer.accept(args.get(idx));
                        }
                    } catch (final NumberFormatException ex) {
                        // ignore, drop the format placeholder
                    }
                } else {
                    final int idx = argPosition++;
                    if (idx < args.size()) {
                        consumer.accept(args.get(idx));
                    }
                }
            }

            // append tail
            if (lastIdx < translated.length()) {
                consumer.accept(Component.text(translated.substring(lastIdx)));
            }
        })
        .build();
    public static final LegacyComponentSerializer LEGACY_SECTION_UXRC = LegacyComponentSerializer.builder().flattener(FLATTENER).hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    public static final LegacyComponentSerializer LEGACY_AMPERSAND = LegacyComponentSerializer.builder().character(LegacyComponentSerializer.AMPERSAND_CHAR).hexColors().build(); // Purpur
    public static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.builder().flattener(FLATTENER).build();
    public static final GsonComponentSerializer GSON = GsonComponentSerializer.builder()
        .legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.INSTANCE)
        .build();
    public static final GsonComponentSerializer COLOR_DOWNSAMPLING_GSON = GsonComponentSerializer.builder()
        .legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.INSTANCE)
        .downsampleColors()
        .build();
    private static final Codec<NBTTagCompound, String, IOException, IOException> NBT_CODEC = new Codec<>() {
        @Override
        public @NonNull NBTTagCompound decode(final @NonNull String encoded) throws IOException {
            try {
                return MojangsonParser.parse(encoded);
            } catch (final CommandSyntaxException e) {
                throw new IOException(e);
            }
        }

        @Override
        public @NonNull String encode(final @NonNull NBTTagCompound decoded) {
            return decoded.toString();
        }
    };
    static final WrapperAwareSerializer WRAPPER_AWARE_SERIALIZER = new WrapperAwareSerializer();

    private PaperAdventure() {
    }

    // Key

    public static MinecraftKey asVanilla(final Key key) {
        return new MinecraftKey(key.namespace(), key.value());
    }

    public static MinecraftKey asVanillaNullable(final Key key) {
        if (key == null) {
            return null;
        }
        return new MinecraftKey(key.namespace(), key.value());
    }

    // Component

    public static Component asAdventure(final IChatBaseComponent component) {
        return component == null ? Component.empty() : GSON.serializer().fromJson(IChatBaseComponent.ChatSerializer.toJsonTree(component), Component.class);
    }

    public static ArrayList<Component> asAdventure(final List<IChatBaseComponent> vanillas) {
        final ArrayList<Component> adventures = new ArrayList<>(vanillas.size());
        for (final IChatBaseComponent vanilla : vanillas) {
            adventures.add(asAdventure(vanilla));
        }
        return adventures;
    }

    public static ArrayList<Component> asAdventureFromJson(final List<String> jsonStrings) {
        final ArrayList<Component> adventures = new ArrayList<>(jsonStrings.size());
        for (final String json : jsonStrings) {
            adventures.add(GsonComponentSerializer.gson().deserialize(json));
        }
        return adventures;
    }

    public static List<String> asJson(final List<Component> adventures) {
        final List<String> jsons = new ObjectArrayList<>(adventures.size());
        for (final Component component : adventures) {
            jsons.add(GsonComponentSerializer.gson().serialize(component));
        }
        return jsons;
    }

    public static IChatBaseComponent asVanilla(final Component component) {
        return new AdventureComponent(component);
    }

    public static List<IChatBaseComponent> asVanilla(final List<Component> adventures) {
        final List<IChatBaseComponent> vanillas = new ObjectArrayList<>(adventures.size());
        for (final Component adventure : adventures) {
            vanillas.add(asVanilla(adventure));
        }
        return vanillas;
    }

    public static String asJsonString(final Component component, final Locale locale) {
        return GSON.serialize(
            GlobalTranslator.render(
                component,
                // play it safe
                locale != null
                    ? locale
                    : Locale.US
            )
        );
    }

    public static String asJsonString(final IChatBaseComponent component, final Locale locale) {
        if (component instanceof AdventureComponent) {
            return asJsonString(((AdventureComponent) component).wrapped, locale);
        }
        return IChatBaseComponent.ChatSerializer.componentToJson(component);
    }

    // thank you for being worse than wet socks, Bukkit
    public static String superHackyLegacyRepresentationOfComponent(final Component component, final String string) {
        return LEGACY_SECTION_UXRC.serialize(component) + ChatColor.getLastColors(string);
    }

    // BossBar

    public static BossBattle.BarColor asVanilla(final BossBar.Color color) {
        if (color == BossBar.Color.PINK) {
            return BossBattle.BarColor.PINK;
        } else if (color == BossBar.Color.BLUE) {
            return BossBattle.BarColor.BLUE;
        } else if (color == BossBar.Color.RED) {
            return BossBattle.BarColor.RED;
        } else if (color == BossBar.Color.GREEN) {
            return BossBattle.BarColor.GREEN;
        } else if (color == BossBar.Color.YELLOW) {
            return BossBattle.BarColor.YELLOW;
        } else if (color == BossBar.Color.PURPLE) {
            return BossBattle.BarColor.PURPLE;
        } else if (color == BossBar.Color.WHITE) {
            return BossBattle.BarColor.WHITE;
        }
        throw new IllegalArgumentException(color.name());
    }

    public static BossBar.Color asAdventure(final BossBattle.BarColor color) {
        if(color == BossBattle.BarColor.PINK) {
            return BossBar.Color.PINK;
        } else if(color == BossBattle.BarColor.BLUE) {
            return BossBar.Color.BLUE;
        } else if(color == BossBattle.BarColor.RED) {
            return BossBar.Color.RED;
        } else if(color == BossBattle.BarColor.GREEN) {
            return BossBar.Color.GREEN;
        } else if(color == BossBattle.BarColor.YELLOW) {
            return BossBar.Color.YELLOW;
        } else if(color == BossBattle.BarColor.PURPLE) {
            return BossBar.Color.PURPLE;
        } else if(color == BossBattle.BarColor.WHITE) {
            return BossBar.Color.WHITE;
        }
        throw new IllegalArgumentException(color.name());
    }

    public static BossBattle.BarStyle asVanilla(final BossBar.Overlay overlay) {
        if (overlay == BossBar.Overlay.PROGRESS) {
            return BossBattle.BarStyle.PROGRESS;
        } else if (overlay == BossBar.Overlay.NOTCHED_6) {
            return BossBattle.BarStyle.NOTCHED_6;
        } else if (overlay == BossBar.Overlay.NOTCHED_10) {
            return BossBattle.BarStyle.NOTCHED_10;
        } else if (overlay == BossBar.Overlay.NOTCHED_12) {
            return BossBattle.BarStyle.NOTCHED_12;
        } else if (overlay == BossBar.Overlay.NOTCHED_20) {
            return BossBattle.BarStyle.NOTCHED_20;
        }
        throw new IllegalArgumentException(overlay.name());
    }

    public static BossBar.Overlay asAdventure(final BossBattle.BarStyle overlay) {
        if (overlay == BossBattle.BarStyle.PROGRESS) {
            return BossBar.Overlay.PROGRESS;
        } else if (overlay == BossBattle.BarStyle.NOTCHED_6) {
            return BossBar.Overlay.NOTCHED_6;
        } else if (overlay == BossBattle.BarStyle.NOTCHED_10) {
            return BossBar.Overlay.NOTCHED_10;
        } else if (overlay == BossBattle.BarStyle.NOTCHED_12) {
            return BossBar.Overlay.NOTCHED_12;
        } else if (overlay == BossBattle.BarStyle.NOTCHED_20) {
            return BossBar.Overlay.NOTCHED_20;
        }
        throw new IllegalArgumentException(overlay.name());
    }

    public static void setFlag(final BossBar bar, final BossBar.Flag flag, final boolean value) {
        if (value) {
            bar.addFlag(flag);
        } else {
            bar.removeFlag(flag);
        }
    }

    // Book

    public static ItemStack asItemStack(final Book book, final Locale locale) {
        final ItemStack item = new ItemStack(net.minecraft.world.item.Items.WRITTEN_BOOK, 1);
        final NBTTagCompound tag = item.getOrCreateTag();
        tag.setString("title", asJsonString(book.title(), locale));
        tag.setString("author", asJsonString(book.author(), locale));
        final NBTTagList pages = new NBTTagList();
        for (final Component page : book.pages()) {
            pages.add(NBTTagString.create(asJsonString(page, locale)));
        }
        tag.set("pages", pages);
        return item;
    }

    // Sounds

    public static SoundCategory asVanilla(final Sound.Source source) {
        if (source == Sound.Source.MASTER) {
            return SoundCategory.MASTER;
        } else if (source == Sound.Source.MUSIC) {
            return SoundCategory.MUSIC;
        } else if (source == Sound.Source.RECORD) {
            return SoundCategory.RECORDS;
        } else if (source == Sound.Source.WEATHER) {
            return SoundCategory.WEATHER;
        } else if (source == Sound.Source.BLOCK) {
            return SoundCategory.BLOCKS;
        } else if (source == Sound.Source.HOSTILE) {
            return SoundCategory.HOSTILE;
        } else if (source == Sound.Source.NEUTRAL) {
            return SoundCategory.NEUTRAL;
        } else if (source == Sound.Source.PLAYER) {
            return SoundCategory.PLAYERS;
        } else if (source == Sound.Source.AMBIENT) {
            return SoundCategory.AMBIENT;
        } else if (source == Sound.Source.VOICE) {
            return SoundCategory.VOICE;
        }
        throw new IllegalArgumentException(source.name());
    }

    public static @Nullable SoundCategory asVanillaNullable(final Sound.@Nullable Source source) {
        if (source == null) {
            return null;
        }
        return asVanilla(source);
    }

    // NBT

    public static @Nullable BinaryTagHolder asBinaryTagHolder(final @Nullable NBTTagCompound tag) {
        if (tag == null) {
            return null;
        }
        try {
            return BinaryTagHolder.encode(tag, NBT_CODEC);
        } catch (final IOException e) {
            return null;
        }
    }

    // Colors

    public static @NonNull TextColor asAdventure(EnumChatFormat minecraftColor) {
        if (minecraftColor.e() == null) {
            throw new IllegalArgumentException("Not a valid color");
        }
        return TextColor.color(minecraftColor.e());
    }

    public static @Nullable EnumChatFormat asVanilla(TextColor color) {
        return EnumChatFormat.getByHexValue(color.value());
    }
}

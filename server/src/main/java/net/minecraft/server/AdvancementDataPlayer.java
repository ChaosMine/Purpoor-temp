package net.minecraft.server;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.SharedConstants;
import net.minecraft.SystemUtils;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.CriterionTriggerAbstract;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.protocol.game.PacketPlayOutAdvancements;
import net.minecraft.network.protocol.game.PacketPlayOutSelectAdvancementTab;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.GameRules;
import net.pl3x.purpur.PurpurConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancementDataPlayer {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson b = (new GsonBuilder()).registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.a()).registerTypeAdapter(MinecraftKey.class, new MinecraftKey.a()).setPrettyPrinting().create();
    private static final TypeToken<Map<MinecraftKey, AdvancementProgress>> c = new TypeToken<>() {};
    private final DataFixer d;
    private final PlayerList e; public PlayerList getPlayerList() { return e; } // Purpur - OBFHELPER
    private final File f;
    public final Map<Advancement, AdvancementProgress> data;
    private final Set<Advancement> h;
    private final Set<Advancement> i;
    private final Set<Advancement> j;
    {
        if (PurpurConfig.disableCriterions) {
            data = null;
            h = null;
            i = null;
            j = null;
        } else {
            h = Sets.newLinkedHashSet();
            i = Sets.newLinkedHashSet();
            j = Sets.newLinkedHashSet();
            data = Maps.newLinkedHashMap();
        }
    }
    private EntityPlayer player;
    @Nullable
    private Advancement l;
    private boolean m = true;

    // Paper start - fix advancement data player leakage
    public final Map<CriterionTriggerAbstract, Set<CriterionTrigger.a>> criterionData = Maps.newIdentityHashMap();
    // Paper end - fix advancement data player leakage

    public AdvancementDataPlayer(DataFixer datafixer, PlayerList playerlist, AdvancementDataWorld advancementdataworld, File file, EntityPlayer entityplayer) {
        this.d = datafixer;
        this.e = playerlist;
        this.f = file;
        this.player = entityplayer;
        this.d(advancementdataworld);
    }

    public void a(EntityPlayer entityplayer) {
        if (PurpurConfig.disableCriterions) return;
        this.player = entityplayer;
    }

    public void a() {
        if (PurpurConfig.disableCriterions) return;

        for (CriterionTrigger<?> criterionTrigger : CriterionTriggers.a()) {
            CriterionTrigger<?> criteriontrigger = criterionTrigger;

            criteriontrigger.a(this);
        }

    }

    public final void reload(AdvancementDataWorld advancementDataWorld) { this.a(advancementDataWorld); } // Paper - OBFHELPER
    public void a(AdvancementDataWorld advancementdataworld) {
        if (PurpurConfig.disableCriterions) return;
        this.a();
        this.data.clear();
        this.h.clear();
        this.i.clear();
        this.j.clear();
        this.m = true;
        this.l = null;
        this.d(advancementdataworld);
    }

    private void b(AdvancementDataWorld advancementdataworld) {
        if (PurpurConfig.disableCriterions) return;
        Iterator iterator = advancementdataworld.getAdvancements().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.c(advancement);
        }

    }

    private void c() {
        if (PurpurConfig.disableCriterions) return;
        List<Advancement> list = Lists.newArrayList();
        Iterator iterator = this.data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Advancement, AdvancementProgress> entry = (Entry) iterator.next();

            if (entry.getValue().isDone()) {
                list.add(entry.getKey());
                this.j.add(entry.getKey());
            }
        }

        iterator = list.iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.e(advancement);
        }

    }

    private void c(AdvancementDataWorld advancementdataworld) {
        if (PurpurConfig.disableCriterions) return;
        Iterator iterator = advancementdataworld.getAdvancements().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            if (advancement.getCriteria().isEmpty()) {
                this.grantCriteria(advancement, "");
                advancement.d().a(this.player);
            }
        }

    }

    private void d(AdvancementDataWorld advancementdataworld) {
        if (PurpurConfig.disableCriterions) return;
        if (this.f.isFile()) {
            try {
                JsonReader jsonreader = new JsonReader(new StringReader(Files.toString(this.f, StandardCharsets.UTF_8)));
                Throwable throwable = null;

                try {
                    jsonreader.setLenient(false);
                    Dynamic<JsonElement> dynamic = new Dynamic(JsonOps.INSTANCE, Streams.parse(jsonreader));

                    if (!dynamic.get("DataVersion").asNumber().result().isPresent()) {
                        dynamic = dynamic.set("DataVersion", dynamic.createInt(1343));
                    }

                    dynamic = this.d.update(DataFixTypes.ADVANCEMENTS.a(), dynamic, dynamic.get("DataVersion").asInt(0), SharedConstants.getGameVersion().getWorldVersion());
                    dynamic = dynamic.remove("DataVersion");
                    Map<MinecraftKey, AdvancementProgress> map = AdvancementDataPlayer.b.getAdapter(AdvancementDataPlayer.c).fromJsonTree(dynamic.getValue());

                    if (map == null) {
                        throw new JsonParseException("Found null for advancements");
                    }

                    Stream<Entry<MinecraftKey, AdvancementProgress>> stream = map.entrySet().stream().sorted(Comparator.comparing(Entry::getValue));
                    Iterator iterator = ((List) stream.toList()).iterator();

                    while (iterator.hasNext()) {
                        Entry<MinecraftKey, AdvancementProgress> entry = (Entry) iterator.next();
                        Advancement advancement = advancementdataworld.a(entry.getKey());

                        if (advancement == null) {
                            // CraftBukkit start
                            if (entry.getKey().getNamespace().equals("minecraft")) {
                                if (!net.pl3x.purpur.PurpurConfig.loggerSuppressIgnoredAdvancementWarnings) // Purpur
                                AdvancementDataPlayer.LOGGER.warn("Ignored advancement '{}' in progress file {} - it doesn't exist anymore?", entry.getKey(), this.f);
                            }
                            // CraftBukkit end
                        } else {
                            this.a(advancement, entry.getValue());
                        }
                    }
                } catch (Throwable throwable1) {
                    throwable = throwable1;
                    throw throwable1;
                } finally {
                    if (jsonreader != null) {
                        if (throwable != null) {
                            try {
                                jsonreader.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        } else {
                            jsonreader.close();
                        }
                    }

                }
            } catch (JsonParseException jsonparseexception) {
                AdvancementDataPlayer.LOGGER.error("Couldn't parse player advancements in {}", this.f, jsonparseexception);
            } catch (IOException ioexception) {
                AdvancementDataPlayer.LOGGER.error("Couldn't access player advancements in {}", this.f, ioexception);
            }
        }

        this.c(advancementdataworld);
        this.c();
        this.b(advancementdataworld);
    }

    public void b() {
        if (PurpurConfig.disableCriterions) return;
        if (org.spigotmc.SpigotConfig.disableAdvancementSaving) return; // Spigot
        Map<MinecraftKey, AdvancementProgress> map = Maps.newHashMap();
        Iterator iterator = this.data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Advancement, AdvancementProgress> entry = (Entry) iterator.next();
            AdvancementProgress advancementprogress = entry.getValue();

            if (advancementprogress.b()) {
                map.put(entry.getKey().getName(), advancementprogress);
            }
        }

        if (this.f.getParentFile() != null) {
            this.f.getParentFile().mkdirs();
        }

        JsonElement jsonelement = AdvancementDataPlayer.b.toJsonTree(map);

        jsonelement.getAsJsonObject().addProperty("DataVersion", SharedConstants.getGameVersion().getWorldVersion());

        try {
            FileOutputStream fileoutputstream = new FileOutputStream(this.f);
            Throwable throwable = null;

            try {
                OutputStreamWriter outputstreamwriter = new OutputStreamWriter(fileoutputstream, Charsets.UTF_8.newEncoder());
                Throwable throwable1 = null;

                try {
                    AdvancementDataPlayer.b.toJson(jsonelement, outputstreamwriter);
                } catch (Throwable throwable2) {
                    throwable1 = throwable2;
                    throw throwable2;
                } finally {
                    if (outputstreamwriter != null) {
                        if (throwable1 != null) {
                            try {
                                outputstreamwriter.close();
                            } catch (Throwable throwable3) {
                                throwable1.addSuppressed(throwable3);
                            }
                        } else {
                            outputstreamwriter.close();
                        }
                    }

                }
            } catch (Throwable throwable4) {
                throwable = throwable4;
                throw throwable4;
            } finally {
                if (fileoutputstream != null) {
                    if (throwable != null) {
                        try {
                            fileoutputstream.close();
                        } catch (Throwable throwable5) {
                            throwable.addSuppressed(throwable5);
                        }
                    } else {
                        fileoutputstream.close();
                    }
                }

            }
        } catch (IOException ioexception) {
            AdvancementDataPlayer.LOGGER.error("Couldn't save player advancements to {}", this.f, ioexception);
        }

    }

    public boolean grantCriteria(Advancement advancement, String s) {
        if (PurpurConfig.disableCriterions) return false;
        boolean flag = false;
        AdvancementProgress advancementprogress = this.getProgress(advancement);
        boolean flag1 = advancementprogress.isDone();

        if (advancementprogress.a(s)) {
            // Paper start
            if (!new com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent(this.player.getBukkitEntity(), advancement.bukkit, s).callEvent()) {
                advancementprogress.b(s);
                return false;
            }
            // Paper end
            this.d(advancement);
            this.j.add(advancement);
            flag = true;
            if (!flag1 && advancementprogress.isDone()) {
                // Paper start - Add Adventure message to PlayerAdvancementDoneEvent
                boolean announceToChat = advancement.getAdvancementDisplay() != null && advancement.getAdvancementDisplay().shouldAnnounceToChat();
                net.kyori.adventure.text.Component message = announceToChat ? PaperAdventure.asAdventure(new ChatMessage("chat.type.advancement." + advancement.getAdvancementDisplay().getFrameType().getId(), this.player.getScoreboardDisplayName(), advancement.getChatComponent())) : null;
                org.bukkit.event.player.PlayerAdvancementDoneEvent event = new org.bukkit.event.player.PlayerAdvancementDoneEvent(this.player.getBukkitEntity(), advancement.bukkit, message);
                this.player.world.getServer().getPluginManager().callEvent(event);
                message = event.message();
                // Paper end
                advancement.d().a(this.player);
                // Paper start - Add Adventure message to PlayerAdvancementDoneEvent
                if (message != null && this.player.world.getGameRules().getBoolean(GameRules.ANNOUNCE_ADVANCEMENTS)) {
                    // Purpur Start - AdvancementMessage By Player
                    if (net.pl3x.purpur.PurpurConfig.advancementOnlyBroadcastToAffectedPlayer) {
                        this.player.sendMessage(PaperAdventure.asVanilla(message), SystemUtils.getNullUUID());
                    } else {
                        getPlayerList().sendMessage(PaperAdventure.asVanilla(message), ChatMessageType.SYSTEM, SystemUtils.getNullUUID());
                    }
                    // Purpur End
                    // Paper end
                }
            }
        }

        if (advancementprogress.isDone()) {
            this.e(advancement);
        }

        return flag;
    }

    public boolean revokeCritera(Advancement advancement, String s) {
        if (PurpurConfig.disableCriterions) return false;
        boolean flag = false;
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (advancementprogress.b(s)) {
            this.c(advancement);
            this.j.add(advancement);
            flag = true;
        }

        if (!advancementprogress.b()) {
            this.e(advancement);
        }

        return flag;
    }

    private void c(Advancement advancement) {
        if (PurpurConfig.disableCriterions) return;
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (!advancementprogress.isDone()) {
            Iterator iterator = advancement.getCriteria().entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<String, Criterion> entry = (Entry) iterator.next();
                CriterionProgress criterionprogress = advancementprogress.getCriterionProgress(entry.getKey());

                if (criterionprogress != null && !criterionprogress.a()) {
                    CriterionInstance criterioninstance = entry.getValue().a();

                    if (criterioninstance != null) {
                        CriterionTrigger<CriterionInstance> criteriontrigger = CriterionTriggers.a(criterioninstance.a());

                        if (criteriontrigger != null) {
                            criteriontrigger.a(this, new CriterionTrigger.a<>(criterioninstance, advancement, entry.getKey()));
                        }
                    }
                }
            }

        }
    }

    private void d(Advancement advancement) {
        if (PurpurConfig.disableCriterions) return;
        AdvancementProgress advancementprogress = this.getProgress(advancement);
        Iterator iterator = advancement.getCriteria().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, Criterion> entry = (Entry) iterator.next();
            CriterionProgress criterionprogress = advancementprogress.getCriterionProgress(entry.getKey());

            if (criterionprogress != null && (criterionprogress.a() || advancementprogress.isDone())) {
                CriterionInstance criterioninstance = entry.getValue().a();

                if (criterioninstance != null) {
                    CriterionTrigger<CriterionInstance> criteriontrigger = CriterionTriggers.a(criterioninstance.a());

                    if (criteriontrigger != null) {
                        criteriontrigger.b(this, new CriterionTrigger.a<>(criterioninstance, advancement, entry.getKey()));
                    }
                }
            }
        }

    }

    public final void sendUpdateIfNeeded(EntityPlayer entityPlayer) { this.b(entityPlayer); } // Paper - OBFHELPER
    public void b(EntityPlayer entityplayer) {
        if (PurpurConfig.disableCriterions) return;
        if (this.m || !this.i.isEmpty() || !this.j.isEmpty()) {
            Map<MinecraftKey, AdvancementProgress> map = Maps.newHashMap();
            Set<Advancement> set = Sets.newLinkedHashSet();
            Set<MinecraftKey> set1 = Sets.newLinkedHashSet();
            Iterator iterator = this.j.iterator();

            Advancement advancement;

            while (iterator.hasNext()) {
                advancement = (Advancement) iterator.next();
                if (this.h.contains(advancement)) {
                    map.put(advancement.getName(), this.data.get(advancement));
                }
            }

            iterator = this.i.iterator();

            while (iterator.hasNext()) {
                advancement = (Advancement) iterator.next();
                if (this.h.contains(advancement)) {
                    set.add(advancement);
                } else {
                    set1.add(advancement.getName());
                }
            }

            if (this.m || !map.isEmpty() || !set.isEmpty() || !set1.isEmpty()) {
                entityplayer.playerConnection.sendPacket(new PacketPlayOutAdvancements(this.m, set, set1, map));
                this.i.clear();
                this.j.clear();
            }
        }

        this.m = false;
    }

    public void a(@Nullable Advancement advancement) {
        if (PurpurConfig.disableCriterions) return;
        Advancement advancement1 = this.l;

        if (advancement != null && advancement.b() == null && advancement.c() != null) {
            this.l = advancement;
        } else {
            this.l = null;
        }

        if (advancement1 != this.l) {
            this.player.playerConnection.sendPacket(new PacketPlayOutSelectAdvancementTab(this.l == null ? null : this.l.getName()));
        }

    }

    private static final AdvancementProgress EMPTY_PROGRESS = new AdvancementProgress();

    public AdvancementProgress getProgress(Advancement advancement) {
        if (PurpurConfig.disableCriterions) return EMPTY_PROGRESS;
        AdvancementProgress advancementprogress = this.data.get(advancement);

        if (advancementprogress == null) {
            advancementprogress = new AdvancementProgress();
            this.a(advancement, advancementprogress);
        }

        return advancementprogress;
    }

    private void a(Advancement advancement, AdvancementProgress advancementprogress) {
        if (PurpurConfig.disableCriterions) return;
        advancementprogress.a(advancement.getCriteria(), advancement.i());
        this.data.put(advancement, advancementprogress);
    }

    private void e(Advancement advancement) {
        if (PurpurConfig.disableCriterions) return;
        // Paper start
        e(advancement, IterationEntryPoint.ROOT);
    }
    private enum IterationEntryPoint {
        ROOT,
        ITERATOR,
        PARENT_OF_ITERATOR
    }
    private void e(Advancement advancement, IterationEntryPoint entryPoint) {
        if (PurpurConfig.disableCriterions) return;
        // Paper end
        boolean flag = this.f(advancement);
        boolean flag1 = this.h.contains(advancement);

        if (flag && !flag1) {
            this.h.add(advancement);
            this.i.add(advancement);
            if (this.data.containsKey(advancement)) {
                this.j.add(advancement);
            }
        } else if (!flag && flag1) {
            this.h.remove(advancement);
            this.i.add(advancement);
        }

        if (flag != flag1 && advancement.b() != null) {
            // Paper start - If we're not coming from an iterator consider this to be a root entry, otherwise
            // market that we're entering from the parent of an iterator.
            this.e(advancement.b(), entryPoint == IterationEntryPoint.ITERATOR ? IterationEntryPoint.PARENT_OF_ITERATOR : IterationEntryPoint.ROOT);
        }

        // If this is true, we've went through a child iteration, entered the parent, processed the parent
        // and are about to reprocess the children. Stop processing here to prevent O(N^2) processing.
        if (entryPoint == IterationEntryPoint.PARENT_OF_ITERATOR) {
            return;
        } // Paper end

        for (Advancement advancement1 : advancement.e()) {
            this.e(advancement1, IterationEntryPoint.ITERATOR); // Paper - Mark this call as being from iteration
        }

    }

    private boolean f(Advancement advancement) {
        if (PurpurConfig.disableCriterions) return false;
        for (int i = 0; advancement != null && i <= 2; ++i) {
            if (i == 0 && this.g(advancement)) {
                return true;
            }

            if (advancement.c() == null) {
                return false;
            }

            AdvancementProgress advancementprogress = this.getProgress(advancement);

            if (advancementprogress.isDone()) {
                return true;
            }

            if (advancement.c().j()) {
                return false;
            }

            advancement = advancement.b();
        }

        return false;
    }

    private boolean g(Advancement advancement) {
        if (PurpurConfig.disableCriterions) return false;
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (advancementprogress.isDone()) {
            return true;
        } else {
            Iterator iterator = advancement.e().iterator();

            Advancement advancement1;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                advancement1 = (Advancement) iterator.next();
            } while (!this.g(advancement1));

            return true;
        }
    }
}

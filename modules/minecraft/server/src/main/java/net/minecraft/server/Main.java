package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import io.papermc.paper.world.ThreadedWorldUpgrader;
import joptsimple.OptionSet;
import net.minecraft.CrashReport;
import net.minecraft.SharedConstants;
import net.minecraft.SystemUtils;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.server.level.progress.WorldLoadListenerLogger;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.ResourcePackRepository;
import net.minecraft.server.packs.repository.ResourcePackSourceFolder;
import net.minecraft.server.packs.repository.ResourcePackSourceVanilla;
import net.minecraft.server.players.UserCache;
import net.minecraft.util.MathHelper;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.worldupdate.WorldUpgrader;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.DataPackConfiguration;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.dimension.WorldDimension;
import net.minecraft.world.level.storage.Convertable;
import net.minecraft.world.level.storage.SavedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.Proxy;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
// CraftBukkit end

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public Main() {}

    public static void main(final OptionSet optionset) { // CraftBukkit - replaces main(String[] astring)
        try {
            CrashReport.h();
            DispenserRegistry.init();
            DispenserRegistry.c();
            IRegistryCustom.Dimension iregistrycustom_dimension = IRegistryCustom.b();
            java.nio.file.Path java_nio_file_path = Paths.get("server.properties");
            DedicatedServerSettings dedicatedserversettings = new DedicatedServerSettings(iregistrycustom_dimension, optionset); // CraftBukkit - CLI argument support

            dedicatedserversettings.save();
            // Paper start - load config files for access below if needed
            org.bukkit.configuration.file.YamlConfiguration bukkitConfiguration = loadConfigFile((File) optionset.valueOf("bukkit-settings"));
            org.bukkit.configuration.file.YamlConfiguration spigotConfiguration = loadConfigFile((File) optionset.valueOf("spigot-settings"));
            org.bukkit.configuration.file.YamlConfiguration paperConfiguration = loadConfigFile((File) optionset.valueOf("paper-settings"));
            // Paper end

            java.nio.file.Path java_nio_file_path1 = Paths.get("eula.txt");
            if (optionset.has("initSettings")) { // CraftBukkit
                Main.LOGGER.info("Initialized '{}' and '{}'", java_nio_file_path.toAbsolutePath(), java_nio_file_path1.toAbsolutePath());
                return;
            }

            org.spigotmc.SpigotConfig.disabledAdvancements = spigotConfiguration.getStringList("advancements.disabled"); // Paper - fix SPIGOT-5885, must be set early in init
            // Paper start - fix SPIGOT-5824
            File file;
            if (optionset.has("universe")) {
                file = (File) optionset.valueOf("universe"); // CraftBukkit
            } else {
                file = new File(bukkitConfiguration.getString("settings.world-container", "."));
            }
            // Paper end - fix SPIGOT-5824
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new com.destroystokyo.paper.profile.PaperAuthenticationService(Proxy.NO_PROXY); // Paper
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            UserCache usercache = new UserCache(); // Paper - only move usercache.json into folder if --universe is used, not world-container
            // CraftBukkit start
            String s = (String) Optional.ofNullable(optionset.valueOf("world")).orElse(dedicatedserversettings.getProperties().levelName);
            Convertable convertable = Convertable.a(file.toPath());
            Convertable.ConversionSession convertable_conversionsession = convertable.c(s, WorldDimension.OVERWORLD);

            MinecraftServer.convertWorld(convertable_conversionsession);
            DataPackConfiguration datapackconfiguration = convertable_conversionsession.e();
            boolean flag = optionset.has("safeMode");

            if (flag) {
                Main.LOGGER.warn("Safe mode active, only vanilla datapack will be loaded");
            }

            ResourcePackRepository resourcepackrepository = new ResourcePackRepository(new ResourcePackSourceVanilla(), new ResourcePackSourceFolder(convertable_conversionsession.getWorldFolder(SavedFile.DATAPACKS).toFile(), PackSource.c));
            // CraftBukkit start
            File bukkitDataPackFolder = new File(convertable_conversionsession.getWorldFolder(SavedFile.DATAPACKS).toFile(), "bukkit");
            if (!bukkitDataPackFolder.exists()) {
                bukkitDataPackFolder.mkdirs();
            }
            File mcMeta = new File(bukkitDataPackFolder, "pack.mcmeta");
            try {
                com.google.common.io.Files.write("{\n"
                        + "    \"pack\": {\n"
                        + "        \"description\": \"Data pack for resources provided by Bukkit plugins\",\n"
                        + "        \"pack_format\": " + SharedConstants.getGameVersion().getPackVersion() + "\n"
                        + "    }\n"
                        + "}\n", mcMeta, com.google.common.base.Charsets.UTF_8);
            } catch (java.io.IOException ex) {
                throw new RuntimeException("Could not initialize Bukkit datapack", ex);
            }
            // CraftBukkit end
            DataPackConfiguration datapackconfiguration1 = MinecraftServer.a(resourcepackrepository, datapackconfiguration == null ? DataPackConfiguration.a : datapackconfiguration, flag);
            CompletableFuture completablefuture = DataPackResources.a(resourcepackrepository.f(), CommandDispatcher.ServerType.DEDICATED, dedicatedserversettings.getProperties().functionPermissionLevel, SystemUtils.f(), Runnable::run);

            DataPackResources datapackresources;

            try {
                datapackresources = (DataPackResources) completablefuture.get();
            } catch (Exception exception) {
                Main.LOGGER.warn("Failed to load datapacks, can't proceed with server load. You can either fix your datapacks or reset to vanilla with --safeMode", exception);
                resourcepackrepository.close();
                return;
            }

            datapackresources.i();
            /*
            RegistryReadOps<NBTBase> registryreadops = RegistryReadOps.a((DynamicOps) DynamicOpsNBT.a, datapackresources.h(), iregistrycustom_dimension);
            Object object = convertable_conversionsession.a((DynamicOps) registryreadops, datapackconfiguration1);

            if (object == null) {
                WorldSettings worldsettings;
                GeneratorSettings generatorsettings;

                if (optionset.has(optionspec2)) {
                    worldsettings = MinecraftServer.c;
                    generatorsettings = GeneratorSettings.a((IRegistryCustom) iregistrycustom_dimension);
                } else {
                    DedicatedServerProperties dedicatedserverproperties = dedicatedserversettings.getProperties();

                    worldsettings = new WorldSettings(dedicatedserverproperties.levelName, dedicatedserverproperties.gamemode, dedicatedserverproperties.hardcore, dedicatedserverproperties.difficulty, false, new GameRules(), datapackconfiguration1);
                    generatorsettings = optionset.has(optionspec3) ? dedicatedserverproperties.generatorSettings.j() : dedicatedserverproperties.generatorSettings;
                }

                object = new WorldDataServer(worldsettings, generatorsettings, Lifecycle.stable());
            }

            if (optionset.has(optionspec4)) {
                convertWorld(convertable_conversionsession, DataConverterRegistry.a(), optionset.has(optionspec5), () -> {
                    return true;
                }, ((SaveData) object).getGeneratorSettings().f());
            }

            convertable_conversionsession.a((IRegistryCustom) iregistrycustom_dimension, (SaveData) object);
            */
            Class.forName(VillagerTrades.class.getName());// Paper - load this sync so it won't fail later async
            MinecraftServer.a((thread) -> {
                DedicatedServer dedicatedserver1 = new DedicatedServer(
                  optionset,
                  datapackconfiguration1,
                  thread,
                  iregistrycustom_dimension,
                  convertable_conversionsession,
                  resourcepackrepository,
                  datapackresources,
                  null,
                  dedicatedserversettings,
                  DataConverterRegistry.a(),
                  minecraftsessionservice,
                  gameprofilerepository,
                  usercache,
                  WorldLoadListenerLogger::new
                );

                if (optionset.has("port")) {
                    int port = (Integer) optionset.valueOf("port");
                    if (port > 0) {
                        dedicatedserver1.setPort(port);
                    }
                }

                return dedicatedserver1;
            });
        } catch (Exception exception1) {
            Main.LOGGER.fatal("Failed to start the minecraft server", exception1);
        }
    }

    // Paper start - load config files
    private static org.bukkit.configuration.file.YamlConfiguration loadConfigFile(File configFile) throws Exception {
        org.bukkit.configuration.file.YamlConfiguration config = new org.bukkit.configuration.file.YamlConfiguration();
        if (configFile.exists()) {
            try {
                config.load(configFile);
            } catch (Exception ex) {
                throw new Exception("Failed to load configuration file: " + configFile.getName(), ex);
            }
        }
        return config;
    }
    // Paper end

    // Paper start - fix and optimise world upgrading
    public static void convertWorldButItWorks(ResourceKey<WorldDimension> dimensionType, ResourceKey<DimensionManager> worldKey, String worldName,
                                              DataFixer dataFixer, boolean removeCaches) {
        int threads = Runtime.getRuntime().availableProcessors() * 3 / 8;
        final ThreadedWorldUpgrader worldUpgrader = new ThreadedWorldUpgrader(dimensionType, worldKey, worldName, threads, dataFixer, removeCaches);
        worldUpgrader.convert();
    }
    // Paper end - fix and optimise world upgrading

    public static void convertWorld(Convertable.ConversionSession convertable_conversionsession, DataFixer datafixer, boolean flag, BooleanSupplier booleansupplier, ImmutableSet<ResourceKey<DimensionManager>> immutableset) { // CraftBukkit
        Main.LOGGER.info("Forcing world upgrade! {}", convertable_conversionsession.getLevelName()); // CraftBukkit
        WorldUpgrader worldupgrader = new WorldUpgrader(convertable_conversionsession, datafixer, immutableset, flag);
        IChatBaseComponent ichatbasecomponent = null;

        while (!worldupgrader.b()) {
            IChatBaseComponent ichatbasecomponent1 = worldupgrader.h();

            if (ichatbasecomponent != ichatbasecomponent1) {
                ichatbasecomponent = ichatbasecomponent1;
                Main.LOGGER.info(worldupgrader.h().getString());
            }

            int i = worldupgrader.e();

            if (i > 0) {
                int j = worldupgrader.f() + worldupgrader.g();

                Main.LOGGER.info("{}% completed ({} / {} chunks)...", MathHelper.d((float) j / (float) i * 100.0F), j, i);
            }

            if (!booleansupplier.getAsBoolean()) {
                worldupgrader.a();
            } else {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException interruptedexception) {
                    ;
                }
            }
        }

    }
}

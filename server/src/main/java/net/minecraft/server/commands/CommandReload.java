package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.ResourcePackRepository;
import net.minecraft.world.level.storage.SaveData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;

public class CommandReload {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void a(Collection<String> collection, CommandListenerWrapper commandlistenerwrapper) {
        commandlistenerwrapper.getServer().reloadServerResources(collection, ServerResourcesReloadedEvent.Cause.COMMAND).exceptionally((throwable) -> { // Paper
            CommandReload.LOGGER.warn("Failed to execute reload", throwable);
            commandlistenerwrapper.sendFailureMessage(new ChatMessage("commands.reload.failure"));
            return null;
        });
    }

    private static Collection<String> a(ResourcePackRepository resourcepackrepository, SaveData savedata, Collection<String> collection) {
        resourcepackrepository.a();
        Collection<String> collection1 = Lists.newArrayList(collection);
        Collection<String> collection2 = savedata.D().b();
        Iterator iterator = resourcepackrepository.b().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (!collection2.contains(s) && !collection1.contains(s)) {
                collection1.add(s);
            }
        }

        return collection1;
    }

    // CraftBukkit start
    public static void reload(MinecraftServer minecraftserver) {
        ResourcePackRepository resourcepackrepository = minecraftserver.getResourcePackRepository();
        SaveData savedata = minecraftserver.getSaveData();
        Collection<String> collection = resourcepackrepository.d();
        Collection<String> collection1 = a(resourcepackrepository, savedata, collection);
        minecraftserver.reloadServerResources(collection1, ServerResourcesReloadedEvent.Cause.PLUGIN); // Paper
    }
    // CraftBukkit end

    public static void a(CommandDispatcher<CommandListenerWrapper> commanddispatcher) {
        commanddispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) net.minecraft.commands.CommandDispatcher.a("reload").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        })).executes((commandcontext) -> {
            CommandListenerWrapper commandlistenerwrapper = (CommandListenerWrapper) commandcontext.getSource();
            MinecraftServer minecraftserver = commandlistenerwrapper.getServer();
            ResourcePackRepository resourcepackrepository = minecraftserver.getResourcePackRepository();
            SaveData savedata = minecraftserver.getSaveData();
            Collection<String> collection = resourcepackrepository.d();
            Collection<String> collection1 = a(resourcepackrepository, savedata, collection);

            commandlistenerwrapper.sendMessage(new ChatMessage("commands.reload.success"), true);
            a(collection1, commandlistenerwrapper);
            return 0;
        }));
    }
}

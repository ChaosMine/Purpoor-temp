package org.bukkit.craftbukkit.command;

import com.google.common.base.Joiner;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.tree.CommandNode;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import java.util.Collections;
import java.util.List;

public final class VanillaCommandWrapper extends BukkitCommand {

    private final CommandDispatcher dispatcher;
    public final CommandNode<CommandListenerWrapper> vanillaCommand;

    public VanillaCommandWrapper(CommandDispatcher dispatcher, CommandNode<CommandListenerWrapper> vanillaCommand) {
        super(vanillaCommand.getName(), "A Mojang provided command.", vanillaCommand.getUsageText(), Collections.EMPTY_LIST);
        this.dispatcher = dispatcher;
        this.vanillaCommand = vanillaCommand;
        this.setPermission(getPermission(vanillaCommand));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        CommandListenerWrapper icommandlistener = getListener(sender);
        dispatcher.a(icommandlistener, toDispatcher(args, getName()), toDispatcher(args, commandLabel), true);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        CommandListenerWrapper icommandlistener = getListener(sender);
        ParseResults<CommandListenerWrapper> parsed = dispatcher.a().parse(toDispatcher(args, getName()), icommandlistener);

        List<String> results = new ObjectArrayList<>();
        dispatcher.a().getCompletionSuggestions(parsed).thenAccept((suggestions) -> {
            suggestions.getList().forEach((s) -> results.add(s.getText()));
        });

        return results;
    }

    public static CommandListenerWrapper getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle().getCommandListener();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getWrapper();
        }
        if (sender instanceof CommandMinecart) {
            return ((CraftMinecartCommand) sender).getHandle().getCommandBlock().getWrapper();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            throw new UnsupportedOperationException("RemoteConsoleCommandSender is unsupported");
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer().getServerCommandListener();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }

        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    public static String getPermission(CommandNode<CommandListenerWrapper> vanillaCommand) {
        // Paper start
        final String commandName;
        if (vanillaCommand.getRedirect() == null) {
            commandName = vanillaCommand.getName();
        } else {
            commandName = vanillaCommand.getRedirect().getName();
        }
        if (vanillaCommand.getPermission() != null) return vanillaCommand.getPermission(); // Purpur
        return "minecraft.command." + stripDefaultNamespace(commandName);
    }

    private static String stripDefaultNamespace(final String maybeNamespaced) {
        final String prefix = "minecraft:";
        if (maybeNamespaced.startsWith(prefix)) {
            return maybeNamespaced.substring(prefix.length());
        }
        return maybeNamespaced;
        // Paper end
    }

    private String toDispatcher(String[] args, String name) {
        return name + ((args.length > 0) ? " " + Joiner.on(' ').join(args) : "");
    }
}

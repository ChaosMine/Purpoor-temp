package net.pl3x.purpur.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.pl3x.purpur.PurpurConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PurpurCommand extends Command {
    public PurpurCommand(String name) {
        super(name);
        this.description = "Purpur related commands";
        this.usageMessage = "/purpur [reload | version]";
        this.setPermission("bukkit.command.purpur");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length == 1) {
            return Stream.of("reload", "version")
                    .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Command.broadcastCommandMessage(sender, ChatColor.RED + "Please note that this command is not supported and may cause issues.");
            Command.broadcastCommandMessage(sender, ChatColor.RED + "If you encounter any issues please use the /stop command to restart your server.");

            MinecraftServer console = MinecraftServer.SERVER;
            PurpurConfig.init((File) console.options.valueOf("purpur-settings"));
            for (WorldServer world : console.getWorlds()) {
                world.purpurConfig.init();
                world.resetBreedingCooldowns();
            }
            console.server.reloadCount++;

            Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Purpur config reload complete.");
        } else if (args[0].equalsIgnoreCase("version")) {
            Command verCmd = org.bukkit.Bukkit.getServer().getCommandMap().getCommand("version");
            if (verCmd != null) {
                return verCmd.execute(sender, commandLabel, new String[0]);
            }
        }

        return true;
    }
}

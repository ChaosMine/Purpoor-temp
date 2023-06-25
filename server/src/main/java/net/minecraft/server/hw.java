package net.minecraft.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.synchronization.ArgumentRegistry;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;

public class hw implements DebugReportProvider {

    private static final Gson b = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final DebugReportGenerator c;

    public hw(DebugReportGenerator debugreportgenerator) {
        this.c = debugreportgenerator;
    }

    @Override
    public void a(HashCache hashcache) throws IOException {
        Path path = this.c.b().resolve("reports/commands.json");
        CommandDispatcher<CommandListenerWrapper> commanddispatcher = (new net.minecraft.commands.CommandDispatcher(net.minecraft.commands.CommandDispatcher.ServerType.ALL)).a();

        DebugReportProvider.a(hw.b, hashcache, ArgumentRegistry.a(commanddispatcher, (CommandNode) commanddispatcher.getRoot()), path);
    }

    @Override
    public String a() {
        return "Command Syntax";
    }
}

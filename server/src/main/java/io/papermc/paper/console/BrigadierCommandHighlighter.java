package io.papermc.paper.console;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.dedicated.DedicatedServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jline.reader.Highlighter;
import org.jline.reader.LineReader;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.regex.Pattern;

public final class BrigadierCommandHighlighter implements Highlighter {
    private static final int[] COLORS = {AttributedStyle.CYAN, AttributedStyle.YELLOW, AttributedStyle.GREEN, AttributedStyle.MAGENTA, /* Client uses GOLD here, not BLUE, however there is no GOLD AttributedStyle. */ AttributedStyle.BLUE};
    private final CommandListenerWrapper commandSourceStack;
    private final DedicatedServer server;

    public BrigadierCommandHighlighter(final @NonNull DedicatedServer server, final @NonNull CommandListenerWrapper commandSourceStack) {
        this.server = server;
        this.commandSourceStack = commandSourceStack;
    }

    @Override
    public AttributedString highlight(final @NonNull LineReader reader, final @NonNull String buffer) {
        final AttributedStringBuilder builder = new AttributedStringBuilder();
        final ParseResults<CommandListenerWrapper> results = this.server.getCommandDispatcher().dispatcher().parse(BrigadierCommandCompleter.prepareStringReader(buffer), this.commandSourceStack);
        int pos = 0;
        if (buffer.startsWith("/")) {
            builder.append("/", AttributedStyle.DEFAULT);
            pos = 1;
        }
        int component = -1;
        for (final ParsedCommandNode<CommandListenerWrapper> node : results.getContext().getLastChild().getNodes()) {
            if (node.getRange().getStart() >= buffer.length()) {
                break;
            }
            final int start = node.getRange().getStart();
            final int end = Math.min(node.getRange().getEnd(), buffer.length());
            builder.append(buffer.substring(pos, start), AttributedStyle.DEFAULT);
            if (node.getNode() instanceof LiteralCommandNode) {
                builder.append(buffer.substring(start, end), AttributedStyle.DEFAULT);
            } else {
                if (++component >= COLORS.length) {
                    component = 0;
                }
                builder.append(buffer.substring(start, end), AttributedStyle.DEFAULT.foreground(COLORS[component]));
            }
            pos = end;
        }
        if (pos < buffer.length()) {
            builder.append((buffer.substring(pos)), AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
        }
        return builder.toAttributedString();
    }

    @Override
    public void setErrorPattern(Pattern errorPattern) {}
    @Override
    public void setErrorIndex(int errorIndex) {}
}

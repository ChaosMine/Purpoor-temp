package org.bukkit.craftbukkit.scoreboard;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardScore;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import net.pl3x.purpur.PurpurConfig;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.WeakCollection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public final class CraftScoreboardManager implements ScoreboardManager {
    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards = new WeakCollection<>();
    private final Map<CraftPlayer, CraftScoreboard> playerBoards = new Reference2ObjectOpenHashMap<>();

    public CraftScoreboardManager(MinecraftServer minecraftserver, net.minecraft.world.scores.Scoreboard scoreboardServer) {
        mainScoreboard = new CraftScoreboard(scoreboardServer);
        mainScoreboard.registeredGlobally = true; // Paper
        server = minecraftserver;
        scoreboards.add(mainScoreboard);
    }

    @Override
    public CraftScoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    @Override
    public CraftScoreboard getNewScoreboard() {
        if (PurpurConfig.disableScoreboards) throw new UnsupportedOperationException("scoreboards removed");
        CraftScoreboard scoreboard = new CraftScoreboard(new ScoreboardServer(server));
        // Paper start
        if (com.destroystokyo.paper.PaperConfig.trackPluginScoreboards) {
            scoreboard.registeredGlobally = true;
        scoreboards.add(scoreboard);
        }
        // Paper end
        return scoreboard;
    }

    // Paper start
    public void registerScoreboardForVanilla(CraftScoreboard scoreboard) {
        scoreboards.add(scoreboard);
    }
    // Paper end

    // CraftBukkit method
    public CraftScoreboard getPlayerBoard(CraftPlayer player) {
        CraftScoreboard board = playerBoards.get(player);
        return (CraftScoreboard) (board == null ? getMainScoreboard() : board);
    }

    // CraftBukkit method
    public void setPlayerBoard(CraftPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard) throws IllegalArgumentException {
        if (PurpurConfig.disableScoreboards) return;
        Validate.isTrue(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");

        CraftScoreboard scoreboard = (CraftScoreboard) bukkitScoreboard;
        net.minecraft.world.scores.Scoreboard oldboard = getPlayerBoard(player).getHandle();
        net.minecraft.world.scores.Scoreboard newboard = scoreboard.getHandle();
        EntityPlayer entityplayer = player.getHandle();

        if (oldboard == newboard) {
            return;
        }

        if (scoreboard == mainScoreboard) {
            playerBoards.remove(player);
        } else {
            playerBoards.put(player, (CraftScoreboard) scoreboard);
        }

        // Old objective tracking
        HashSet<ScoreboardObjective> removed = new HashSet<ScoreboardObjective>();
        for (int i = 0; i < 3; ++i) {
            ScoreboardObjective scoreboardobjective = oldboard.getObjectiveForSlot(i);
            if (scoreboardobjective != null && !removed.contains(scoreboardobjective)) {
                entityplayer.playerConnection.sendPacket(new PacketPlayOutScoreboardObjective(scoreboardobjective, 1));
                removed.add(scoreboardobjective);
            }
        }

        // Old team tracking
        Iterator<?> iterator = oldboard.getTeams().iterator();
        while (iterator.hasNext()) {
            ScoreboardTeam scoreboardteam = (ScoreboardTeam) iterator.next();
            entityplayer.playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(scoreboardteam, 1));
        }

        // The above is the reverse of the below method.
        server.getPlayerList().sendScoreboard((ScoreboardServer) newboard, player.getHandle());
    }

    // CraftBukkit method
    public void removePlayer(Player player) {
        if (!PurpurConfig.disableScoreboards) {
            playerBoards.remove(player);
        }
    }

    // CraftBukkit method
    public void getScoreboardScores(IScoreboardCriteria criteria, String name, Consumer<ScoreboardScore> consumer) {
        if (!PurpurConfig.disableScoreboards) {
            // Tuinity start - add timings for scoreboard search
            // plugins leaking scoreboards will make this very expensive, let server owners debug it easily
            // Tuinity end - add timings for scoreboard search
            for (CraftScoreboard scoreboard : scoreboards) {
                Scoreboard board = scoreboard.board;
                board.getObjectivesForCriteria(criteria, name, (score) -> consumer.accept(score));
            }
            // Tuinity end - add timings for scoreboard search
        }
    }
}

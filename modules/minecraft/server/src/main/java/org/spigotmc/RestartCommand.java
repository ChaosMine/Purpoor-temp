package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RestartCommand extends Command
{

    public RestartCommand(String name)
    {
        super( name );
        this.description = "Restarts the server";
        this.usageMessage = "/restart";
        this.setPermission( "bukkit.command.restart" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args)
    {
        if ( testPermission( sender ) )
        {
            MinecraftServer.SERVER.processQueue.add( new Runnable()
            {
                @Override
                public void run()
                {
                    restart();
                }
            } );
        }
        return true;
    }

    public static void restart()
    {
        try
        {
            System.out.println("Stopping server.");
            WatchdogThread.doStop();

            shutdownServer(false);
            // Paper end
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    // Paper start - sync copied from above with minor changes, async added
    private static void shutdownServer(boolean isRestarting)
    {
        if ( MinecraftServer.SERVER.isMainThread() )
        {
            // Kick all players
            for (var p : com.google.common.collect.ImmutableList.copyOf( MinecraftServer.SERVER.getPlayerList().players ) )
            {
                p.playerConnection.disconnect(SpigotConfig.restartMessage, org.bukkit.event.player.PlayerKickEvent.Cause.RESTART_COMMAND); // Paper - kick event reason (cause is never used))
            }
            // Give the socket a chance to send the packets
            try
            {
                Thread.sleep( 100 );
            } catch ( InterruptedException ex )
            {
            }

            closeSocket();

            // Actually shutdown
            try
            {
                MinecraftServer.SERVER.close(); // calls stop()
            } catch ( Throwable t )
            {
            }

            // Actually stop the JVM
            System.exit( 0 );

        } else
        {
            // Mark the server to shutdown at the end of the tick
            MinecraftServer.SERVER.safeShutdown( false, isRestarting );

            // wait 10 seconds to see if we're actually going to try shutdown
            try
            {
                Thread.sleep( 10000 );
            }
            catch (InterruptedException ignored)
            {
            }

            // Check if we've actually hit a state where the server is going to safely shutdown
            // if we have, let the server stop as usual
            if (MinecraftServer.SERVER.isStopped()) return;

            // If the server hasn't stopped by now, assume worse case and kill
            closeSocket();
            System.exit( 0 );
        }
    }
    // Paper end

    // Paper - Split from moved code
    private static void closeSocket()
    {
        // Close the socket so we can rebind with the new process
        MinecraftServer.SERVER.getServerConnection().close();

        // Give time for it to kick in
        try
        {
            Thread.sleep( 100 );
        } catch ( InterruptedException ex )
        {
        }
    }
    // Paper end
}

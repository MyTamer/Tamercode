package megamek.server.commands;

import megamek.common.*;
import megamek.server.Server;
import java.util.*;

/**
 * @author Andrew Hunter
 *
 */
public class HeatSinkCommand extends ServerCommand {

    /**
     * @param server
     * @param name
     * @param helpText
     */
    public HeatSinkCommand(Server server) {
        super(server, "heatSink", "Allows the turning off of mech's heatsinks.  Use not recommended. Usage: /heatSink [hex number of mech] [total number of sinks you wish to be ON]");
    }

    public void run(int connId, String[] args) {
        try {
            if (server.getGame().getPhase() != IGame.PHASE_END) {
                server.sendServerChat(connId, "Error: heat sinks can only be turned off in the end phase.");
            } else {
                if (args.length < 3 || args.length > 3) {
                    server.sendServerChat(connId, "Syntax error, use \"/help heatSink\" to find out more.");
                } else {
                    int hexNumberX = Integer.parseInt(args[1].substring(0, 2)) - 1;
                    int hexNumberY = Integer.parseInt(args[1].substring(2)) - 1;
                    int sinks = Integer.parseInt(args[2]);
                    Coords c = new Coords(hexNumberX, hexNumberY);
                    Mech target = null;
                    for (Enumeration possibles = server.getGame().getEntities(c); possibles.hasMoreElements(); ) {
                        Entity entity = (Entity) possibles.nextElement();
                        if (entity instanceof Mech && entity.getOwnerId() == connId) {
                            target = (Mech) entity;
                        }
                    }
                    if (target == null) {
                        server.sendServerChat(connId, "Error: could not find mech owned by " + server.getGame().getPlayer(connId).getName() + " in specified hex.");
                    } else {
                        if (target.getNumberOfSinks() < sinks) {
                            server.sendServerChat(connId, "Error:  Tried to turn on more sinks than exist");
                        } else if (sinks < 0) {
                            server.sendServerChat(connId, "Error: Can't have negative active sinks");
                        } else {
                            target.setActiveSinksNextRound(sinks);
                            server.sendServerChat("Player " + server.getGame().getPlayer(connId).getName() + " turned on " + sinks + " heat sinks in entity " + target.getDisplayName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            server.sendServerChat("Error trying to set sinks, text: " + e.getMessage());
        }
    }
}

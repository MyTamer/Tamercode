package ring.commands.admin;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.mobiles.Mobile;
import ring.world.World;

public class Godvoice extends AbstractAdminCommand implements Command {

    public CommandResult execute(CommandSender sender, CommandParameters params) {
        params.init(CommandType.TEXT);
        CommandResult res = new CommandResult();
        res.setFailText("You don't feel particularly divine...");
        if (super.isAccessAllowed(sender) == false) {
            return res;
        }
        Mobile mob = (Mobile) sender;
        String message = "";
        String textBackToPlayer;
        textBackToPlayer = "You project your voice across the cosmos, saying, \"";
        String textToOtherPlayers = "The voice of the gods rumbles in the sky! \"";
        int length = params.length();
        for (int c = 0; c < length; c++) {
            message += (String) params.getParameter(c);
            if (c != length - 1) {
                message += " ";
            }
        }
        textBackToPlayer += message;
        textBackToPlayer += "\"";
        textToOtherPlayers += message;
        textToOtherPlayers += "\"";
        res.setText(textBackToPlayer);
        World.notifyPlayersAtLocation(mob, textToOtherPlayers);
        res.setSuccessful(true);
        return res;
    }

    public String getCommandName() {
        return "godvoice";
    }

    public void rollback() {
        throw new UnsupportedOperationException();
    }
}

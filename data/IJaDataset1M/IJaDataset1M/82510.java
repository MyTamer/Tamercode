package megamek.client.bot.ui.AWT;

import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import megamek.client.bot.BotClient;
import megamek.client.bot.Messages;
import megamek.client.ui.AWT.CommonHelpDialog;
import megamek.client.ui.AWT.ConfirmDialog;
import megamek.client.ui.AWT.GUIPreferences;
import megamek.common.IGame;
import megamek.common.event.GameBoardChangeEvent;
import megamek.common.event.GameBoardNewEvent;
import megamek.common.event.GameEndEvent;
import megamek.common.event.GameEntityChangeEvent;
import megamek.common.event.GameEntityNewEvent;
import megamek.common.event.GameEntityNewOffboardEvent;
import megamek.common.event.GameEntityRemoveEvent;
import megamek.common.event.GameListener;
import megamek.common.event.GameMapQueryEvent;
import megamek.common.event.GameNewActionEvent;
import megamek.common.event.GamePhaseChangeEvent;
import megamek.common.event.GamePlayerChangeEvent;
import megamek.common.event.GamePlayerChatEvent;
import megamek.common.event.GamePlayerConnectedEvent;
import megamek.common.event.GamePlayerDisconnectedEvent;
import megamek.common.event.GameReportEvent;
import megamek.common.event.GameSettingsChangeEvent;
import megamek.common.event.GameTurnChangeEvent;

public class BotGUI implements GameListener {

    private BotClient bot;

    private Frame frame = new Frame();

    private static boolean WarningShown;

    public BotGUI(BotClient bot) {
        this.bot = bot;
    }

    public void gamePhaseChange(GamePhaseChangeEvent e) {
        if (bot.game.getPhase() == IGame.Phase.PHASE_LOUNGE || bot.game.getPhase() == IGame.Phase.PHASE_STARTING_SCENARIO) {
            notifyOfBot();
        }
    }

    public void notifyOfBot() {
        if (GUIPreferences.getInstance().getNagForBotReadme() && !WarningShown) {
            WarningShown = true;
            String title = Messages.getString("BotGUI.notifyOfBot.title");
            String body = Messages.getString("BotGUI.notifyOfBot.message");
            Dimension screenSize = frame.getToolkit().getScreenSize();
            frame.pack();
            frame.setLocation(screenSize.width / 2 - frame.getSize().width / 2, screenSize.height / 2 - frame.getSize().height / 2);
            ConfirmDialog confirm = new ConfirmDialog(frame, title, body, true);
            confirm.setVisible(true);
            if (!confirm.getShowAgain()) {
                GUIPreferences.getInstance().setNagForBotReadme(false);
            }
            if (confirm.getAnswer()) {
                File helpfile = new File("docs/ai-readme.txt");
                new CommonHelpDialog(frame, helpfile).setVisible(true);
            }
        }
    }

    public void gamePlayerConnected(GamePlayerConnectedEvent e) {
    }

    public void gamePlayerDisconnected(GamePlayerDisconnectedEvent e) {
    }

    public void gamePlayerChange(GamePlayerChangeEvent e) {
    }

    public void gamePlayerChat(GamePlayerChatEvent e) {
    }

    public void gameTurnChange(GameTurnChangeEvent e) {
    }

    public void gameReport(GameReportEvent e) {
    }

    public void gameEnd(GameEndEvent e) {
    }

    public void gameBoardNew(GameBoardNewEvent e) {
    }

    public void gameBoardChanged(GameBoardChangeEvent e) {
    }

    public void gameSettingsChange(GameSettingsChangeEvent e) {
    }

    public void gameMapQuery(GameMapQueryEvent e) {
    }

    public void gameEntityNew(GameEntityNewEvent e) {
    }

    public void gameEntityNewOffboard(GameEntityNewOffboardEvent e) {
    }

    public void gameEntityChange(GameEntityChangeEvent e) {
    }

    public void gameNewAction(GameNewActionEvent e) {
    }

    public void gameEntityRemove(GameEntityRemoveEvent e) {
    }
}

package spacetrader.gui;

import java.awt.Point;
import java.util.Arrays;
import jwinforms.Button;
import jwinforms.FormSize;
import jwinforms.Label;
import jwinforms.WinformForm;
import jwinforms.enums.ContentAlignment;
import jwinforms.enums.DialogResult;
import jwinforms.enums.FormBorderStyle;
import jwinforms.enums.FormStartPosition;
import spacetrader.Functions;
import spacetrader.HighScoreRecord;
import spacetrader.Strings;

public class FormViewHighScores extends WinformForm {

    private Button btnClose;

    private Label lblRank0;

    private Label lblRank2;

    private Label lblRank1;

    private Label lblScore0;

    private Label lblScore1;

    private Label lblScore2;

    private Label lblName0;

    private Label lblName1;

    private Label lblName2;

    private Label lblStatus0;

    private Label lblStatus1;

    private Label lblStatus2;

    public FormViewHighScores() {
        InitializeComponent();
        Label[] lblName = new Label[] { lblName0, lblName1, lblName2 };
        Label[] lblScore = new Label[] { lblScore0, lblScore1, lblScore2 };
        Label[] lblStatus = new Label[] { lblStatus0, lblStatus1, lblStatus2 };
        HighScoreRecord[] highScores = Functions.GetHighScores(this);
        for (int i = highScores.length - 1; i >= 0 && highScores[i] != null; i--) {
            lblName[2 - i].setText(highScores[i].Name());
            lblScore[2 - i].setText(Functions.FormatNumber(highScores[i].Score() / 10) + "." + highScores[i].Score() % 10);
            lblStatus[2 - i].setText(Functions.StringVars(Strings.HighScoreStatus, new String[] { Strings.GameCompletionTypes[highScores[i].Type().CastToInt()], Functions.Multiples(highScores[i].Days(), Strings.TimeUnit), Functions.Multiples(highScores[i].Worth(), Strings.MoneyUnit), Strings.DifficultyLevels[highScores[i].Difficulty().CastToInt()].toLowerCase() }));
            lblScore[2 - i].setVisible(true);
            lblStatus[2 - i].setVisible(true);
        }
    }

    private void InitializeComponent() {
        btnClose = new Button();
        lblRank0 = new Label();
        lblRank2 = new Label();
        lblRank1 = new Label();
        lblScore0 = new Label();
        lblScore1 = new Label();
        lblScore2 = new Label();
        lblName0 = new Label();
        lblName1 = new Label();
        lblName2 = new Label();
        lblStatus0 = new Label();
        lblStatus1 = new Label();
        lblStatus2 = new Label();
        SuspendLayout();
        btnClose.setDialogResult(DialogResult.Cancel);
        btnClose.setLocation(new Point(-32, -32));
        btnClose.setName("btnClose");
        btnClose.setSize(new FormSize(32, 32));
        btnClose.setTabIndex(32);
        btnClose.setTabStop(false);
        btnClose.setText("X");
        lblRank0.setAutoSize(true);
        lblRank0.setLocation(new Point(8, 8));
        lblRank0.setName("lblRank0");
        lblRank0.setSize(new FormSize(14, 13));
        lblRank0.setTabIndex(33);
        lblRank0.setText("1.");
        lblRank0.TextAlign = ContentAlignment.TopRight;
        lblRank2.setAutoSize(true);
        lblRank2.setLocation(new Point(8, 136));
        lblRank2.setName("lblRank2");
        lblRank2.setSize(new FormSize(14, 13));
        lblRank2.setTabIndex(34);
        lblRank2.setText("3.");
        lblRank2.TextAlign = ContentAlignment.TopRight;
        lblRank1.setAutoSize(true);
        lblRank1.setLocation(new Point(8, 72));
        lblRank1.setName("lblRank1");
        lblRank1.setSize(new FormSize(14, 13));
        lblRank1.setTabIndex(35);
        lblRank1.setText("2.");
        lblRank1.TextAlign = ContentAlignment.TopRight;
        lblScore0.setLocation(new Point(168, 8));
        lblScore0.setName("lblScore0");
        lblScore0.setSize(new FormSize(43, 13));
        lblScore0.setTabIndex(36);
        lblScore0.setText("888.8%");
        lblScore0.TextAlign = ContentAlignment.TopRight;
        lblScore0.setVisible(false);
        lblScore1.setLocation(new Point(168, 72));
        lblScore1.setName("lblScore1");
        lblScore1.setSize(new FormSize(43, 13));
        lblScore1.setTabIndex(37);
        lblScore1.setText("888.8%");
        lblScore1.TextAlign = ContentAlignment.TopRight;
        lblScore1.setVisible(false);
        lblScore2.setLocation(new Point(168, 136));
        lblScore2.setName("lblScore2");
        lblScore2.setSize(new FormSize(43, 13));
        lblScore2.setTabIndex(38);
        lblScore2.setText("888.8%");
        lblScore2.TextAlign = ContentAlignment.TopRight;
        lblScore2.setVisible(false);
        lblName0.setLocation(new Point(24, 8));
        lblName0.setName("lblName0");
        lblName0.setSize(new FormSize(144, 13));
        lblName0.setTabIndex(39);
        lblName0.setText("Empty");
        lblName1.setLocation(new Point(24, 72));
        lblName1.setName("lblName1");
        lblName1.setSize(new FormSize(144, 13));
        lblName1.setTabIndex(40);
        lblName1.setText("Empty");
        lblName2.setLocation(new Point(24, 136));
        lblName2.setName("lblName2");
        lblName2.setSize(new FormSize(144, 13));
        lblName2.setTabIndex(41);
        lblName2.setText("Empty");
        lblStatus0.setLocation(new Point(24, 24));
        lblStatus0.setName("lblStatus0");
        lblStatus0.setSize(new FormSize(200, 26));
        lblStatus0.setTabIndex(42);
        lblStatus0.setText("Claimed moon in 888,888 days, worth 8,888,888 credits on impossible level.");
        lblStatus0.setVisible(false);
        lblStatus1.setLocation(new Point(24, 88));
        lblStatus1.setName("lblStatus1");
        lblStatus1.setSize(new FormSize(200, 26));
        lblStatus1.setTabIndex(43);
        lblStatus1.setText("Claimed moon in 888,888 days, worth 8,888,888 credits on impossible level.");
        lblStatus1.setVisible(false);
        lblStatus2.setLocation(new Point(24, 152));
        lblStatus2.setName("lblStatus2");
        lblStatus2.setSize(new FormSize(200, 26));
        lblStatus2.setTabIndex(44);
        lblStatus2.setText("Claimed moon in 888,888 days, worth 8,888,888 credits on impossible level.");
        lblStatus2.setVisible(false);
        setAutoScaleBaseSize(new FormSize(5, 13));
        setCancelButton(btnClose);
        setClientSize(new FormSize(218, 191));
        Controls.addAll(Arrays.asList(lblStatus2, lblStatus1, lblStatus0, lblName2, lblName1, lblName0, lblScore2, lblScore1, lblScore0, lblRank1, lblRank2, lblRank0, btnClose));
        setFormBorderStyle(FormBorderStyle.FixedDialog);
        setMaximizeBox(false);
        setMinimizeBox(false);
        setName("FormViewHighScores");
        setShowInTaskbar(false);
        setStartPosition(FormStartPosition.CenterParent);
        setText("High Scores");
        ResumeLayout(false);
    }
}

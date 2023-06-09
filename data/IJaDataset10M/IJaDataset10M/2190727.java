package org.penemunxt.projects.penemunxtexplorer.pc.map.timeline;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.penemunxt.projects.penemunxtexplorer.pc.connection.DataShare;
import org.penemunxt.projects.penemunxtexplorer.pc.map.*;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.*;

public class MapTimeline extends JPanel implements ChangeListener, MouseListener, MouseMotionListener, ActionListener {

    static final int TIMELINE_PLAY_FPS_MIN = 1;

    static final int TIMELINE_PLAY_FPS_MAX = 100;

    static final int TIMELINE_PLAY_FPS_DEFAULT = 25;

    Panel timelineControlPanel = new Panel();

    JButton btnTimelineEnableDisable;

    JButton btnTimelinePlayPause;

    JButton btnTimelineRewind;

    JSlider sldTimeline;

    JSlider sldTimelineSpeed;

    JLabel lblFPS;

    MapVisulaisation mapVisulaisationThumbnail;

    MapThumbnail mapThumbnail;

    ArrayList<ChangeListener> frameChangeListeners = new ArrayList<ChangeListener>();

    DataShare DS;

    Color bgColor = null;

    int timelineDefault = 0;

    int timelinePlaySpeed = TIMELINE_PLAY_FPS_DEFAULT;

    boolean timelinePlay = false;

    boolean timelineEnabled = false;

    MapTimelinePlayer TimelinePlayer;

    public synchronized void addFrameChangeListeners(ChangeListener listener) {
        frameChangeListeners.add(listener);
    }

    public synchronized void removeFrameChangeListeners(ChangeListener listener) {
        frameChangeListeners.remove(listener);
    }

    private void triggerFrameChanged() {
        if (frameChangeListeners != null && frameChangeListeners.size() > 0) {
            for (ChangeListener listener : frameChangeListeners) {
                listener.stateChanged(new ChangeEvent(this));
            }
        }
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public DataShare getDS() {
        return DS;
    }

    public void setDS(DataShare dS) {
        DS = dS;
    }

    public int getCurrentFrame() {
        return sldTimeline.getValue();
    }

    public void setCurrentFrame(int frame) {
        this.setCurrentFrame(frame, true);
    }

    public void setCurrentFrame(int frame, boolean triggerChanged) {
        sldTimeline.setValue(frame);
        if (triggerChanged) {
            triggerFrameChanged();
        }
    }

    private boolean isDataEmpty() {
        return !(DS != null && DS.NXTRobotData != null && DS.NXTRobotData.size() > 0);
    }

    private void refreshSlider() {
        sldTimeline.setMinimum(0);
        if (!isDataEmpty()) {
            sldTimeline.setMaximum(DS.NXTRobotData.size());
            if (!isEnabled()) {
                sldTimeline.setValue(sldTimeline.getMaximum());
            }
        } else {
            sldTimeline.setValue(0);
            sldTimeline.setMaximum(1);
        }
    }

    private void refreshSpeedLabel() {
        lblFPS.setText(timelinePlaySpeed + " FPS");
    }

    public void refresh() {
        setAllowEnable(!isDataEmpty());
        refreshSpeedLabel();
    }

    public void setAllowEnable(boolean allowEnable) {
        if (allowEnable) {
            btnTimelineEnableDisable.setEnabled(true);
        } else {
            this.setEnabled(false);
            btnTimelineEnableDisable.setEnabled(false);
        }
        refreshSlider();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            disableTimeline();
        } else {
            enableTimeline();
        }
    }

    @Override
    public boolean isEnabled() {
        return timelineEnabled;
    }

    public void toggleTimelineEnabled() {
        if (timelineEnabled) {
            disableTimeline();
        } else {
            enableTimeline();
        }
    }

    private void enableTimeline() {
        timelineEnabled = true;
        stopAutoPlay();
        btnTimelineEnableDisable.setText("Disable");
        btnTimelinePlayPause.setEnabled(true);
        btnTimelineRewind.setEnabled(true);
        sldTimelineSpeed.setEnabled(true);
        sldTimeline.setEnabled(true);
        sldTimeline.setVisible(true);
        lblFPS.setEnabled(true);
    }

    private void disableTimeline() {
        timelineEnabled = false;
        stopAutoPlay();
        btnTimelineEnableDisable.setText("Enable");
        btnTimelinePlayPause.setEnabled(false);
        btnTimelineRewind.setEnabled(false);
        sldTimelineSpeed.setEnabled(false);
        sldTimeline.setEnabled(false);
        sldTimeline.setVisible(false);
        lblFPS.setEnabled(false);
    }

    public void rewind() {
        sldTimeline.setValue(sldTimeline.getMinimum());
    }

    public void toggleAutoPlay() {
        if (timelinePlay) {
            stopAutoPlay();
        } else {
            startAutoPlay();
        }
    }

    public void startAutoPlay() {
        stopAutoPlay();
        if (sldTimeline.getValue() == sldTimeline.getMaximum()) {
            sldTimeline.setValue(sldTimeline.getMinimum());
        }
        timelinePlay = true;
        TimelinePlayer = new MapTimelinePlayer(sldTimeline, timelinePlaySpeed);
        TimelinePlayer.addDataEndListeners(this);
        TimelinePlayer.start();
        btnTimelinePlayPause.setText("Pause");
    }

    public void stopAutoPlay() {
        timelinePlay = false;
        if (TimelinePlayer != null) {
            TimelinePlayer.deactivate();
            TimelinePlayer = null;
        }
        btnTimelinePlayPause.setText("Play");
    }

    public MapTimeline() {
        super();
        createGUI();
    }

    public void setMapThumbnail(int mapScale, MapProcessors mapProcessors, DataShare DS) {
        if (mapProcessors != null) {
            mapThumbnail = new MapThumbnail();
            mapVisulaisationThumbnail = new MapVisulaisation(mapScale, mapProcessors, false);
            mapVisulaisationThumbnail.setDS(DS);
        }
    }

    private void createGUI() {
        Font fntSectionHeader = new Font("Arial", Font.BOLD, 14);
        Font fntLabelHeader = new Font("Arial", Font.BOLD, 12);
        Label lblTimelineHeader = new Label("Timeline");
        lblTimelineHeader.setFont(fntSectionHeader);
        sldTimeline = new JSlider(SwingConstants.HORIZONTAL, 0, 1, timelineDefault);
        sldTimeline.setPaintTicks(true);
        sldTimeline.setPaintLabels(true);
        sldTimeline.addChangeListener(this);
        sldTimeline.setBackground(bgColor);
        sldTimeline.addMouseListener(this);
        sldTimeline.addMouseMotionListener(this);
        Label lblTimelineSpeedHeader = new Label("Speed");
        lblTimelineSpeedHeader.setFont(fntLabelHeader);
        sldTimelineSpeed = new JSlider(SwingConstants.HORIZONTAL, TIMELINE_PLAY_FPS_MIN, TIMELINE_PLAY_FPS_MAX, TIMELINE_PLAY_FPS_DEFAULT);
        sldTimelineSpeed.setPaintTicks(false);
        sldTimelineSpeed.setPaintLabels(false);
        sldTimelineSpeed.setBackground(bgColor);
        sldTimelineSpeed.addChangeListener(this);
        lblFPS = new JLabel("");
        btnTimelineEnableDisable = new JButton("Enable");
        btnTimelineEnableDisable.addActionListener(this);
        btnTimelinePlayPause = new JButton("Play/Pause");
        btnTimelinePlayPause.addActionListener(this);
        btnTimelineRewind = new JButton("Rewind");
        btnTimelineRewind.addActionListener(this);
        timelineControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        timelineControlPanel.add(btnTimelineEnableDisable);
        timelineControlPanel.add(btnTimelineRewind);
        timelineControlPanel.add(btnTimelinePlayPause);
        timelineControlPanel.add(lblTimelineSpeedHeader);
        timelineControlPanel.add(sldTimelineSpeed);
        timelineControlPanel.add(lblFPS);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(bgColor);
        this.add(lblTimelineHeader);
        this.add(timelineControlPanel);
        this.add(sldTimeline);
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        if (ce.getSource() == sldTimelineSpeed) {
            timelinePlaySpeed = sldTimelineSpeed.getValue();
            if (TimelinePlayer != null) {
                TimelinePlayer.setFPS(timelinePlaySpeed);
            }
        } else if (ce.getSource() == sldTimeline) {
            triggerFrameChanged();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnTimelinePlayPause) {
            toggleAutoPlay();
        } else if (ae.getSource() == btnTimelineEnableDisable) {
            toggleTimelineEnabled();
        } else if (ae.getSource() == btnTimelineRewind) {
            rewind();
        } else if (ae.getSource() == TimelinePlayer) {
            stopAutoPlay();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (mapThumbnail != null) {
            if (!mapThumbnail.isOpen()) {
                mapThumbnail.open(mapVisulaisationThumbnail, 250, 250);
                mapThumbnail.setScale(25);
            }
            int x = e.getXOnScreen();
            float percentage = (e.getX() / (float) sldTimeline.getWidth());
            int frame = (int) (percentage * sldTimeline.getMaximum());
            x -= (mapThumbnail.getWidth() / 2);
            int finalX;
            finalX = Math.min(x, ((int) sldTimeline.getLocationOnScreen().getX() + sldTimeline.getWidth() - mapVisulaisationThumbnail.getWidth()));
            finalX = Math.max(finalX, (int) sldTimeline.getLocationOnScreen().getX());
            int finalY = (int) sldTimeline.getLocationOnScreen().getY() - mapThumbnail.mainWindow.getHeight() - 100;
            mapThumbnail.setFrame(frame);
            mapThumbnail.move(finalX, finalY);
            mapVisulaisationThumbnail.refresh();
        }
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        if (mapThumbnail != null) {
            mapThumbnail.hide();
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        if (mapThumbnail != null) {
            mapThumbnail.hide();
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }
}

package org.jeventslib;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Uid;

/**
 * Event managing gui
 * 
 * @author Rudi Giacomini Pilon
 */
public class Planner extends javax.swing.JFrame {

    public static DateTime[] specialDates = new DateTime[1];

    public static calEvent[] events = new calEvent[1];

    public static final int EVENT_ID = 0;

    public static final int EVENT_DESCRIPTION = 1;

    private static String calendarFile = null;

    private DateEval dateEvaluate = new DateEval();

    private static Debug debug = Debug.getInstance();

    private static final boolean DEBUGSTATE = true;

    /**
     
     * Refresh events in calendar for a given year
     * 
     * @param year the year to check
     */
    private void refreshEvents(int year) {
        try {
            if (year == 0) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
            }
            DateTime from = new DateTime(Integer.toString(year) + "0101T000000Z");
            DateTime to = new DateTime(Integer.toString(year) + "1231T000000Z");
            ical4jIface iface = new ical4jIface(calendarFile);
            iface.calendarParse(from, to);
        } catch (ParseException ex) {
            Logger.getLogger(JEventsLib.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
      * refresh the events list
      * 
      */
    private void refreshList() {
        eventDescrTextArea.setText("");
        DefaultListModel listModel;
        events = new calEvent[1];
        Date whatDay = eventsCalendar.getDate();
        Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(whatDay);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.clear(java.util.Calendar.MINUTE);
        cal.clear(java.util.Calendar.SECOND);
        DateTime from = new DateTime(cal.getTime());
        ical4jIface iface = new ical4jIface(calendarFile);
        iface.daylyEventsParse(from);
        listModel = new DefaultListModel();
        int totElements = events.length - 1;
        for (int i = 0; i < totElements; i++) {
            listModel.addElement(events[i].getSummary());
        }
        eventsList.setModel(listModel);
    }

    /** Creates new form Planner
      * 
      * @param calendar 
      */
    public Planner(String calendar) {
        calendarFile = calendar;
        refreshEvents(0);
        initComponents();
        eventsCalendar.getDayChooser().addDateEvaluator(dateEvaluate);
        Date today = new Date();
        eventsCalendar.setDate(today);
        eventsCalendar.setTodayButtonVisible(true);
        eventsCalendar.getDayChooser().setAlwaysFireDayProperty(true);
        refreshList();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        eventsCalendar = new com.toedter.calendar.JCalendar();
        eventListScrollPane = new javax.swing.JScrollPane();
        eventsList = new javax.swing.JList();
        eventsListLabel = new javax.swing.JLabel();
        eventDescrScrollPane = new javax.swing.JScrollPane();
        eventDescrTextArea = new javax.swing.JTextArea();
        evtAddButton = new javax.swing.JButton();
        evtDelButton = new javax.swing.JButton();
        evtEditButton = new javax.swing.JButton();
        evtAckButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());
        eventsCalendar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        eventsCalendar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                eventsCalendarPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 15);
        getContentPane().add(eventsCalendar, gridBagConstraints);
        eventListScrollPane.setMinimumSize(new java.awt.Dimension(150, 300));
        eventListScrollPane.setPreferredSize(new java.awt.Dimension(150, 300));
        eventsList.setFont(new java.awt.Font("Dialog", 1, 10));
        eventsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        eventsList.setMinimumSize(new java.awt.Dimension(100, 200));
        eventsList.setPreferredSize(new java.awt.Dimension(140, 290));
        eventsList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eventsListMouseClicked(evt);
            }
        });
        eventListScrollPane.setViewportView(eventsList);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 15);
        getContentPane().add(eventListScrollPane, gridBagConstraints);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jeventslib/Bundle");
        eventsListLabel.setText(bundle.getString("TODAY EVENTS"));
        eventsListLabel.setMaximumSize(new java.awt.Dimension(950, 115));
        eventsListLabel.setMinimumSize(new java.awt.Dimension(95, 25));
        eventsListLabel.setName("");
        eventsListLabel.setPreferredSize(new java.awt.Dimension(95, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        getContentPane().add(eventsListLabel, gridBagConstraints);
        eventDescrScrollPane.setHorizontalScrollBar(null);
        eventDescrScrollPane.setMinimumSize(new java.awt.Dimension(164, 96));
        eventDescrScrollPane.setPreferredSize(new java.awt.Dimension(164, 96));
        eventDescrTextArea.setColumns(20);
        eventDescrTextArea.setRows(5);
        eventDescrTextArea.setMinimumSize(new java.awt.Dimension(164, 94));
        eventDescrScrollPane.setViewportView(eventDescrTextArea);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 20, 15);
        getContentPane().add(eventDescrScrollPane, gridBagConstraints);
        evtAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jeventslib/icons/calendar_add.png")));
        evtAddButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evtAddButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 5);
        getContentPane().add(evtAddButton, gridBagConstraints);
        evtDelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jeventslib/icons/calendar_sub.png")));
        evtDelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evtDelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 15);
        getContentPane().add(evtDelButton, gridBagConstraints);
        evtEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jeventslib/icons/calendar_edit.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 20, 5);
        getContentPane().add(evtEditButton, gridBagConstraints);
        evtAckButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jeventslib/icons/calendar_link.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 20, 15);
        getContentPane().add(evtAckButton, gridBagConstraints);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 461) / 2, (screenSize.height - 466) / 2, 461, 466);
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        refreshEvents(0);
    }

    private void eventsCalendarPropertyChange(java.beans.PropertyChangeEvent evt) {
        refreshList();
        eventDescrTextArea.setText("");
    }

    private void eventsListMouseClicked(java.awt.event.MouseEvent evt) {
        int idx = eventsList.getSelectedIndex();
        if (idx >= 0) {
            eventDescrTextArea.setText(events[idx].getDescription());
        }
    }

    private void evtAddButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Date whatDay = eventsCalendar.getDate();
        addEventDialog aed = new addEventDialog(null, true);
        String delocDate = LocUtil.delocalizeDate(whatDay);
        aed.setDay(LocUtil.localizeDate(delocDate));
        aed.setVisible(true);
        boolean thereIsNewEvent = aed.getReturnStatus() == addEventDialog.RET_OK;
        if (thereIsNewEvent) {
            net.fortuna.ical4j.model.Date adate = new net.fortuna.ical4j.model.Date(whatDay);
            calEvent ce = aed.getReturnEvent();
            VEvent newEvent = new VEvent(adate, ce.getSummary());
            Uid hc = new Uid(ce.getHashcode());
            newEvent.getProperties().add(hc);
            Description des = new Description(ce.getDescription());
            newEvent.getProperties().add(des);
            ical4jIface iface = new ical4jIface(calendarFile);
            iface.openCalendarFile();
            iface.calendar.getComponents().add(newEvent);
            iface.saveCalendar();
            refreshEvents(0);
            eventsCalendar.getDayChooser().getDayPanel().repaint();
            eventsCalendar.getDayChooser().repaint();
            refreshList();
        }
    }

    private void evtDelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Date whatDay = eventsCalendar.getDate();
        net.fortuna.ical4j.model.Date adate = new net.fortuna.ical4j.model.Date(whatDay);
        int idx = eventsList.getSelectedIndex();
        if (idx < 0) {
            return;
        }
        String deleteThis = events[idx].getHashcode();
        ical4jIface iface = new ical4jIface(calendarFile);
        iface.deleteEvent(deleteThis);
        eventsCalendar.getDayChooser().getDayPanel().repaint();
        eventsCalendar.getDayChooser().repaint();
        refreshList();
    }

    private javax.swing.JScrollPane eventDescrScrollPane;

    private javax.swing.JTextArea eventDescrTextArea;

    private javax.swing.JScrollPane eventListScrollPane;

    private com.toedter.calendar.JCalendar eventsCalendar;

    private javax.swing.JList eventsList;

    private javax.swing.JLabel eventsListLabel;

    private javax.swing.JButton evtAckButton;

    private javax.swing.JButton evtAddButton;

    private javax.swing.JButton evtDelButton;

    private javax.swing.JButton evtEditButton;
}

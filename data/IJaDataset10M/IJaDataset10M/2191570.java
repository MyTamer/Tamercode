package org.japura.gui.calendar;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.japura.gui.PopupMenuBuilder;

/**
 * <P>
 * Copyright (C) 2011 Carlos Eduardo Leite de Andrade
 * <P>
 * This library is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <P>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <P>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <A
 * HREF="www.gnu.org/licenses/">www.gnu.org/licenses/</A>
 * <P>
 * For more information, contact: <A HREF="www.japura.org">www.japura.org</A>
 * <P>
 * 
 * @author Carlos Eduardo Leite de Andrade
 */
class DayOfMonthSlot extends CalendarSlot implements MouseListener {

    private static final long serialVersionUID = 2647751815341515373L;

    private int day;

    private int month;

    private int year;

    private boolean currentMonth;

    private boolean selected;

    public DayOfMonthSlot(Calendar calendar) {
        super(calendar, CalendarComponentType.DAY_MONTH);
        setBorder(getProperties().buildDayOfMonthBorder());
        addMouseListener(this);
        setFont(getProperties().getDayOfMonthFont());
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        setText(day + "");
    }

    @Override
    public Color getBackground() {
        if (getProperties() == null) {
            return super.getBackground();
        }
        if (selected) {
            return getProperties().getSelectedDayOfMonthBackground();
        } else if (currentMonth) {
            return getProperties().getDayOfMonthBackground();
        } else {
            return getProperties().getDayOfNonCurrentMonthBackground();
        }
    }

    @Override
    public Color getForeground() {
        if (getProperties() == null) {
            return super.getForeground();
        }
        if (selected) {
            return getProperties().getSelectedDayOfMonthForeground();
        } else if (currentMonth) {
            return getProperties().getDayOfMonthForeground();
        } else {
            return getProperties().getDayOfNonCurrentMonthForeground();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Calendar calendar = getCalendar();
        if (calendar.isEnabled() == false) {
            return;
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            PopupMenuBuilder<CalendarComponent> pmb = calendar.getPopupMenuBuilder();
            if (pmb != null) {
                JPopupMenu pm = pmb.buildPopupMenu(this);
                if (pm != null) {
                    pm.show(this, e.getX(), e.getY());
                }
            }
            return;
        }
        if (getCalendar().dialogMode == false && selected) {
            return;
        }
        long time = getDate().getTime();
        if (currentMonth && getCalendar().dialogMode) {
            getCalendar().selectedDialogDate = time;
            getCalendar().getModalDialog().dispose();
        } else {
            getCalendar().setDate(time);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DayOfWeek getDayOfWeek() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.YEAR, year);
        gc.set(GregorianCalendar.MONTH, month);
        gc.set(GregorianCalendar.DAY_OF_MONTH, day);
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.SECOND, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        int dow = gc.get(GregorianCalendar.DAY_OF_WEEK);
        return DayOfWeek.getDayOfWeek(dow);
    }

    public Date getDate() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.YEAR, year);
        gc.set(GregorianCalendar.MONTH, month);
        gc.set(GregorianCalendar.DAY_OF_MONTH, day);
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.SECOND, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        return gc.getTime();
    }

    public void setCurrentMonth(boolean currentMonth) {
        this.currentMonth = currentMonth;
    }
}

package com;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;


public abstract class Calendar extends JComponent {

    protected static final LocalTime START_TIME = LocalTime.of(0, 0);

    protected static final LocalTime END_TIME = LocalTime.of(22, 59);

    protected static final int MIN_WIDTH = 500;
    protected static final int MIN_HEIGHT = MIN_WIDTH;

    protected static final int HEADER_HEIGHT = 30;
    protected static final int TIME_COL_WIDTH = 100;
    JFrame frm = new JFrame();

    // An estimate of the width of a single character (not exact but good
    // enough)
    private static final int FONT_LETTER_PIXEL_WIDTH = 7;
    private ArrayList<CalendarEvent> events;
    private double timeScale;
    private double dayWidth;
    private Graphics2D g2;

    private EventListenerList listenerList = new EventListenerList();

    public Calendar() {
        this(new ArrayList<>());
    }
    public Calendar(Calendar calendar,Clock clock){
 
    }

    Calendar(ArrayList<CalendarEvent> events) {
        this.events = events;
        setupEventListeners();
        setupTimer();
    }

    public static LocalTime roundTime(LocalTime time, int minutes) {
        LocalTime t = time;

        if (t.getMinute() % minutes > minutes / 2) {
            t = t.plusMinutes(minutes - (t.getMinute() % minutes));
        } else if (t.getMinute() % minutes < minutes / 2) {
            t = t.minusMinutes(t.getMinute() % minutes);
        }

        return t;
    }

    private void setupEventListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!checkCalendarEventClick(e.getPoint())) {
                    checkCalendarEmptyClick(e.getPoint());
                }
            }
        });
    }

    protected abstract boolean dateInRange(LocalDate date);

    private boolean checkCalendarEventClick(Point p) {
        double x0, x1, y0, y1;
        for (CalendarEvent event : events) {
            if (!dateInRange(event.getDate())) continue;

            x0 = dayToPixel(event.getDate().getDayOfWeek());
            y0 = timeToPixel(event.getStart());
            x1 = dayToPixel(event.getDate().getDayOfWeek()) + dayWidth;
            y1 = timeToPixel(event.getEnd());

            if (p.getX() >= x0 && p.getX() <= x1 && p.getY() >= y0 && p.getY() <= y1) {
                fireCalendarEventClick(event);
                return true;
            }
        }
        return false;
    }

    private boolean checkCalendarEmptyClick(Point p) {
        final double x0 = dayToPixel(getStartDay());
        final double x1 = dayToPixel(getEndDay()) + dayWidth;
        final double y0 = timeToPixel(START_TIME);
        final double y1 = timeToPixel(END_TIME);

        if (p.getX() >= x0 && p.getX() <= x1 && p.getY() >= y0 && p.getY() <= y1) {
            LocalDate date = getDateFromDay(pixelToDay(p.getX()));
            fireCalendarEmptyClick(LocalDateTime.of(date, pixelToTime(p.getY())));
            return true;
        }
        return false;
    }

    protected abstract LocalDate getDateFromDay(DayOfWeek day);

    // CalendarEventClick methods

    public void addCalendarEventClickListener(CalendarEventClickListener l) {
        listenerList.add(CalendarEventClickListener.class, l);
    }

    public void removeCalendarEventClickListener(CalendarEventClickListener l) {
        listenerList.remove(CalendarEventClickListener.class, l);
    }

    // Notify all listeners that have registered interest for
    // notification on this event type.
    private void fireCalendarEventClick(CalendarEvent calendarEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        CalendarEventClickEvent calendarEventClickEvent;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CalendarEventClickListener.class) {
                calendarEventClickEvent = new CalendarEventClickEvent(this, calendarEvent);
                ((CalendarEventClickListener) listeners[i + 1]).calendarEventClick(calendarEventClickEvent);
            }
        }
    }

    // CalendarEmptyClick methods

    public void addCalendarEmptyClickListener(CalendarEmptyClickListener l) {
        listenerList.add(CalendarEmptyClickListener.class, l);
    }

    public void removeCalendarEmptyClickListener(CalendarEmptyClickListener l) {
        listenerList.remove(CalendarEmptyClickListener.class, l);
    }

    private void fireCalendarEmptyClick(LocalDateTime dateTime) {
        Object[] listeners = listenerList.getListenerList();
        CalendarEmptyClickEvent calendarEmptyClickEvent;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CalendarEmptyClickListener.class) {
                calendarEmptyClickEvent = new CalendarEmptyClickEvent(this, dateTime);
                ((CalendarEmptyClickListener) listeners[i + 1]).calendarEmptyClick(calendarEmptyClickEvent);
            }
        }
    }

    private void calculateScaleVars() {
        int width = getWidth();
        int height = getHeight();

        if (width < MIN_WIDTH) {
            width = MIN_WIDTH;
        }

        if (height < MIN_HEIGHT) {
            height = MIN_HEIGHT;
        }

        // Units are pixels per second
        timeScale = (double) (height - HEADER_HEIGHT) / (END_TIME.toSecondOfDay() - START_TIME.toSecondOfDay());
        dayWidth = (width - TIME_COL_WIDTH) / numDaysToShow();
    }

    protected abstract int numDaysToShow();

    // Gives x val of left most pixel for day col
    protected abstract double dayToPixel(DayOfWeek dayOfWeek);

    private double timeToPixel(LocalTime time) {
        return ((time.toSecondOfDay() - START_TIME.toSecondOfDay()) * timeScale) + HEADER_HEIGHT;
    }

    private LocalTime pixelToTime(double y) {
        return LocalTime.ofSecondOfDay((int) ((y - HEADER_HEIGHT) / timeScale) + START_TIME.toSecondOfDay()).truncatedTo(ChronoUnit.MINUTES);
    }

    private DayOfWeek pixelToDay(double x) {
        double pixel;
        DayOfWeek day;
        for (int i = getStartDay().getValue(); i <= getEndDay().getValue(); i++) {
            day = DayOfWeek.of(i);
            pixel = dayToPixel(day);
            if (x >= pixel && x < pixel + dayWidth) {
                return day;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        calculateScaleVars();
        g2 = (Graphics2D) g;

        // Rendering hints try to turn anti-aliasing on which improves quality
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set background to white
        g2.setColor(Color.black);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Set paint colour to black
        g2.setColor(Color.white);

        drawDayHeadings();
        drawTodayShade();
        drawGrid();
        drawTimes();
        drawEvents();
        drawCurrentTimeLine();
    }

    // protected void paintComponent(Graphics g, String color) {
    //     calculateScaleVars();
    //     g2 = (Graphics2D) g;

    //     // Rendering hints try to turn anti-aliasing on which improves quality
    //     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    //     if(color.equalsIgnoreCase("Light")) {
    //     // Set background to white
    //     g2.setColor(Color.white);
    //     g2.fillRect(0, 0, getWidth(), getHeight());

    //     // Set paint colour to black
    //     g2.setColor(Color.black);
    //     }

    //     else if (color.equalsIgnoreCase("Dark")){
    //         g2.setColor(Color.black);
    //         g2.fillRect(0, 0, getWidth(), getHeight());
    
    //         // Set paint colour to black
    //         g2.setColor(Color.white);
    //     }
    //     repaint();
    //     drawDayHeadings();
    //     drawTodayShade();
    //     drawGrid();
    //     drawTimes();
    //     drawEvents();
    //     drawCurrentTimeLine();
        
    // }

    protected abstract DayOfWeek getStartDay();

    protected abstract DayOfWeek getEndDay();

    private void drawDayHeadings() {
        int y = 20;
        int x;
        LocalDate day;
        DayOfWeek dayOfWeek;

        for (int i = getStartDay().getValue(); i <= getEndDay().getValue(); i++) {
            dayOfWeek = DayOfWeek.of(i);
            day = getDateFromDay(dayOfWeek);

            String text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + day.getDayOfMonth() + "/" + day.getMonthValue();
            x = (int) (dayToPixel(DayOfWeek.of(i)) + (dayWidth / 2) - (FONT_LETTER_PIXEL_WIDTH * text.length() / 2));
            g2.drawString(text, x, y);
        }
    }

package com;

import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.*;


public class CalendarEvent {
    public class CalendarEventEditor extends JFrame {

        // Other code
    
        private JButton matchButton;
        private CalendarEvent event;
    
        public CalendarEventEditor(CalendarEvent event) {
            super("Edit Event");
            this.event = event;
            initComponents();
        }
    
        private void initComponents() {
    
            // Other code
    
            matchButton = new JButton("Match Availability");
            matchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    matchAvailability();
                }
            });
    
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(matchButton);
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
    
            getContentPane().add(inputPanel, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        }
    
        private void matchAvailability() {
            // TODO: Implement matching availability functionality here
            System.out.println("Match availability button clicked.");
        }
    }
    
    private static final Color DEFAULT_COLOR = Color.PINK;

    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private String text;
    private Color color;

    public CalendarEvent(LocalDate date, LocalTime start, LocalTime end, String text) {
        this(date, start, end, text, DEFAULT_COLOR);
    }

    public CalendarEvent(LocalDate date, String text) {
        this(date, text, DEFAULT_COLOR);
    }

    public CalendarEvent(LocalDate date, LocalTime start, LocalTime end, String text, Color color) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.text = text;
        this.color = color;
    }

    public CalendarEvent(LocalDate date, String text, Color color) {
        this.date = date;
        this.text = text;
        this.color = color;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return getDate() + " " + getStart() + "-" + getEnd() + ". " + getText();
    }

    public Color getColor() {
        return color;
    }
    public boolean getEndsBefore(LocalTime time) {
        return this.getEnd().isBefore(time);
    }
    
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarEvent that = (CalendarEvent) o;

        if (!date.equals(that.date)) return false;
        if (!start.equals(that.start)) return false;
        return end.equals(that.end);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }
  

public class CalendarEventEditor extends JFrame {

    private CalendarEvent event;

    private JTextField dateField;
    private JTextField startField;
    private JTextField endField;
    private JComboBox<String> formatBox;
    private JButton saveButton;
    private JButton cancelButton;

    public CalendarEventEditor(CalendarEvent event) {
        super("Edit Event");
        this.event = event;
        initComponents();
    }

    private void initComponents() {
        dateField = new JTextField(10);
        dateField.setText(event.getDate().toString());
        startField = new JTextField(5);
        startField.setText(event.getStart().toString());
        endField = new JTextField(5);
        endField.setText(event.getEnd().toString());
        formatBox = new JComboBox<>(new String[] {"12-hour", "24-hour"});
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEvent();
            }
        });
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        inputPanel.add(new JLabel("Date:"), constraints);
        constraints.gridx = 1;
        inputPanel.add(dateField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        inputPanel.add(new JLabel("Start Time:"), constraints);
        constraints.gridx = 1;
        inputPanel.add(startField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        inputPanel.add(new JLabel("End Time:"), constraints);
        constraints.gridx = 1;
        inputPanel.add(endField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        inputPanel.add(new JLabel("Format:"), constraints);
        constraints.gridx = 1;
        inputPanel.add(formatBox, constraints);
}

    private void saveEvent() {
        LocalDate date = LocalDate.parse(dateField.getText());
        LocalTime start = LocalTime.parse(startField.getText());
        LocalTime end = LocalTime.parse(endField.getText());
        String format = (String) formatBox.getSelectedItem();

        // Convert time format to 24-hour if necessary
        if (format.equals("12-hour")) {
            start = start.withHour(start.getHour() + (start.getHour() < 12 ? 12 : 0));
            end = end.withHour(end.getHour() + (end.getHour() < 12 ? 12 : 0));
        }

        event.setDate(date);
        event.setStart(start);
        event.setEnd(end);

        // TODO: Save the event to a data store or perform any necessary actions here
        System.out.println("Event saved: " + event.toString());

        dispose();
    }
}


public boolean check_Conflict(CalendarEvent newEvent) {
    if (!date.equals(newEvent.date)) {
        return false;
    }
    if (end.isBefore(newEvent.start) || start.isAfter(newEvent.end)) {
        return false;
    }
        return true;
    }

}

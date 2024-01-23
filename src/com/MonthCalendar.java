package com;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.*;
import java.time.format.*;

public class MonthCalendar extends JFrame implements ActionListener {
    private JButton prevButton, nextButton;
    private JLabel monthLabel;
    private JPanel calendarPanel;
    private LocalDate currentDate;
    private DateTimeFormatter monthFormatter;
    LocalDate today = LocalDate.now();


    public MonthCalendar() {
        super("Month View");

        // Initialize the current date to the current month and year
        currentDate = LocalDate.now().withDayOfMonth(1);

        // Set up the date formatter to display the month and year
        monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        // Create the previous and next buttons
        prevButton = new JButton("<<");
        prevButton.addActionListener(this);
        nextButton = new JButton(">>");
        nextButton.addActionListener(this);

        // Create the month label
        monthLabel = new JLabel(currentDate.format(monthFormatter), JLabel.CENTER);
        //monthLabel.setBackground(Color.BLACK);

        // Create the calendar panel
        calendarPanel = new JPanel(new GridLayout(0, 7));
        calendarPanel.setBackground(Color.BLACK);
        addDaysOfWeek(calendarPanel);
        addCalendarDays(calendarPanel);

        // Add the components to the frame
        JPanel panel = new JPanel(new BorderLayout());
//        Color gray = new Color(128, 128, 128);
//
//        panel.setBackground(gray);
//        panel.setForeground(Color.WHITE);
        panel.add(prevButton, BorderLayout.WEST);
        panel.add(monthLabel, BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.EAST);
        add(panel, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);

        // Set the frame properties
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addDaysOfWeek(JPanel panel) {
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel label = new JLabel(day, JLabel.CENTER);
            label.setForeground(Color.WHITE);
            panel.add(label);
        }
    }

    private void addCalendarDays(JPanel panel) {
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        int startColumn = firstDayOfMonth.getDayOfWeek().getValue();
        int endColumn = startColumn + lastDayOfMonth.getDayOfMonth() - 1;

        int currentColumn = 0;
        for (int i = 1; i <= endColumn; i++) {
            if (i < startColumn) {
                panel.add(new JLabel());
                currentColumn++;
            } else {
                int dayOfMonth = i - startColumn + 1;
                JButton button = new JButton(String.valueOf(dayOfMonth));
                button.addActionListener(this);
                if (currentDate.withDayOfMonth(dayOfMonth).equals(today)) {
                    JLabel label = new JLabel(String.valueOf(dayOfMonth), JLabel.CENTER);
                    label.setBackground(Color.RED);
                    label.setOpaque(true);
                    panel.add(label);
                } else {
                    panel.add(button);
                }
                currentColumn++;
            }
        
            if (currentColumn == 7) {
                currentColumn = 0;
            }
        }
        
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prevButton) {
            currentDate = currentDate.minusMonths(1);
        } else if (e.getSource() == nextButton) {
            currentDate = currentDate.plusMonths(1);
        } else {
            // Handle button clicks for individual days here
            System.out.println("Clicked on day " + e.getActionCommand());
        }

        // Update the month label and calendar panel
        monthLabel.setText(currentDate.format(monthFormatter));
        calendarPanel.removeAll();
        addDaysOfWeek(calendarPanel);
        addCalendarDays(calendarPanel);
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
}
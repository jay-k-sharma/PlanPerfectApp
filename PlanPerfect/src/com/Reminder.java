package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Reminder extends JFrame {
    
    private JButton addButton;
    private ArrayList<CalendarEvent> events;

    public Reminder(ArrayList<CalendarEvent> events) {
        super("Add a Reminder");

        this.events = events;

        // Reminder button
        addButton = new JButton("Add a Reminder");
        addButton.addActionListener(new AddReminderListener());

        // Panel to show reminder button
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(addButton, BorderLayout.CENTER);

        // Add panel to Calendar
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class AddReminderListener implements ActionListener {

        private JTextField reminderNameField;
        private JSpinner spinnerDate;
        private JSpinner spinnerTime;

        private AddReminderListener() {
            reminderNameField = new JTextField(20);

            SpinnerDateModel modelDate = new SpinnerDateModel();
            spinnerDate = new JSpinner(modelDate);
            spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "MM/dd/yyyy"));

            SpinnerDateModel modelTime = new SpinnerDateModel();
            spinnerTime = new JSpinner(modelTime);
            spinnerTime.setEditor(new JSpinner.DateEditor(spinnerTime, "hh:mm a"));
        }

        // User can input name, date and time of reminder
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Reminder name:"));
            panel.add(reminderNameField);
            panel.add(new JLabel("Date:"));
            panel.add(spinnerDate);
            panel.add(new JLabel("Time:"));
            panel.add(spinnerTime);

            int result = JOptionPane.showConfirmDialog(Reminder.this, panel, "Add a Reminder",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String reminderName = reminderNameField.getText();
                Date date = (Date) spinnerDate.getValue();
                LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                        .plusHours(((Date) spinnerTime.getValue()).getHours())
                        .plusMinutes(((Date) spinnerTime.getValue()).getMinutes());
                events.add(new CalendarEvent(dateTime.toLocalDate(), dateTime.toLocalTime(),
                        dateTime.toLocalTime().plusMinutes(30), reminderName));
            }
        }
    }
}

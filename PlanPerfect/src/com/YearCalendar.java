// package com;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.time.YearMonth;
// import java.time.format.DateTimeFormatter;

// public class YearCalendar extends JFrame {

//     private int currentYear;
//     private JLabel yearLabel;
//     private JPanel mainPanel;

//     public YearCalendar(int year) {
//         currentYear = year;
//         setTitle("Year View");
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         JPanel headerPanel = new JPanel(new BorderLayout());
//         JButton previousButton = new JButton("<");
//         previousButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 currentYear--;
//                 yearLabel.setText(String.valueOf(currentYear));
//                 updateCalendar();
//             }
//         });
//         headerPanel.add(previousButton, BorderLayout.WEST);
//         JButton nextButton = new JButton(">");
//         nextButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 currentYear++;
//                 yearLabel.setText(String.valueOf(currentYear));
//                 updateCalendar();
//             }
//         });
//         headerPanel.add(nextButton, BorderLayout.EAST);
//         yearLabel = new JLabel(String.valueOf(currentYear), JLabel.CENTER);
//         headerPanel.add(yearLabel, BorderLayout.CENTER);

//         mainPanel = new JPanel(new GridLayout(3, 4));
//         updateCalendar();

//         add(headerPanel, BorderLayout.NORTH);
//         add(mainPanel, BorderLayout.CENTER);
//         pack();
//         setLocationRelativeTo(null);
//         setVisible(true);
//     }

//     private void updateCalendar() {
//         mainPanel.removeAll();

//         String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

//         for (int i = 0; i < 12; i++) {
//             JPanel monthPanel = new JPanel(new GridLayout(0, 7));
//             JLabel monthLabel = new JLabel(months[i], JLabel.CENTER);
//             monthPanel.add(monthLabel);
//             monthPanel.add(new JLabel("S", JLabel.CENTER));
//             monthPanel.add(new JLabel("M", JLabel.CENTER));
//             monthPanel.add(new JLabel("T", JLabel.CENTER));
//             monthPanel.add(new JLabel("W", JLabel.CENTER));
//             monthPanel.add(new JLabel("T", JLabel.CENTER));
//             monthPanel.add(new JLabel("F", JLabel.CENTER));
//             monthPanel.add(new JLabel("S", JLabel.CENTER));

//             YearMonth yearMonth = YearMonth.of(currentYear, i + 1);
//             int numDays = yearMonth.lengthOfMonth();
//             int firstDay = yearMonth.atDay(1).getDayOfWeek().getValue();

//             for (int j = 1; j < firstDay; j++) {
//                 monthPanel.add(new JLabel(""));
//             }

//             for (int j = 1; j <= numDays; j++) {
//                 JLabel dayLabel = new JLabel(String.valueOf(j), JLabel.CENTER);
//                 monthPanel.add(dayLabel);
//             }

//             mainPanel.add(monthPanel);
//         }

//         validate();
//     }

// }

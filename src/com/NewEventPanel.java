package com;
import java.awt.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
 
import javax.swing.*;
 
import static java.awt.GridBagConstraints.BASELINE_LEADING;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.HORIZONTAL;
public final class NewEventPanel extends JPanel {
 
  private static final long serialVersionUID = 1L;
 
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
    .ofLocalizedDate(FormatStyle.FULL)
    .withLocale(Locale.US);
 
  private final LocalDate date;
 
  private final JTextField titleField = new JTextField();
 
  private final JTextArea descriptionArea = new JTextArea();
 
  public NewEventPanel(LocalDate date) {
    super(new GridBagLayout());
 
    if (date == null)
      throw new IllegalArgumentException();
 
    this.date = date;
 
    initializeComponents();
  }
 
  LocalDate date() {
    return date;
  }
 
  public String title() {
    return titleField.getText();
  }
 
  public String description() {
    return descriptionArea.getText();
  }
 
  private void initializeComponents() {
 
    var dateField = new JTextField(date.format(DATE_FORMATTER));
    dateField.setEditable(false);
 
    descriptionArea.setPreferredSize(new Dimension(300, 150));
    var descriptionPane = new JScrollPane(descriptionArea);
 
    var constraints = new GridBagConstraints();
    constraints.anchor = BASELINE_LEADING;
    constraints.insets = new Insets(4, 4, 4, 4);
 
    constraints.gridx = 0;
    add(new JLabel("Date:"),        constraints);
    add(new JLabel("Title:"),       constraints);
    add(new JLabel("Description:"), constraints);
 
    constraints.gridx   = 1;
    constraints.weightx = 1;
    constraints.fill    = HORIZONTAL;
    add(dateField,  constraints);
    add(titleField, constraints);
 
    constraints.weighty = 1;
    constraints.fill    = BOTH;
    add(new JScrollPane(descriptionPane), constraints);
  }
}
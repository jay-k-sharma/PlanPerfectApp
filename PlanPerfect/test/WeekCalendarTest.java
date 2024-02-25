import com.Calendar;
import com.CalendarEvent;
import com.TimeSlot;
import com.WeekCalendar;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class WeekCalendarTest {
	public static void main(String[] args) {
		
		JFrame frm = new JFrame();
        frm.setTitle("Plan Perfect");
		JPanel weekControls = new JPanel();
		ArrayList<CalendarEvent> events = new ArrayList<>();

		 
		String url = "jdbc:mysql://localhost:3306/CA_Public_Holidays"; 
		String user = "root";
		String password = "EECS2311"; // replace ... with your password 
		
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			String[] queries = {"SELECT * FROM 2023_Holidays;", "SELECT * FROM 2024_Holidays;", "SELECT * FROM 2025_Holidays;"};
		
			for (String query : queries) {
				try (Statement statement = con.createStatement(); ResultSet result = statement.executeQuery(query)) {
					while (result.next()) { 
						String holiday_Name = result.getString("Holiday_Name");
						int day = result.getInt("day");
						int month = result.getInt("month");
						int year = result.getInt("year");
		
						events.add(new CalendarEvent(LocalDate.of(year, month, day), LocalTime.of(8, 0), LocalTime.of(8, 20), holiday_Name));
					}
				}
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}


		WeekCalendar cal = new WeekCalendar(events);
		

		cal.addCalendarEventClickListener(e -> System.out.println(e.getCalendarEvent()));
		cal.addCalendarEmptyClickListener(e -> {
			System.out.println(e.getDateTime());
			System.out.println(Calendar.roundTime(e.getDateTime().toLocalTime(), 30));
		});

		JButton goToTodayBtn = new JButton("Today");
		goToTodayBtn.addActionListener(e -> cal.goToToday());

		JButton nextWeekBtn = new JButton(">");
		nextWeekBtn.addActionListener(e -> cal.nextWeek());

		JButton prevWeekBtn = new JButton("<");
		prevWeekBtn.addActionListener(e -> cal.prevWeek());

//This is setting button, inside that button we are giving user to customize different things
		JButton SettingsButton = new JButton("Settings");
		SettingsButton.addActionListener(e -> {

	  Object[] GivenOptions = {"Font Type", "Font Size","Export"};
	  int Choosedchoice = JOptionPane.showOptionDialog(frm, "", "Settings", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null, GivenOptions,GivenOptions[0]);
	  if (Choosedchoice == 1) {

		  String[] fontsizes = {"10", "12", "14", "16", "18"};
		  String Size = (String) JOptionPane.showInputDialog(frm, "Select the font size", "Font Sizes", JOptionPane.PLAIN_MESSAGE, null, fontsizes, fontsizes[0]);
		  if (Size != null) {
			  cal.setFontSize(Integer.parseInt(Size));
			  
		  }
	  } 
	  else if (Choosedchoice  == 0) {
		  String[] fontTypes = {"Arial","Times New Roman", "Helvetica", "Courier New", "Verdana", "Lucida Console","Tahoma","Georgia" };
		 
		  String Type = (String) JOptionPane.showInputDialog(frm, "Select Font Type", "Font Type", JOptionPane.PLAIN_MESSAGE, null, fontTypes, fontTypes[0]);
		  if (Type != null) {
			  cal.setFontType(Type);
			  
		  }

	  }else if (Choosedchoice == 2) {
        BufferedImage image = new BufferedImage(frm.getWidth(), frm.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Render the calendar to the image
        frm.paint(g2d);
        
        // Dispose of the Graphics2D object to free up resources
        g2d.dispose();
        
        // Save the image to a file
        try {
            File output = new File("calendar.png");
            ImageIO.write(image, "png", output);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

  });
  JButton EventsButton = new JButton("Events");

  EventsButton.addActionListener(e -> {
	Object[] GivenOptions = {"Completed Events", "Add Events","Delete Event"};
	int Choosedchoice = JOptionPane.showOptionDialog(frm, "", "Events", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null, GivenOptions,GivenOptions[0]);
	if (Choosedchoice == 0) {
	  
		//Display completed events

	  ArrayList<CalendarEvent> EventsPassed = cal.getEventAlreadyPassed();
	  if (EventsPassed.isEmpty()) {
		  JOptionPane.showMessageDialog(frm, "NO EVENT/REMINDERS PASSED");
	  } else {
		  StringBuilder stringBuilder = new StringBuilder();
		  for (CalendarEvent event : EventsPassed) {
			  stringBuilder.append(event.toString()).append("\n");
		  }
		  JOptionPane.showMessageDialog(frm, stringBuilder.toString(), "Passed Events/Reminders", JOptionPane.PLAIN_MESSAGE);
	  }
	}else if (Choosedchoice == 1) {
	  //Add new event
	  JTextField EventName = new JTextField(20);
	  JTextField Day = new JTextField(10);
	  JTextField start_Time = new JTextField(6);
	  JTextField end_Time = new JTextField(6);
	  JTextField location= new JTextField(20);
	  JPanel AddEvent_panel = new JPanel(new GridLayout(0, 2));
	  AddEvent_panel.add(new JLabel("Name"));
	  AddEvent_panel.add(EventName);
	  AddEvent_panel.add(new JLabel("Year/Month/Day (Format: YYYY-MM-DD)"));
	  AddEvent_panel.add(Day);
	  AddEvent_panel.add(new JLabel("Start Time (Format: HH:mm)"));
	  AddEvent_panel.add(start_Time);
	  AddEvent_panel.add(new JLabel("End Time (Format: HH:mm)"));
	  AddEvent_panel.add(end_Time);
	  AddEvent_panel.add(new JLabel("Location of Event"));
	  AddEvent_panel.add(location);
	  int Display = JOptionPane.showConfirmDialog(null, AddEvent_panel, "Add the Event",
		  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	  if (Display == JOptionPane.OK_OPTION) {
		  String name = EventName.getText();
		  LocalDate startDate = LocalDate.parse(Day.getText());
		  LocalTime startTime = LocalTime.parse(start_Time.getText());
		  LocalTime endTime = LocalTime.parse(end_Time.getText());
		  CalendarEvent newEvent = new CalendarEvent(startDate, startTime, endTime, name);
		  events.add(newEvent);
		  cal.repaint();
	  }
	}else if (Choosedchoice == 2) {
	  //Delete event
	  JTextField EnterName = new JTextField(30);
	  JPanel DELETE_EVENT_Panel = new JPanel(new GridLayout(1, 1));
	  DELETE_EVENT_Panel.add(new JLabel("Enter Event Name to Delete: "));
	  DELETE_EVENT_Panel.add(EnterName);
	  int Result = JOptionPane.showConfirmDialog(null, DELETE_EVENT_Panel, "Delete the Event",
		  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	  if (Result == JOptionPane.OK_OPTION) {
		  String NAME_OF_EVENT = EnterName.getText();
		  events.removeIf(event -> event.getText().equals(NAME_OF_EVENT));
		  cal.repaint();
	  }
	}
  });
  

		JPanel weekControls = new JPanel();
		//weekControls.add(ADD_EVENT_BUTTON); //Adding "ADD_EVENT_BUTTON" in the GUI
		//weekControls.add(DELETE_EVENT_BUTTON);//Adding "DELETE_EVENT_BUTTON" in the GUI
		weekControls.add(EventsButton); //Adding "Completed Events" in the GUI
		
		//weekControls.add(prevMonthBtn);
		weekControls.add(prevWeekBtn);
		weekControls.add(goToTodayBtn);
		weekControls.add(nextWeekBtn);
		//weekControls.add(nextMonthBtn);
		
		

		weekControls.add(prevWeekBtn);
		weekControls.add(goToTodayBtn);
		weekControls.add(nextWeekBtn);

		weekControls.add(SettingsButton);
      


		frm.add(weekControls, BorderLayout.NORTH);
		

		frm.add(cal, BorderLayout.CENTER);
		frm.setSize(800, 800);
		frm.setVisible(true);
		frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		
	}
}




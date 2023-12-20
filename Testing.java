import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.*;
import com.Calendar;


import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Testing {

    @Test
    public void testGetStart() {
      LocalTime start = LocalTime.of(9, 0);
      LocalTime end = LocalTime.of(10, 0);
      TimeSlot ts = new TimeSlot(start, end);
      assertEquals(start, ts.getStart());
    }
  
    @Test
    public void testGetEnd() {
      LocalTime start = LocalTime.of(9, 0);
      LocalTime end = LocalTime.of(10, 0);
      TimeSlot ts = new TimeSlot(start, end);
      assertEquals(end, ts.getEnd());
    }
  
    @Test
    public void testGetStartOfWeek() {
        // Test Monday
        LocalDate date = LocalDate.of(2023, 4, 10); // Sunday
        LocalDate expected = LocalDate.of(2023, 4, 10); // Monday
        LocalDate actual = Week.getStartOfWeek(date);
        assertEquals(expected, actual);

        // Test Friday
        date = LocalDate.of(2023, 4, 14); // Friday
        expected = LocalDate.of(2023, 4, 10); // Monday
        actual = Week.getStartOfWeek(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDay() {
        Week week = new Week(LocalDate.of(2023, 4, 10)); // Week starting from Monday, Apr 10
        LocalDate expected = LocalDate.of(2023, 4, 10); // Monday
        LocalDate actual = week.getDay(DayOfWeek.MONDAY);
        assertEquals(expected, actual);

        expected = LocalDate.of(2023, 4, 15); // Saturday
        actual = week.getDay(DayOfWeek.SATURDAY);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextWeek() {
        Week week = new Week(LocalDate.of(2023, 4, 10)); // Week starting from Monday, Apr 10
        Week expected = new Week(LocalDate.of(2023, 4, 21)); // Week starting from Monday, Apr 17
        Week actual = week.nextWeek();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testPrevWeek() {
        Week week = new Week(LocalDate.of(2023, 4, 10)); // Week starting from Monday, Apr 10
        Week expected = new Week(LocalDate.of(2023, 4, 03));
        Week actual = week.prevWeek();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Week week = new Week(LocalDate.of(2023, 4, 10)); // Week starting from Monday, Apr 10
        String expected = "Week of the 2023-04-10";
        String actual = week.toString();
        assertEquals(expected, actual);
    }


    @Test
    public void testDateInRange() {
        LocalDate date = LocalDate.of(2023, 4, 10);
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        assertTrue(calendar.dateInRange(date));
    }

    @Test
    public void testGetDateFromDay() {
        LocalDate date = LocalDate.of(2023, 4, 10);
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        assertEquals(date, calendar.getDateFromDay(DayOfWeek.SUNDAY));
    }

    @Test
    public void testNumDaysToShow() {
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        assertEquals(1, calendar.numDaysToShow());
    }

    @Test
    public void testGetStartDay() {
        LocalDate date = LocalDate.of(2023, 4, 10);
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        assertEquals(date.getDayOfWeek(), calendar.getStartDay());
    }

    @Test
    public void testGetEndDay() {
        LocalDate date = LocalDate.of(2023, 4, 10);
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        assertEquals(date.getDayOfWeek(), calendar.getEndDay());
    }

    @Test
    public void testSetRangeToToday() {
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        LocalDate today = LocalDate.now();
        calendar.setRangeToToday();
        assertEquals(today, calendar.calDate);
    }

    @Test
    public void testDayToPixel() {
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        assertEquals(Calendar.TIME_COL_WIDTH, calendar.dayToPixel(DayOfWeek.MONDAY), 0.01);
    }


    @Test
    public void testNextDay() {
        LocalDate today = LocalDate.now();
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        calendar.nextDay();
        assertEquals(today.plusDays(1), calendar.calDate);
    }

    @Test
    public void testPrevDay() {
        LocalDate today = LocalDate.now();
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        calendar.prevDay();
        assertEquals(today.minusDays(1), calendar.calDate);
    }

    @Test
    public void testUpdateAndRepaint() {
        // Test that this method doesn't throw an exception
        DayCalendar calendar = new DayCalendar(new ArrayList<>());
        calendar.updateAndRepaint();
    }

    private Reminder reminder;

    @Before
    public void setUp() throws Exception {
        reminder = new Reminder("Do laundry", LocalDateTime.of(2023, 4, 15, 12, 0));
    }

    @Test
    public void testGetName() {
        assertEquals("Do laundry", reminder.getName());
    }

    @Test
    public void testSetName() {
        reminder.setName("Buy groceries");
        assertEquals("Buy groceries", reminder.getName());
    }

    @Test
    public void testGetDateTime() {
        assertEquals(LocalDateTime.of(2023, 4, 15, 12, 0), reminder.getDateTime());
    }

    @Test
    public void testSetDateTime() {
        LocalDateTime newDateTime = LocalDateTime.of(2023, 4, 16, 10, 0);
        reminder.setDateTime(newDateTime);
        assertEquals(newDateTime, reminder.getDateTime());
    }

    @Test
    public void testIsCompleted() {
        assertFalse(reminder.isCompleted());
    }

    @Test
    public void testSetCompleted() {
        reminder.setCompleted(true);
        assertTrue(reminder.isCompleted());
    }

    private ArrayList<CalendarEvent> events;

    @Before
    public void SQLDBsetUp() {
        String url = "jdbc:mysql://localhost:3306/CA_Public_Holidays";
        String user = "root";
        String password = "EECS2311"; // replace ... with your password

        events = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String[] queries = { "SELECT * FROM 2023_Holidays;", "SELECT * FROM 2024_Holidays;", "SELECT * FROM 2025_Holidays;" };

            for (String query : queries) {
                try (Statement statement = con.createStatement();
                        ResultSet result = statement.executeQuery(query)) {
                    while (result.next()) {
                        String holiday_Name = result.getString("Holiday_Name");
                        int day = result.getInt("day");
                        int month = result.getInt("month");
                        int year = result.getInt("year");

                        events.add(new CalendarEvent(LocalDate.of(year, month, day), LocalTime.of(8, 0),
                                LocalTime.of(9, 0), holiday_Name));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDayCalendar() {
        DayCalendar cal = new DayCalendar(events);
        assertNotNull(cal);
    }

    @Test
    public void testWeekCalendar() {
        WeekCalendar cal = new WeekCalendar(events);
        assertNotNull(cal);
    }

    



}




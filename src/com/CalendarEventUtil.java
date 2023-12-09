package com;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class CalendarEventUtil {
    public static List<LocalDateTime> getRepeatedDates(LocalDateTime start, LocalDateTime end,int intervalInDays){
        List<LocalDateTime> repeatedDates = new ArrayList<>();
        LocalDateTime currentDate = start;
       
    while (currentDate.isBefore(end)) {
      repeatedDates.add(currentDate);
      currentDate = currentDate.plusDays(intervalInDays);
    }
      
      
          return repeatedDates;
        }
       
      }
    

    


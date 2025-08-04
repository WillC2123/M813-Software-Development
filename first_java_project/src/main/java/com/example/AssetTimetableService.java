package com.example;

import java.util.*;

/**
 * AssetTimetableService is responsible for managing the timetables of equipment and DJs, 
 * which are stored in maps with their IDs as keys and lists of reserved dates as values.
 * It can then check for date conflicts and update the timetables accordingly.
 */

public class AssetTimetableService {

    public Map<String, List<Date>> equipmentTimetable = new HashMap<>();
    public Map<String, List<Date>> djTimetable = new HashMap<>();

    public boolean hasDateConflict(Object asset, Date eventDate, List<Date> monitorDates) {
        String id = (asset instanceof Equipment) ? ((Equipment) asset).getId() : ((DJ) asset).getId();
        List<Date> reservedDates = (asset instanceof Equipment) ? equipmentTimetable.getOrDefault(id, new ArrayList<>())
                                                                : djTimetable.getOrDefault(id, new ArrayList<>());

        return reservedDates.stream().anyMatch(date -> monitorDates.contains(date) || date.equals(eventDate));
    }

    public void updateAssetTimetable(Booking booking, List<?> assets, Date eventDate) {
        for (Object asset : assets) {
            String id = (asset instanceof Equipment) ? ((Equipment) asset).getId() : ((DJ) asset).getId();
            if (asset instanceof Equipment) {
                equipmentTimetable.computeIfAbsent(id, k -> new ArrayList<>()).add(eventDate);
            } else if (asset instanceof DJ) {
                djTimetable.computeIfAbsent(id, k -> new ArrayList<>()).add(eventDate);
            }
        }
    }
}

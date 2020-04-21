package com.arimac.backend.pmtool.projectmanagementtool.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class UtilsService {

    public String getUUId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public Timestamp getCurrentTimestamp(){
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        return new Timestamp(currentDateAndTime.toDateTime().getMillis());
    }

    public DateTime getCurrentDateTime(){
        DateTime now = DateTime.now();
        DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
        return nowCol;
    }
}

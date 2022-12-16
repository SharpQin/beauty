package cc.microthink.order;

import java.time.*;
import java.util.TimeZone;

public class DatetimeTest {

    public static void main(String[] args) {

        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        TimeZone timeZone = TimeZone.getDefault();
        System.out.println(timeZone);

        LocalDateTime beforeTime = LocalDateTime.now().minusMinutes(30);
        Instant instant = beforeTime.toInstant(ZoneOffset.UTC);  //ZoneOffset.UTC
        System.out.println(instant);

        ZonedDateTime zt = ZonedDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Instant inst = zt.toInstant();
        ZoneId instZoneId = zt.getZone();
        System.out.println(zt);
        System.out.println(zoneId);
        System.out.println(inst);
        System.out.println(instZoneId);

    }

}

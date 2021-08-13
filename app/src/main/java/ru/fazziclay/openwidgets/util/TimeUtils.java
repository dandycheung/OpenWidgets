package ru.fazziclay.openwidgets.util;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/*

%%     a literal %
%a     locale's abbreviated weekday name (e.g., Sun)
%A     locale's full weekday name (e.g., Sunday)
%b     locale's abbreviated month name (e.g., Jan)
%B     locale's full month name (e.g., January)
%c     locale's date and time (e.g., Thu Mar  3 23:05:25 2005)
%C     century; like %Y, except omit last two digits (e.g., 20)
%d     day of month (e.g., 01)
%D     date; same as %m/%d/%y
%e     day of month, space padded; same as %_d
%F     full date; like %+4Y-%m-%d
%g     last two digits of year of ISO week number (see %G)
%G     year of ISO week number (see %V); normally useful only
with %V
%h     same as %b
%H     hour (00..23)
%I     hour (01..12)
%j     day of year (001..366)
%k     hour, space padded ( 0..23); same as %_H
%l     hour, space padded ( 1..12); same as %_I
%m     month (01..12)
%M     minute (00..59)
%n     a newline
%N     nanoseconds (000000000..999999999)
%p     locale's equivalent of either AM or PM; blank if not known
%P     like %p, but lower case
%q     quarter of year (1..4)
%r     locale's 12-hour clock time (e.g., 11:11:04 PM)
%R     24-hour hour and minute; same as %H:%M
%s     seconds since 1970-01-01 00:00:00 UTC
%S     second (00..60)
%t     a tab
%T     time; same as %H:%M:%S
%u     day of week (1..7); 1 is Monday
%U     week number of year, with Sunday as first day of week
(00..53)
%V     ISO week number, with Monday as first day of week (01..53)
%w     day of week (0..6); 0 is Sunday
%W     week number of year, with Monday as first day of week
(00..53)
%x     locale's date representation (e.g., 12/31/99)
%X     locale's time representation (e.g., 23:13:48)
%y     last two digits of year (00..99)
%Y     year
%z     +hhmm numeric time zone (e.g., -0400)
%:z    +hh:mm numeric time zone (e.g., -04:00)
%::z   +hh:mm:ss numeric time zone (e.g., -04:00:00)
%:::z  numeric time zone with : to necessary precision (e.g.,
-04, +05:30)
%Z     alphabetic time zone abbreviation (e.g., EDT)
By default, date pads numeric fields with zeroes.  The following
optional flags may follow '%':
*/

public class TimeUtils {
    private static int h(int i) {
        i = i-1;
        if (i == 0) {
            i = 7;
        }
        return i;
    }

    private static String g(int i) {
        if (i < 10 && i >= 0) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    private static String gg(int i) {
        if (i < 10 && i >= 0) {
            return "00" + i;
        }
        if (i < 100 && i >= 0) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    private static String f(String source, String a, Object b) {
        return source.replace(a, b.toString());
    }
    
    public static String dateFormat(String source) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis()); // Experimental fix bug: console time not changed
        
        int nowMilliseconds = calendar.get(Calendar.MILLISECOND);
        int nowSeconds = calendar.get(Calendar.SECOND);
        int nowMinutes = calendar.get(Calendar.MINUTE);
        int nowHours24 = calendar.get(Calendar.HOUR_OF_DAY);
        int nowHours12 = calendar.get(Calendar.HOUR);

        // ------------------------ 100 HOUR FORMAT
        float _nowSeconds = (nowSeconds + (float) nowMilliseconds / 1000) * 100 /60;
        float _nowMinutes = (nowMinutes + (float) nowSeconds / 60) * 100 /60;
        float _nowHours24   = (nowHours24 + (float) nowMinutes / 60) * 100 /24;
        float _nowHours12   = (nowHours12 + (float) nowMinutes / 60) * 50 /12;
        // ------------------------
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.getDefault());

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekName = dateFormatSymbols.getWeekdays()[dayOfWeek];
        String dayOfWeekNameShort = dateFormatSymbols.getShortWeekdays()[dayOfWeek];

        int month = calendar.get(Calendar.MONTH);
        String monthName = dateFormatSymbols.getMonths()[month];
        String monthNameShort = dateFormatSymbols.getShortMonths()[month];

        source = f(source, "%%", "FAZZICLAY_PROJ_0_PROJ_FAZZICLAY");
        source = f(source, "%a", dayOfWeekNameShort);
        source = f(source, "%A", dayOfWeekName);
        source = f(source, "%b", monthNameShort);
        source = f(source, "%B", monthName);
        source = f(source, "%c", "?");
        source = f(source, "%C", "?");
        source = f(source, "%d", calendar.get(Calendar.DAY_OF_MONTH));
        source = f(source, "%D", "?");
        source = f(source, "%e", "?");
        source = f(source, "%F", "?");
        source = f(source, "%g", "?");
        source = f(source, "%G", "?");
        source = f(source, "%h", "?");
        source = f(source, "%H", g(nowHours24));
        source = f(source, "%I", g(nowHours12));
        source = f(source, "%j", calendar.get(Calendar.DAY_OF_YEAR));
        source = f(source, "%k", "?");
        source = f(source, "%l", "?");
        source = f(source, "%m", g(month+1));
        source = f(source, "%M", g(nowMinutes));
        source = f(source, "%n", "?");
        source = f(source, "%N", gg(calendar.get(Calendar.MILLISECOND)));
        source = f(source, "%p", "?");
        source = f(source, "%P", "?");
        source = f(source, "%q", "?");
        source = f(source, "%r", "?");
        source = f(source, "%R", "?");
        source = f(source, "%s", System.currentTimeMillis()/1000);
        source = f(source, "%S", g(nowSeconds));
        source = f(source, "%t", '\t');
        source = f(source, "%T", g(nowHours24)+":"+g(nowMinutes)+":"+g(nowSeconds));
        source = f(source, "%u", h(dayOfWeek));
        source = f(source, "%U", calendar.get(Calendar.WEEK_OF_YEAR));
        source = f(source, "%V", "?");
        source = f(source, "%w", dayOfWeek);
        source = f(source, "%W", "?");
        source = f(source, "%x", "?");
        source = f(source, "%X", "?");
        source = f(source, "%y", "?");
        source = f(source, "%Y", calendar.get(Calendar.YEAR));
        source = f(source, "%z", "?");
        source = f(source, "%:z", "?");
        source = f(source, "%::z", "?");
        source = f(source, "%:::z", "?");
        source = f(source, "%Z", "?");

        // 100th format
        source = f(source, "%.S", g(Math.round(_nowSeconds)));
        source = f(source, "%.M", g(Math.round(_nowMinutes)));
        source = f(source, "%.H", g(Math.round(_nowHours24)));
        source = f(source, "%.I", g(Math.round(_nowHours12)));

        source = f(source, "FAZZICLAY_PROJ_0_PROJ_FAZZICLAY", "%");

        return source;
    }
}

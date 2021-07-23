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
    private static String sss = "";

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

    private static void f(String a, Object b) {
        sss = sss.replace(a, b.toString());
    }
    
    public static String dateFormat(String source) {
        sss = source;
        
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        
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

        f("%%", "FAZZICLAY_PROJ_0_PROJ_FAZZICLAY");
        f("%a", dayOfWeekNameShort);
        f("%A", dayOfWeekName);
        f("%b", monthNameShort);
        f("%B", monthName);
        f("%c", "?");
        f("%C", "?");
        f("%d", calendar.get(Calendar.DAY_OF_MONTH));
        f("%D", "?");
        f("%e", "?");
        f("%F", "?");
        f("%g", "?");
        f("%G", "?");
        f("%h", "?");
        f("%H", g(nowHours24));
        f("%I", g(nowHours12));
        f("%j", calendar.get(Calendar.DAY_OF_YEAR));
        f("%k", "?");
        f("%l", "?");
        f("%m", g(calendar.get(Calendar.MONTH)));
        f("%M", g(nowMinutes));
        f("%n", "?");
        f("%N", calendar.get(Calendar.MILLISECOND));
        f("%p", "?");
        f("%P", "?");
        f("%q", "?");
        f("%r", "?");
        f("%R", "?");
        f("%s", System.currentTimeMillis()/1000);
        f("%S", g(nowSeconds));
        f("%t", '\t');
        f("%T", g(nowHours24)+":"+g(nowMinutes)+":"+g(nowSeconds));
        f("%u", h(dayOfWeek));
        f("%U", calendar.get(Calendar.WEEK_OF_YEAR));
        f("%V", "?");
        f("%w", dayOfWeek);
        f("%W", "?");
        f("%x", "?");
        f("%X", "?");
        f("%y", "?");
        f("%Y", calendar.get(Calendar.YEAR));
        f("%z", "?");
        f("%:z", "?");
        f("%::z", "?");
        f("%:::z", "?");
        f("%Z", "?");

        // 100th format
        f("%.S", g(Math.round(_nowSeconds)));
        f("%.M", g(Math.round(_nowMinutes)));
        f("%.H", g(Math.round(_nowHours24)));
        f("%.I", g(Math.round(_nowHours12)));

        f("FAZZICLAY_PROJ_0_PROJ_FAZZICLAY", "%");

        return sss;
    }
}

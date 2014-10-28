package pro.anton.averin.android.skeleton.utils;


        import android.content.Context;
import android.content.res.Resources;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by AAverin
 */
@SuppressWarnings("DefaultFileTemplate")
public class TimeUtils {

//    private SimpleDateFormat dateShort;
//    private SimpleDateFormat dateServer;
//    private SimpleDateFormat dateDayMonth;
//    private SimpleDateFormat dateText;
//    private SimpleDateFormat dateApi;
//    private SimpleDateFormat dateBirth;
//    private SimpleDateFormat timeShort;

    public TimeUtils(Context context) {
        Resources res = context.getResources();

//        dateBirth = new SimpleDateFormat(res.getString(R.string.date_birth));
//        dateShort = new SimpleDateFormat(res.getString(R.string.date_short));
//        dateShort.setTimeZone(TimeZone.getTimeZone(Config.DEFAULT_TIMEZONE));
//        dateApi = new SimpleDateFormat(res.getString(R.string.date_api));
//        dateApi.setTimeZone(TimeZone.getDefault());
//        dateDayMonth = new SimpleDateFormat(res.getString(R.string.date_daymonth));
//        dateDayMonth.setTimeZone(TimeZone.getTimeZone(Config.DEFAULT_TIMEZONE));
//        dateServer = new SimpleDateFormat(res.getString(R.string.date_server));
//        dateText = new SimpleDateFormat(res.getString(R.string.date_text));
//        timeShort = new SimpleDateFormat(res.getString(R.string.time_short));
//        timeShort.setTimeZone(TimeZone.getTimeZone(Config.DEFAULT_TIMEZONE));
    }

//    public String format_dateShort(Date date) {
//        return dateShort.format(date);
//    }
//
//    public String format_dateServer(Date date) {
//        return dateServer.format(date);
//    }
//
//    public String format_dateDayMonth(Date date) {
//        return dateDayMonth.format(date);
//    }
//
//    public String format_dateText(Date date) {
//        return dateText.format(date);
//    }
//
//    public String format_timeShort(Date date) {
//        return timeShort.format(date);
//    }
//
//    public String format_dateApi(Date date) {
//        return dateApi.format(date);
//    }
//
//    public String format_dateBirth(Date date) {
//        return dateBirth.format(date);
//    }

    public static Calendar getDatePart(Date date) {
        return getDatePart(date, false);
    }
    public static Calendar getDatePart(Date date, boolean withHours) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        if (!withHours) {
            cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
            cal.set(Calendar.MINUTE, 0);                 // set minute in hour
            cal.set(Calendar.SECOND, 0);                 // set second in minute
            cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second
        }

        return cal;                                  // return the date part
    }
    /**
     * This method also assumes endDate >= startDate
     **/
    public static long daysBetween(Date startDate, Date endDate) {
        return daysBetween(startDate, endDate, false);
    }
    public static long daysBetween(Date startDate, Date endDate, boolean withHours) {
        Calendar sDate = getDatePart(startDate, withHours);
        Calendar eDate = getDatePart(endDate, withHours);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }


}
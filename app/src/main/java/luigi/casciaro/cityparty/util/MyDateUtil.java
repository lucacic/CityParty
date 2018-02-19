package luigi.casciaro.cityparty.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import luigi.casciaro.cityparty.AppController;

public class MyDateUtil {

    private static String[] dateToday = getTodayDateArray();
    private static Date dateNow;

    /**
     * Stringa "Mese Anno"
     * @return "Giugno 2017"
     */
    public static String getMonthName(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // get name
        String nameOfCurrentMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALY);
        // upper case first char
        nameOfCurrentMonth = nameOfCurrentMonth.substring(0,1).toUpperCase() + nameOfCurrentMonth.substring(1).toLowerCase();
        // return
        return nameOfCurrentMonth.substring(0,3);
    }

    /**
     * Get an array with today date
     * @return ["dd","mm","yyyy"]
     */
    public static String[] getTodayDateArray() {
        Calendar c = Calendar.getInstance();
        dateToday = new String[3];
        dateNow = c.getTime();

        int gg = c.get(Calendar.DAY_OF_MONTH);
        int mm = c.get(Calendar.MONTH) + 1;
        int aaaa = c.get(Calendar.YEAR);

        if (gg >= 1 && gg <= 9)
            dateToday[0] = "0" + gg;
        else
            dateToday[0] = "" + gg;

        if (mm >= 1 && mm <= 9)
            dateToday[1] = "0" + mm;
        else
            dateToday[1] = "" + mm;

        dateToday[2] = "" + aaaa;

        return dateToday;
    }

    public static Date getDateNow() {
        return (dateNow != null) ? dateNow : Calendar.getInstance().getTime();
    }

    /**
     * Get a string representation of today array date
     * @return dd/mm/yyyy
     */
    public static String getTodayDate_toDisplay() {
        return dateToday[0].concat("/").concat(dateToday[1]).concat("/").concat(dateToday[2]);
    }

    /**
     * Get an array from day, month and year formatted
     * @param day
     * @param month
     * @param year
     * @return ["dd","mm","yyyy"]
     */
    public static String[] getDateArrayFromDayMonthYear(int day, int month, int year) {
        String[] dateArray = new String[3];

        if (day >= 1 && day <= 9)
            dateArray[0] = "0" + day;
        else
            dateArray[0] = "" + day;

        if (month >= 1 && month <= 9)
            dateArray[1] = "0" + month;
        else
            dateArray[1] = "" + month;

        dateArray[2] = "" + year;

        return dateArray;
    }

    /**
     * Get a string representation of date in array
     * @param day
     * @param month
     * @param year
     * @return day/month/year
     */
    public static String getDateArrayFromDayMonthYear_toDisplay(int day, int month, int year){
        String[] dateArray = getDateArrayFromDayMonthYear(day, month, year);
        return dateArray[0].concat("/").concat(dateArray[1]).concat("/").concat(dateArray[2]);
    }

    /**
     * Format Date object to yyyy-MM-dd String
     * @param date
     * @return yyyy-MM-dd date string
     */
    public static String getStringToDisplayFromDate(Date date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(AppController.ISO_FORMAT_INPUT);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a Date from yyyy-MM-dd format date
     * @param date
     * @return yyyy-MM-dd
     */
    public static Date getDateFromAmericanFormatString(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(AppController.ISO_FORMAT_INPUT_AMERICAN);
            return dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the first day of a month
     * @param date
     * @return Date of the first day of the Date in params
     */
    public static Date getFirstDayOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);// This is necessary to get proper results
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        return cal.getTime();
    }

    /**
     * Get the last day of a month
     * @param date
     * @return Date of the last day of the Date in params
     */
    public static Date getLastDayOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);// This is necessary to get proper results
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }

    public static String[] getDateArrayFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String[] dateArray = new String[3];

        int gg = cal.get(Calendar.DAY_OF_MONTH);
        int mm = cal.get(Calendar.MONTH) + 1;
        int aaaa = cal.get(Calendar.YEAR);

        if (gg >= 1 && gg <= 9)
            dateArray[0] = "0" + gg;
        else
            dateArray[0] = "" + gg;

        if (mm >= 1 && mm <= 9)
            dateArray[1] = "0" + mm;
        else
            dateArray[1] = "" + mm;

        dateArray[2] = "" + aaaa;

        return dateArray;
    }

    /**
     * Get a String to display from millis
     * @param millis
     * @return HH:mm:ss
     */
    public static String getMillisStringToDisplayFromInt(int millis){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }

    /**
     * Get a String to display from millis
     * @return dd/MM/yyyy hh:mm:ss
     */
    public static String getStringToDisplayDateAndTimeFromLong(Long milliSeconds){
        //String dateFormat = "dd/MM/yyyy hh:mm:ss.SSS";
        String dateFormat = "dd/MM/yyyy hh:mm:ss";

        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    /**
     *
     * @param date
     * @return 2018-01-18
     */
    public static String getYYYYMMddFromDate(Date date){
        // formatter
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        // Pass date object
        return formatter.format(date);
    }

    /**
     *
     * @param date
     * @return 2018-01-18
     */
    public static String getddMMYYYYFromDate(Date date){
        // formatter
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        // Pass date object
        return formatter.format(date);
    }

    /**
     *
     * @param date
     * @return 09:21
     */
    public static String getHHmmFromDate(Date date){
        // formatter
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
        // Pass date object
        return formatter.format(date);
    }

    /**
     * Check if two dates are equal or not
     * @param dateFirst
     * @param dateSecond
     * @param simpleDateFormatOfDates
     * @return true if dates are equal
     */
    public static boolean isDateEqual(Date dateFirst, Date dateSecond, SimpleDateFormat simpleDateFormatOfDates){
        return simpleDateFormatOfDates.format(dateFirst).equals(simpleDateFormatOfDates.format(dateSecond));
    }

    /**
     * Get an array of now time
     * @return [{hh},{mm}]
     */
    public static String[] getNowTimeArray() {
        Calendar c = Calendar.getInstance();
        String[] res = new String[2];

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if (hour >= 1 && hour <= 9)
            res[0] = "0" + hour;
        else
            res[0] = "" + hour;

        if (minute >= 1 && minute <= 9)
            res[1] = "0" + minute;
        else
            res[1] = "" + minute;

        return res;
    }

    /**
     * Get a string representation of now time array
     * @return HH:mm
     */
    public static String getNowTime_toDisplay() {
        return getNowTimeArray()[0].concat(":").concat(getNowTimeArray()[1]);
    }

    /**
     * Get a string representation of now time array
     * @return dd/MM/yyyy - HH:mm
     */
    public static String getNowDateTime_toDisplay() {
        return getTodayDate_toDisplay() + " - " + getNowTimeArray()[0].concat(":").concat(getNowTimeArray()[1]);
    }

    /**
     * Get an array from hours and minute formatted
     * @param hours
     * @param minute
     * @return ["HH","mm"]
     */
    public static String[] getNowTimeArrayFromHoursMinute(int hours, int minute) {
        String[] res = new String[2];
        res[0] = hours < 10 ? "0"+hours : ""+hours;
        res[1] = minute < 10 ? "0"+minute : ""+minute;
        return res;
    }

    /**
     * Get a string representation of time in array
     * @param hours
     * @param minute
     * @return HH:mm
     */
    public static String getNowTimeArrayFromHoursMinute_toDisplay(int hours, int minute) {
        String[] res = getNowTimeArrayFromHoursMinute(hours, minute);
        return res[0].concat(":").concat(res[1]);
    }

    /**
     * Get a string representation of date in input
     *
     * @param date 2013/08/14T08:46:21Z or 2017-01-05T15:10:00.000+0000
     * @return gg/mm/aaaa, H:M:S
     */
    public static String getDateAndTimeFromString_toDisplay(String date) {
        // data in arrivo: 2013-08-14T08:46:21Z o 16/08/2013, 22:24:51.000
        try {
            if (date.contains("-")) {
                return date.split("T")[0].split("-")[2] + "/" + date.split("T")[0].split("-")[1] + "/" + date.split("T")[0].split("-")[0] + ", " + date.split("T")[1].split(":")[0] + ":" + date.split("T")[1].split(":")[1];
            } else {
                return date.split("T")[0].split("/")[2] + "/" + date.split("T")[0].split("/")[1] + "/" + date.split("T")[0].split("/")[0] + ", " + date.split("T")[1].split(":")[0] + ":" + date.split("T")[1].split(":")[1];
            }
        } catch (Exception e) {
            return date;
        }
    }

    /**
     *
     * @param date [{gg},{MM},{aaaa}]
     * @return Date
     */
    public static Date getDateFromArray(String[] date) {
        try {
            // transform in yyyy-MM-dd'T'HH:mm | 2017-08-04T00:00:00
            String dateString = date[2] + "-" + date[1] + "-" + date[0] + "T00:00";
            SimpleDateFormat dateFormat = new SimpleDateFormat(AppController.ISO_FORMAT_BASE);
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check time è tra le 22:00 (>=) e le 07:00 (<) del giorno successivo
     */
    public static boolean isTimeBetween22And7(){

        try {
            // create 22:00:00 time
            String string22 = "22:00:00";
            Date time22 = new SimpleDateFormat("HH:mm:ss").parse(string22);
            Calendar calendar22 = Calendar.getInstance();
            calendar22.setTime(time22);

            // create 07:00:00 time
            String string7 = "07:00:00";
            Date time7 = new SimpleDateFormat("HH:mm:ss").parse(string7);
            Calendar calendar7 = Calendar.getInstance();
            calendar7.setTime(time7);
            calendar7.add(Calendar.DATE, 1);

            Calendar calendar3 = Calendar.getInstance();
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar22.getTime()) && x.before(calendar7.getTime())) {
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Add days to current date
     * @param numberOfDays
     * @return
     */
    public static Date getDateWithDayAdded(int numberOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }
}
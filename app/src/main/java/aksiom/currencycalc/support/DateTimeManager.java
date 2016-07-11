package aksiom.currencycalc.support;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Aksiom on 6/30/2016.
 */
public class DateTimeManager {

    public static final DateTimeFormatter HNB_DATE = DateTimeFormat.forPattern("YYYY-MM-dd");
    public static final DateTimeFormatter CHART_DATE = DateTimeFormat.forPattern("dd.MM.YYYY");

    public static String parseFromDate(LocalDate date , DateTimeFormatter format){

        return  format.print(date);
    }
}

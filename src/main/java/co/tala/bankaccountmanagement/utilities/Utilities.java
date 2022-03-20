package co.tala.bankaccountmanagement.utilities;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utilities {
    public static String generateTransactionId()
    {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

        public static LocalDateTime dateToLocalDateTime(Date date) {
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }

        public static Date localDateTimeToDate(LocalDateTime localDateTime) {
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }

    public static Map<String, Date> getStartAndEndOfDay()
    {

        Date date = new Date();
        LocalDateTime localDateTime = Utilities.dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);

        Map<String, Date> dateValues = new HashMap<>();

        dateValues.put("startOfDay", Utilities.localDateTimeToDate(startOfDay));
        dateValues.put("endOfDay", Utilities.localDateTimeToDate(endOfDay));

        return dateValues;
    }
}

package utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class DateUtils {

    // Định dạng chuẩn dùng trong chương trình
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    // Chuyển chuỗi sang Timestamp
    public static Timestamp toTimestamp(String datetimeStr) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(datetimeStr, DateTimeFormatter.ofPattern(DATETIME_FORMAT));
            return Timestamp.valueOf(ldt);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Chuyển Timestamp sang chuỗi định dạng
    public static String formatTimestamp(Timestamp ts) {
        if (ts == null) return "";
        return ts.toLocalDateTime().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
    }

    // Kiểm tra thời gian bắt đầu phải trước thời gian kết thúc
    public static boolean isStartBeforeEnd(Timestamp start, Timestamp end) {
        if (start == null || end == null) return false;
        return start.before(end);
    }

    // Kiểm tra định dạng có hợp lệ không (ví dụ khi nhập từ form)
    public static boolean isValidFormat(String datetimeStr) {
        return toTimestamp(datetimeStr) != null;
    }
}

package com.vupt.SHM.utils;

public class DisplayTextUtils {
    public static String getWarningDeleteMessage(String menu, String data) {
        return String.format("Đồng ý xóa %s [%s]?", new Object[]{menu, data});
    }

    public static String getNotFoundMessage(String menu, String property, Object data) {
        return String.format("Không tìm thấy %s với %s là %s", new Object[]{menu, property, data.toString()});
    }

    public static String getIsExistByMessage(String menu, String property, Object data) {
        return String.format("%s đã tồn tại với %s là %s", new Object[]{menu, property, data});
    }
}
package com.timeout.diy.common.utils;

public class StringUtil {
    public static boolean isEmpty(String string) {
        if(string == null || "".equals(string)) {
            return true;
        }
        return false;
    }


    public static boolean isNotEmpty(String string) {
        if(string != null && !"".equals(string)) {
            return true;
        }
        return false;
    }

}

package com.vulpeszerda.mapoverlayview;

/**
 * Created by vulpes on 2017. 4. 19..
 */

final class CommonUtils {
    static boolean isNullOrEmpty(CharSequence message) {
        return message == null || message.length() == 0;
    }

    static boolean isNullOrEmpty(String message) {
        return message == null || message.length() == 0;
    }

    static boolean areObjectsEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }
}

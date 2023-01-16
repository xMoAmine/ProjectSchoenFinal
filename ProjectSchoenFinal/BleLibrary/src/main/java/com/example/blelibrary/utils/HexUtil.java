package com.example.blelibrary.utils;

public class HexUtil {

    public static String formatDecimalString(byte[] data, boolean addSpace) {
        if (data == null || data.length < 1)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String decimal = String.valueOf(data[i]);
            sb.append(decimal);
            if (addSpace)
                sb.append(" ");
        }
        return sb.toString().trim();
    }

}






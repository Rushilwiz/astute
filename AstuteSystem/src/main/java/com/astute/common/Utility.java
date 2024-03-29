package com.astute.common;

import java.util.HashSet;

/**
 * Created by Gopi on 9/7/2017.
 */
public class Utility {
    public Utility() {
    }

    public static String setToString(HashSet<String> inValue, String separator){
        String values = "";
        for(String value : inValue) {
            values += value + separator;
        }
        values = values.substring(0, values.length()-1);
        return values;
    }

    public static String arrayToString(String[] inValue, String separator){
        String values = "";
        for(String value : inValue) {
            values += value + separator;
        }
        values = values.substring(0, values.length()-1);
        return values;
    }
}

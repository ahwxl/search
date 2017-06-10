package com.bplow.search.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    
    public static String getCurrentDate(){
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return format.format(new Date());
    }

}

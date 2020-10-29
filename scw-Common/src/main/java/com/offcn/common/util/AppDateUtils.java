package com.offcn.common.util;

import com.sun.org.apache.bcel.internal.generic.FieldOrMethod;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDateUtils {

    public static String getFormatTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String string = format.format(new Date());
        return string;
    }

    public static String getFormatTime(String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String string = format.format(new Date());
        return string;
    }

    public static String getFormatTime(String pattern , Date date){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String string = format.format(date);
        return string;
    }

}


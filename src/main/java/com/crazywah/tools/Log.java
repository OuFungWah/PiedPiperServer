package com.crazywah.tools;

import java.text.SimpleDateFormat;

public class Log {

    static boolean isDev = true;

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static void i(String tag,String message){
        logout("INFO",tag,message);
    }

    public static void d(String tag,String message){
        if (isDev) {
            logout("DEV", tag, message);
        }
    }

    public static void e(String tag,String message){
        logout("ERROR",tag,message);
    }

    public static void w(String tag,String message){
        logout("WRONG",tag,message);
    }

    private static void logout(String type,String tag,String message){
        System.out.println(simpleDateFormat.format(System.currentTimeMillis())+"  PiedPiper "+type+" --- "+" [ "+tag+" ] :\t "+message);
    }

}

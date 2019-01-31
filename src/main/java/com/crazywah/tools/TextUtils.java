package com.crazywah.tools;

public class TextUtils {

    public static boolean isEmpty(String text) {
        return text == null || text.equals("");
    }

    public static boolean isEmpty(String ...texts){
        for (String text:texts){
            if(text==null||text.equals("")){
                return true;
            }
        }
        return false;
    }

}

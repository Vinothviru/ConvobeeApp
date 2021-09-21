package com.convobee.utils;

public class ParseConstantsUtil {

    public static String parse(String constant,String variable){
        String aa = constant.replaceFirst("<<VARIABLE>>",variable);
        return  aa;
    }
}


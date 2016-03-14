package com.huangyezhaobiao.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by shenzhixin on 2015/12/30.
 */
public class CharUtils {
    public static String encodeUTF(String msg) {
        try {
            return URLEncoder.encode(msg,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String decodeUTF(String msg) {
        try {
           return URLDecoder.decode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}

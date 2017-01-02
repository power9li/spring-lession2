package com.power.spring.utils;

/**
 * Created by ShenLi on 2014/12/19.
 * 16进制和字符串互转
 */
public class HexUtils {

    public static byte[] hex2String(String hex) {
        byte[] bytes = new byte[hex.length()/2];
        for(int i =0; i<hex.length(); i = i+2) {
            String subStr = hex.substring(i, i+2);
            boolean negative = false; //是否为正数
            int inte = Integer.parseInt(subStr,16);
            if(inte > 127) negative = false;
            if(inte == 128){
                inte = -128;
            }else if(negative){
                inte = 0 - (inte & 0x7F);
            }
            byte b = (byte)inte;
            bytes[i/2] = b;
        }
        return bytes;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for(int i=0; i< bytes.length; i++) {
            byte b = bytes[i];
            int inte = (b < 0)? Math.abs(b)|0x80 /*负数会转成正数*/: Math.abs(b);
            String temp = Integer.toHexString(inte);
            if(temp.length() == 1) {
                hex.append("0");
            }
            hex.append(temp.toLowerCase());
        }
        return hex.toString();
    }

    public static void main(String[] args) throws Exception{
        String source = "I am source string.";
        String hstr = byte2hex(source.getBytes("utf8"));
        byte[] bytes = hex2String(hstr);
        String resource = new String(bytes);
        System.out.printf("source = %s \n hstr = %s \n resource = %s", source, hstr, resource);

    }

}

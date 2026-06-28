package com.dalg.geni.utils;
import java.util.*;
public class PersianText {
    private static final Random r=new Random();
    public static boolean has(String text, String... keys){ if(text==null)return false; for(String k:keys) if(text.contains(k)) return true; return false; }
    public static String cleanName(String s){
        if(s==null)return "";
        return s.replace("جنی","").replace("زنگ بزن","").replace("تماس بگیر","").replace("به","").replace("با","").replace("روشن کن","").replace("خاموش کن","").replace("پیام بده","").trim();
    }
    public static String callReply(String name){
        String[] a={"باشه، الان دارم به "+name+" زنگ می‌زنم.","حتماً، تماس با "+name+" رو شروع می‌کنم.","الان به "+name+" زنگ می‌زنم.","چشم، دارم شماره "+name+" رو می‌گیرم."};
        return a[r.nextInt(a.length)];
    }
    public static String speakerOn(){ String[] a={"باشه، بلندگو روشن شد.","حتماً، گذاشتم روی بلندگو.","بلندگو رو فعال کردم."}; return a[r.nextInt(a.length)]; }
    public static String speakerOff(){ String[] a={"باشه، از بلندگو برداشتم.","بلندگو خاموش شد.","تماس از حالت بلندگو خارج شد."}; return a[r.nextInt(a.length)]; }
}

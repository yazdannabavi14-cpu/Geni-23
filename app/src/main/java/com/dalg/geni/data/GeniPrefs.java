package com.dalg.geni.data;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;

public class GeniPrefs {
    private final SharedPreferences p;
    public GeniPrefs(Context c){ p=c.getSharedPreferences("geni", Context.MODE_PRIVATE); }
    public String wakeWord(){ return p.getString("wake","جنی"); }
    public void wakeWord(String v){ p.edit().putString("wake",v).apply(); }
    public int callSim(){ return p.getInt("call_sim", -1); }
    public void callSim(int id){ p.edit().putInt("call_sim", id).apply(); }
    public int smsSim(){ return p.getInt("sms_sim", -1); }
    public void smsSim(int id){ p.edit().putInt("sms_sim", id).apply(); }
    public void alias(String name, String phone){ p.edit().putString("alias_"+norm(name), phone).apply(); }
    public String aliasPhone(String name){ return p.getString("alias_"+norm(name), null); }
    public Map<String,String> aliases(){
        Map<String,String> out=new LinkedHashMap<>();
        for(Map.Entry<String,?> e:p.getAll().entrySet()) if(e.getKey().startsWith("alias_")) out.put(e.getKey().substring(6), String.valueOf(e.getValue()));
        return out;
    }
    public static String norm(String s){ return s==null?"":s.trim().toLowerCase(Locale.ROOT).replace("ي","ی").replace("ك","ک"); }
}

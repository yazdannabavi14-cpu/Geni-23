package com.dalg.geni.voice;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import androidx.core.content.ContextCompat;
import com.dalg.geni.data.GeniPrefs;
import com.dalg.geni.utils.PersianText;

public class GeniEngine {
    public interface Speaker { void say(String text); }
    private final Context c; private final GeniPrefs prefs; private final Speaker sp;
    public GeniEngine(Context c, Speaker sp){ this.c=c; this.sp=sp; this.prefs=new GeniPrefs(c); }

    public void handle(String raw){
        if(raw==null) return; String t=raw.trim().toLowerCase().replace("ي","ی").replace("ك","ک");
        if(!t.contains(prefs.wakeWord().toLowerCase()) && !t.startsWith("زنگ") && !t.contains("بلندگو")) return;
        if(PersianText.has(t,"بلندگو") && PersianText.has(t,"بذار","بزار","روشن","فعال")){ setSpeaker(true); sp.say(PersianText.speakerOn()); return; }
        if(PersianText.has(t,"بلندگو") && PersianText.has(t,"بردار","خاموش","غیر فعال","غیرفعال")){ setSpeaker(false); sp.say(PersianText.speakerOff()); return; }
        if(PersianText.has(t,"زنگ بزن","تماس بگیر","شماره بگیر")){
            String name=PersianText.cleanName(t); String phone=findPhone(name);
            if(phone==null){ sp.say("شماره "+name+" رو پیدا نکردم. می‌تونی از داخل برنامه اضافه‌اش کنی."); return; }
            sp.say(PersianText.callReply(name)); call(phone); return;
        }
        if(PersianText.has(t,"پیام بده","اس ام اس بده","sms بده")){
            sp.say("پیامک آماده است. برای تست امن، فعلاً متن پیام را از داخل برنامه وارد کن."); return;
        }
        sp.say("شنیدم. فعلاً فرمان تماس و بلندگو فعال است.");
    }
    private String findPhone(String name){
        String ph=prefs.aliasPhone(name); if(ph!=null) return ph;
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED) return null;
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] proj={ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        try(Cursor cur=c.getContentResolver().query(uri, proj, null, null, null)){
            if(cur!=null) while(cur.moveToNext()){
                String dn=cur.getString(0); String num=cur.getString(1);
                if(dn!=null && GeniPrefs.norm(dn).contains(GeniPrefs.norm(name))) return num;
            }
        } catch(Exception ignored){}
        return null;
    }
    private void call(String phone){
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){ sp.say("اجازه تماس داده نشده است."); return; }
        Intent i=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+Uri.encode(phone))); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int sim=prefs.callSim(); if(sim!=-1){ i.putExtra("com.android.phone.extra.slot", sim); i.putExtra("slot", sim); i.putExtra("simSlot", sim); i.putExtra("subscription", sim); }
        try{ c.startActivity(i); }catch(Exception e){ sp.say("تماس شروع نشد. تنظیمات اجازه تماس را بررسی کن."); }
    }
    public void sendSms(String phone, String msg){
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){ sp.say("اجازه ارسال پیامک داده نشده است."); return; }
        try{
            int sub=prefs.smsSim(); SmsManager sm = sub!=-1 ? SmsManager.getSmsManagerForSubscriptionId(sub) : SmsManager.getDefault();
            sm.sendTextMessage(phone,null,msg,null,null); sp.say("پیامک ارسال شد.");
        }catch(Exception e){ sp.say("ارسال پیامک انجام نشد."); }
    }
    private void setSpeaker(boolean on){ AudioManager am=(AudioManager)c.getSystemService(Context.AUDIO_SERVICE); if(am!=null) am.setSpeakerphoneOn(on); }
}

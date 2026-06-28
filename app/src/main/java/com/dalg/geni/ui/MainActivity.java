package com.dalg.geni.ui;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.*;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.*;
import android.widget.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.dalg.geni.data.GeniPrefs;
import com.dalg.geni.services.GeniVoiceService;
import java.util.*;

public class MainActivity extends Activity {
    private GeniPrefs prefs; private LinearLayout root; private TextView status;
    private final String[] perms={Manifest.permission.RECORD_AUDIO,Manifest.permission.CALL_PHONE,Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.POST_NOTIFICATIONS};
    public void onCreate(Bundle b){ super.onCreate(b); prefs=new GeniPrefs(this); buildUi(); requestPerms(); }
    private void buildUi(){
        ScrollView sv=new ScrollView(this); root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(36,36,36,36); root.setBackgroundColor(Color.rgb(9,11,18)); sv.addView(root); setContentView(sv);
        title("جنی",30); label("دستیار صوتی فارسی نزدیک به Siri — نسخه تست تماس و بلندگو",14);
        button("فعال‌سازی جنی در پس‌زمینه", v->startSvc());
        button("دادن اجازه اجرای دائمی / باتری", v->openBatterySettings());
        status=label("وضعیت: آماده تنظیم",15);
        section("کلیدواژه بیدارباش"); edit("کلیدواژه", prefs.wakeWord(), s->{ prefs.wakeWord(s); toast("ثبت شد"); });
        section("سیم‌کارت تماس و پیامک"); simButtons();
        section("افزودن مخاطب دستی / یادگیری اسم"); aliasBox();
        section("فرمان‌های تست"); label("بگو: «جنی به علی زنگ بزن» / «جنی بزار رو بلندگو» / «جنی از بلندگو بردار»",14);
    }
    private TextView title(String t,int sp){ TextView v=label(t,sp); v.setTextColor(Color.WHITE); v.setGravity(Gravity.CENTER); return v; }
    private TextView label(String t,int sp){ TextView v=new TextView(this); v.setText(t); v.setTextSize(sp); v.setTextColor(Color.rgb(210,220,235)); v.setPadding(0,10,0,10); root.addView(v); return v; }
    private void section(String t){ TextView v=label("\n"+t,18); v.setTextColor(Color.rgb(53,215,255)); }
    private void button(String t, View.OnClickListener l){ Button b=new Button(this); b.setText(t); b.setAllCaps(false); b.setOnClickListener(l); root.addView(b,new LinearLayout.LayoutParams(-1,-2)); }
    private void edit(String hint,String val, Saver saver){ EditText e=new EditText(this); e.setHint(hint); e.setText(val); e.setSingleLine(true); e.setTextColor(Color.WHITE); e.setHintTextColor(Color.GRAY); root.addView(e); button("ذخیره " + hint, v->saver.save(e.getText().toString())); }
    interface Saver{ void save(String s); }
    private void aliasBox(){
        EditText name=new EditText(this), phone=new EditText(this); name.setHint("نام مثل علی"); phone.setHint("شماره مثل 0912..."); for(EditText e:new EditText[]{name,phone}){ e.setTextColor(Color.WHITE); e.setHintTextColor(Color.GRAY); root.addView(e); }
        button("ذخیره مخاطب در حافظه جنی", v->{ prefs.alias(name.getText().toString(), phone.getText().toString()); toast("مخاطب ذخیره شد"); });
    }
    private void simButtons(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){ label("بعد از دادن اجازه، برنامه را یک بار ببند و باز کن تا سیم‌کارت‌ها دیده شوند.",13); return; }
        try{
            SubscriptionManager sm=(SubscriptionManager)getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE); List<SubscriptionInfo> list=sm.getActiveSubscriptionInfoList();
            if(list==null || list.isEmpty()){ label("سیم‌کارت فعال پیدا نشد؛ از پیش‌فرض گوشی استفاده می‌شود.",13); return; }
            for(SubscriptionInfo si:list){ int id=si.getSubscriptionId(); String name=String.valueOf(si.getDisplayName()); button("انتخاب برای تماس: "+name, v->{prefs.callSim(id); toast("سیم تماس ثبت شد");}); button("انتخاب برای پیامک: "+name, v->{prefs.smsSim(id); toast("سیم پیامک ثبت شد");}); }
        }catch(Exception e){ label("خواندن سیم‌کارت روی این گوشی محدود شد؛ از پیش‌فرض گوشی استفاده می‌شود.",13); }
    }
    private void requestPerms(){ if(Build.VERSION.SDK_INT>=23) ActivityCompat.requestPermissions(this, perms, 7); }
    private void startSvc(){ Intent i=new Intent(this, GeniVoiceService.class); if(Build.VERSION.SDK_INT>=26) startForegroundService(i); else startService(i); status.setText("وضعیت: جنی در پس‌زمینه فعال شد"); }
    private void openBatterySettings(){ try{ startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)); }catch(Exception e){ startActivity(new Intent(Settings.ACTION_SETTINGS)); } }
    private void toast(String s){ Toast.makeText(this,s,Toast.LENGTH_SHORT).show(); }
}

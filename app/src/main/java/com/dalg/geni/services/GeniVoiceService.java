package com.dalg.geni.services;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.speech.*;
import android.speech.tts.TextToSpeech;
import androidx.core.app.*;
import androidx.core.content.ContextCompat;
import com.dalg.geni.ui.MainActivity;
import com.dalg.geni.voice.GeniEngine;
import java.util.*;

public class GeniVoiceService extends Service implements RecognitionListener, TextToSpeech.OnInitListener {
    private static final String CH="geni_low_noise"; private SpeechRecognizer sr; private TextToSpeech tts; private GeniEngine engine; private Handler h;
    public void onCreate(){ super.onCreate(); h=new Handler(Looper.getMainLooper()); createChannel(); tts=new TextToSpeech(this,this); engine=new GeniEngine(this, this::say); startForeground(11, notif()); startListening(); }
    public int onStartCommand(Intent i,int f,int id){ startListening(); return START_STICKY; }
    public IBinder onBind(Intent i){ return null; }
    private Notification notif(){
        Intent pi=new Intent(this, MainActivity.class); PendingIntent p=PendingIntent.getActivity(this,0,pi,PendingIntent.FLAG_IMMUTABLE);
        return new NotificationCompat.Builder(this,CH).setContentTitle("جنی آماده است").setContentText("در حال گوش دادن کم‌مزاحمت").setSmallIcon(android.R.drawable.ic_btn_speak_now).setOngoing(true).setPriority(NotificationCompat.PRIORITY_MIN).setSilent(true).setContentIntent(p).build();
    }
    private void createChannel(){ if(Build.VERSION.SDK_INT>=26){ NotificationChannel ch=new NotificationChannel(CH,"Geni background",NotificationManager.IMPORTANCE_MIN); ch.setSound(null,null); ch.enableVibration(false); ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(ch);} }
    private void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED) return;
        if(sr==null){ sr=SpeechRecognizer.createSpeechRecognizer(this); sr.setRecognitionListener(this); }
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"fa-IR"); i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true); i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3);
        try{ sr.startListening(i); }catch(Exception e){ restartSoon(); }
    }
    private void restartSoon(){ h.removeCallbacksAndMessages(null); h.postDelayed(this::startListening, 1200); }
    public void say(String s){ try{ tts.speak(s, TextToSpeech.QUEUE_FLUSH,null,"geni"); }catch(Exception ignored){} }
    public void onInit(int status){ if(status==TextToSpeech.SUCCESS){ tts.setLanguage(new Locale("fa","IR")); tts.setSpeechRate(0.95f); } }
    public void onResults(Bundle b){ ArrayList<String> r=b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); if(r!=null && !r.isEmpty()) engine.handle(r.get(0)); restartSoon(); }
    public void onPartialResults(Bundle b){ ArrayList<String> r=b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); if(r!=null && !r.isEmpty()) engine.handle(r.get(0)); }
    public void onError(int e){ restartSoon(); } public void onReadyForSpeech(Bundle b){} public void onBeginningOfSpeech(){} public void onRmsChanged(float f){} public void onBufferReceived(byte[] b){} public void onEndOfSpeech(){ restartSoon(); } public void onEvent(int i,Bundle b){}
    public void onDestroy(){ if(sr!=null){sr.destroy();} if(tts!=null){tts.shutdown();} super.onDestroy(); }
}

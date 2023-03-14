package com.example.sesliasistan;

import static com.example.sesliasistan.ReminderBroadcast.messageExtra;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class IlacEkle extends AppCompatActivity {
    SQLiteDatabase db=null;
    TextView txtTarih;
    TextView txtSaat;
    Button btn;
    EditText ilacAdi;
    EditText miktari;
    EditText birimi;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilac_ekle);
        setTitle("İlaç Ekle");
        Log.d("myTag","setting title");

        Calendar calendarAlarm=Calendar.getInstance();

        final Calendar calendar=Calendar.getInstance();

        int yil=calendar.get(Calendar.YEAR);
        int ay=calendar.get(Calendar.MONTH);
        int gun=calendar.get(Calendar.DAY_OF_MONTH);
        int saat=calendar.get(Calendar.HOUR_OF_DAY);
        int dk=calendar.get(Calendar.MINUTE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bildirimKanaliOlustur();
        }

        ilacAdi=findViewById(R.id.editTextIlacAdi);
        miktari=findViewById(R.id.editTextMiktar);
        birimi=findViewById(R.id.editTextBirim);

        txtTarih=findViewById(R.id.txtTarih2);
        txtSaat=findViewById(R.id.txtSaat);
        String zaman=saat+":"+dk;
        txtSaat.setText(zaman);
        btn=findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adi=ilacAdi.getText().toString();
                String miktar=miktari.getText().toString();
                String birim=birimi.getText().toString();
                db=openOrCreateDatabase("dtAsistan",MODE_PRIVATE,null);
                String kaydet="insert into ilaclar(ilac_adi ,miktar ,birim ,tarih ,saat )values('"+adi+"','"+miktar+"','"+birim+"','"+txtTarih.getText().toString()+"','"+txtSaat.getText().toString()+"') ";
                db.execSQL(kaydet);
                db.close();

                Intent intent=new Intent(getApplicationContext(),ReminderBroadcast.class);
                intent.putExtra(messageExtra, adi + " adlı ilacınızı " + miktar +" " + birim + " kadar alma vaktiniz gelmiştir");

                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),ReminderBroadcast.notificationId,intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                long kayitZamani=System.currentTimeMillis();

                long onsaniye=10*1000;
                String[] tr=txtTarih.getText().toString().split("\\.");
                int y=Integer.valueOf(tr[2]);
                int a=Integer.valueOf(tr[1]);
                int g=Integer.valueOf(tr[0]);
                String[] zm=txtSaat.getText().toString().split(":");
                int ss=Integer.valueOf(zm[0]);
                int dd=Integer.valueOf(zm[1]);

                calendarAlarm.set(Calendar.YEAR,y);
                calendarAlarm.set(Calendar.MONTH,a - 1); // Calendar month starts from zero !!!
                calendarAlarm.set(Calendar.DAY_OF_MONTH,g);
                calendarAlarm.set(Calendar.HOUR_OF_DAY,ss);
                calendarAlarm.set(Calendar.MINUTE,dd);
                calendarAlarm.set(Calendar.SECOND,0);
                long toplam=calendarAlarm.getTimeInMillis();
                String tt=String.valueOf(g)+"."+String.valueOf(a)+"."+String.valueOf(y)+"Saat "+String.valueOf(ss)+":"+String.valueOf(dd);
                Toast.makeText(IlacEkle.this,tt,Toast.LENGTH_LONG).show();

                Log.d("myTag","millis : " + calendarAlarm.getTimeInMillis());
                //alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendarAlarm.getTimeInMillis(),pendingIntent);
                long delayedTime = SystemClock.elapsedRealtime() + 10 * 1000;
                Log.d("myTag","elapsed realtime " + SystemClock.elapsedRealtime());
                Log.d("myTag","delay is " + delayedTime);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarAlarm.getTimeInMillis() ,pendingIntent);


                Intent ekle=new Intent(IlacEkle.this,Ilaclar.class);
                startActivity(ekle);
                finish();
            };
        });


        int ay2=ay+1;
        String tarih=gun+"."+ay2+"."+yil;
        txtTarih.setText(tarih);

        txtTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        IlacEkle.this,onDateSetListener,yil,ay,gun);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();


            }
        });
        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yil, int ay, int gun) {
                ay=ay+1;
                String tarih=gun+"."+ay+"."+yil;
                txtTarih.setText(tarih);

            }
        };


        txtSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(IlacEkle.this,onTimeSetListener,saat,dk,true);

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();


            }
        });
        onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int saat, int dk) {
                String zaman=saat+":"+dk;
                txtSaat.setText(zaman);
            }


        };

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void bildirimKanaliOlustur(){
       CharSequence name="sesliAsistan";
       String aciklama="Sesli Asistan hatirlatma";
       int importance= NotificationManager.IMPORTANCE_DEFAULT;
       NotificationChannel channel=new NotificationChannel("bildiriSesliAsistan",name,importance);
       channel.setDescription(aciklama);

       NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       notificationManager.createNotificationChannel(channel);
    }
    @Override
    public void onBackPressed()
    {

        Intent geri=new Intent(IlacEkle.this,Ilaclar.class);


        //geri.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(geri);
        finish();
    }
}
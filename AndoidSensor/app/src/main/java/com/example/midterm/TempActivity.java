package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEventListener;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TempActivity extends AppCompatActivity implements SensorEventListener {

    private static final String CHANNEL_ID="channel_id";
    private static final String CHANNEL_NAME="mKurt";
    private static final String CHANNEL_DESC="ŞARJINIZ %10 ALTINDA!";

    TextView tvTemperature;
    ListView lvTemp,lvSeciliTemp;
    Button btnListele;
    EditText etInput;
    final tempDB tempDB = new tempDB(TempActivity.this);
    //counter benim tarih tutucum saniye bazında
    int counter=0; //
    //tempDatas veritabanından çekeceğim sicaklik verileri
    List<String> tempDatas;
    List<String> choosenDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);



        tvTemperature=findViewById(R.id.tvTemperature);
        lvTemp=findViewById(R.id.lvTemp);
        etInput=findViewById(R.id.etInput);
        btnListele=findViewById(R.id.btnListele2);
        lvSeciliTemp=findViewById(R.id.lvSeciliTemp);


        Thread thread = new Thread()
        {
            @Override
            public void run(){
                while(!isInterrupted())
                {
                    try {
                        // 10 saniyelik threading
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
                                {

                                    NotificationChannel channel= new NotificationChannel(CHANNEL_ID,
                                            CHANNEL_NAME,
                                            NotificationManager.IMPORTANCE_DEFAULT);
                                    channel.setDescription(CHANNEL_DESC);
                                    NotificationManager manager = getSystemService(NotificationManager.class);
                                    manager.createNotificationChannel(channel);
                                }
                                Intent intent = TempActivity.this.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                                final float  temp   = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)) / 10;
                                if(temp<-19 )
                                {
                                    bildirimVer1();
                                }
                                else if(temp>49)
                                {
                                    bildirimVer2();
                                }

                                else
                                {

                                }

                                // counter her 10 saniyede bir artıyor
                                counter+=10;
                                tvTemperature.setText("Telefon sicakligi: "+temp+"dir");
                                //10 saniyede 1, bir counter (tarih) değeri bir de batarya değeri
                                //veritabanına işleniyor.
                                tempDB.dataInput(counter,String.valueOf(temp));
                                // şimdi listView'a 10 saniyede bir update yapacaz.
                                tempDatas=tempDB.veriListele();
                                //şimdi verileri aldık ama bunu ArrayAdapter ile listview'a aktarıcaz.
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TempActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        android.R.id.text1,tempDatas);
                                // şimdi listview'a adapteri set edip doldurtucaz.
                                lvTemp.setAdapter(adapter);
                                btnListele.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(counter<Integer.parseInt(etInput.getText().toString()))
                                        {
                                            Toast.makeText(TempActivity.this,
                                                    "Henüz yeterli zaman geçmemiş. Lütfen doğru bir zaman girin.",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {

                                            int liste_boyutu=tempDatas.size();
                                            int gelenDeger=Integer.parseInt(etInput.getText().toString());
                                            choosenDatas=tempDatas.subList(liste_boyutu-gelenDeger,liste_boyutu);
                                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TempActivity.this,
                                                    android.R.layout.simple_list_item_1,
                                                    android.R.id.text1,choosenDatas);
                                            lvSeciliTemp.setAdapter(adapter2);

                                        }
                                    }
                                });



                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        };
        // burada da her 10 saniyede bir textView'a telefonun batarya değeri gösterilecek
        //ve veritabanına her 10 saniyede bir veri eklenecek.
        thread.start();






    }
    private void bildirimVer1(){

        String title="Isı problemi";
        String body="Telefonunuzun ısısı çok düşük";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



        Intent intent= new Intent(TempActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("bildirim",body);
        PendingIntent pendingIntent =  PendingIntent.getActivity(TempActivity.this,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());






    }

    private void bildirimVer2(){

        String title="Isı problemi";
        String body="Telefonunuzun ısısı çok yüksek!";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



        Intent intent= new Intent(TempActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("bildirim",body);
        PendingIntent pendingIntent =  PendingIntent.getActivity(TempActivity.this,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());






    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

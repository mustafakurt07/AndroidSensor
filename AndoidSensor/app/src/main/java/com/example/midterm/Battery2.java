package com.example.midterm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import java.util.List;



public class Battery2 extends AppCompatActivity {
    TextView tvBattery,tvCount;
    int PHONE_BATTERY;
    EditText etListele2;
    Button btnListele2,btnCiz;



    // bu counter benim için tarihi tutuyo, counter içerisinde bir değer girilince, o veriyi çekebilicem.
    // bir  nevi tag gibi düşün

    ListView lvBattery2,lvSecili2;
    List<String> batteryDatas;
    List<String>choosenDatas;
    int counter=0;
    int bataryaDegeri=0;
    private static final String CHANNEL_ID="channel_id";
    private static final String CHANNEL_NAME="mKurt";
    private static final String CHANNEL_DESC="ŞARJINIZ %10 ALTINDA!";

     batteryDB batteryDB = new batteryDB(Battery2.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery2);


        lvBattery2=findViewById(R.id.lvBattery2);
        tvBattery=findViewById(R.id.tvBattery2);
        etListele2=findViewById(R.id.etListele2);
        lvSecili2=findViewById(R.id.lvSecili2);
        btnListele2=findViewById(R.id.btnListele2);




            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = this.registerReceiver(null, ifilter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 0);

            final int batteryPct = (level * 100 / scale); // bu doğru sonuç veriyo


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

                                    bataryaDegeri=batteryPct;
                                    if(bataryaDegeri<10)
                                    {
                                        bildirimVer();
                                    }
                                    else
                                    {

                                    }
                                    // counter her 10 saniyede bir artıyor
                                    counter+=10;
                                    tvBattery.setText("Batarya % "+String.valueOf(batteryPct)+"dir ->" + counter);

                                    //10 saniyede 1, bir counter (tarih) değeri bir de batarya değeri
                                    //veritabanına işleniyor.

                                    batteryDB.dataInput(counter,String.valueOf(batteryPct));
                                    // şimdi listView'a 10 saniyede bir update yapacaz.
                                    batteryDatas=batteryDB.veriListele();
                                    //şimdi verileri aldık ama bunu ArrayAdapter ile listview'a aktarıcaz.
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Battery2.this,
                                            android.R.layout.simple_list_item_1,
                                            android.R.id.text1,batteryDatas);
                                    // şimdi listview'a adapteri set edip doldurtucaz.
                                    lvBattery2.setAdapter(adapter);
                                    btnListele2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(counter<Integer.parseInt(etListele2.getText().toString()))
                                            {
                                                Toast.makeText(Battery2.this,
                                                        "Henüz yeterli zaman geçmemiş. Lütfen doğru bir zaman girin.",
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                //şimdi listenin size'ı alınacak
                                                int liste_boyutu=batteryDatas.size();
                                                int gelenDeger=Integer.parseInt(etListele2.getText().toString());
                                                choosenDatas=batteryDatas.subList(liste_boyutu-gelenDeger,liste_boyutu);
                                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Battery2.this,
                                                        android.R.layout.simple_list_item_1,
                                                        android.R.id.text1,choosenDatas);
                                                lvSecili2.setAdapter(adapter2);






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


        private void bildirimVer(){

        String title="Batarya Düşük!";
        String body="Şarjınız %10'un altında, lütfen cihazı şarj edin.";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



            Intent intent= new Intent(Battery2.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("bildirim",body);
            PendingIntent pendingIntent =  PendingIntent.getActivity(Battery2.this,
                    0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,mBuilder.build());






        }








}


package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

public class MainActivity extends AppCompatActivity {

    Button btnSarj,btnTemp,btnCiziktir,btnSarjCiziktir;
    TextView tvInfo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSarj=findViewById(R.id.btnSarj);
        btnTemp=findViewById(R.id.btnTemp);
        btnCiziktir=findViewById(R.id.btnCiziktir);
        tvInfo2=findViewById(R.id.tvInfo2);
        btnSarjCiziktir=findViewById(R.id.btnBataryaGrafik);
        TastyToast.makeText(getApplicationContext(), "WELCOME TO MİDTERMMMMM !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


        btnCiziktir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Iste Grafiginiz !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                Intent i = new Intent(MainActivity.this, GraphicsTemp.class);
                startActivity(i);
            }
        });

        btnSarj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "efendimiz sarjını istiyor !", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                Intent i = new Intent(MainActivity.this, Battery2.class);
                startActivity(i);
            }
        });

        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Telefon sıcaklıgı onemli!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                Intent i = new Intent(getApplicationContext(), TempActivity.class);
                startActivity(i);
            }
        });

        btnSarjCiziktir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Iste Grafiginiz !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                Intent i = new Intent(getApplicationContext(), Graphics.class);
                startActivity(i);
            }
        });

        String mesaj = getIntent().getStringExtra("bildirim");
        tvInfo2.setText(mesaj);






}

    }


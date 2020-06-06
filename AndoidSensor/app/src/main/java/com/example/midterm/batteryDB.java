package com.example.midterm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class batteryDB extends SQLiteOpenHelper {

    //veritabanı adım = batteryDB
    private static final  String DATABASE_NAME="batteryDB";
    //veritabanı sürümüm
    private static final int dbVersion=1;
    //veritabanındaki tarih değerlerim
    private static final String ROW_ID="id";

    private static final String TABLE= "bataryaBilgileri";

    //veritabanı tarih sütunu
    private static final String ROW_NAME = "tarih";
    //veritabanı  değer sütunu
    private static final String ROW_VALUE="deger";





    public batteryDB(@Nullable Context context) {

        super(context,DATABASE_NAME,null,dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //tabloyu oluşturdum
        db.execSQL("CREATE TABLE " + TABLE + " ("+ROW_ID+" INTEGER PRIMARY KEY,"+ROW_NAME+"" +
                " TEXT NOT NULL,"+ROW_VALUE+" TEXT NOT NULL) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //aynı isimde tablo varsa sil
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        //aşağıda bunu yolla
        onCreate(db);

    }

    //burada veri ekliyoruz
    void dataInput(int tarih, String deger)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ROW_NAME, tarih);
        cv.put(ROW_VALUE,deger);
        db.insert(TABLE,null,cv);
        db.close();
    }

    public  List<String> veriListele()
    {
        List<String> veriler = new ArrayList<String>();
        SQLiteDatabase db = batteryDB.this.getWritableDatabase();
        // cursor ile verileri listelicez while döngüsünde çağırcaz
        // cursor parametre olarak normal bi dizi istiyo, o yüzden String dizisi kullanıcam
        String [] sutunlar={ROW_ID,ROW_NAME,ROW_VALUE};
        Cursor cursor = db.query(TABLE, sutunlar,null,null,null,null,null);
        //cursor.movetonext yani sıradaki veriye git.
        while(cursor.moveToNext())
        {
            // şimdi 3 tane sutun var bunları tek sutuna çevirmemiz gerekiyo
            // 0 dediğimiz buradaki sıraya göre gidiyo, id 0, 2. ne name = 1
            // 3. ne value = 2
        veriler.add(cursor.getString(2));
        }
        return veriler;

    }

}

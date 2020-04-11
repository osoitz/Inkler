package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBlocal db = new DBlocal(getApplicationContext());
        boolean llena = db.checkEmpty();
        if (llena){
            Log.d("DBCHECK", "LA BASE DE DATOS TIENE INFORMACION");
        } else {
            Log.d("DBCHECK", "LA BASE DE DATOS ESTA VACIA, RELLENANDO...");
            db.rellenarDB();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(MainActivity.this, ActivityListaTatuadores.class);
                startActivity(intent);
            }
        }, 2000);   //2 seconds

    }



}

package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rellenardb = findViewById(R.id.rellenardb);

        rellenardb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO txapu
                DBlocal db = new DBlocal(getApplicationContext());
                db.rellenarDB(getApplicationContext());
                Intent intent = new Intent(MainActivity.this, ActivityListaTatuadores.class);
                startActivity(intent);
            }
        });

    }


    
}

package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rellenardb = findViewById(R.id.rellenardb);

        rellenardb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rellenarDB();
                Intent intent = new Intent(MainActivity.this, RecyclerTatuadores.class);
                startActivity(intent);
            }
        });

    }

    private void rellenarDB () {
        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        dbHelper.delete(db);

        for (int pos = 0;pos < 10;pos++) {

            ContentValues e1 = new ContentValues();
            e1.put(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE, "Estudio " + pos);
            e1.put(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION, "Direccion " + pos);
            e1.put(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL, "Email " + pos);
            e1.put(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO, "Telefono " + pos);
            e1.put(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD, 43 + pos);
            e1.put(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD, -2 + pos);
            db.insert(DBHelper.entidadEstudio.TABLE_NAME, null, e1);
            Log.d("Estudio", "Estudio " + pos + " , Direccion " + pos + " , Email " + pos + " , Telefono " + pos);

            ContentValues t1 = new ContentValues();
            t1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, "Satan " + pos);
            t1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, "Beñat " + pos);
            t1.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, "Smith " + pos);
            t1.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO, pos + 1);
            db.insert(DBHelper.entidadTatuador.TABLE_NAME, null, t1);
            Log.d("Tatuador", "Satan " + pos + " , Beñat " + pos + " , Smith " + pos + " , SBS@gmail.com " + pos + " , 666666666 " + pos);

            ContentValues w1 = new ContentValues();
            w1.put(DBHelper.entidadWeb.COLUMN_NAME_URL, "URL " + pos);
            w1.put(DBHelper.entidadWeb.COLUMN_NAME_ID_ESTUDIO, pos + 1);
            w1.put(DBHelper.entidadWeb.COLUMN_NAME_ID_TATUADOR, pos + 1);
            db.insert(DBHelper.entidadWeb.TABLE_NAME, null, w1);
            Log.d("Web", "URL " + pos);
        }
    }
}

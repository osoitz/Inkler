package com.example.inkler;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerTatuadores extends AppCompatActivity {

    // Variables necesarias
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorTatuadores adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_tatuadores);

        cargartatuadores();

        //Acciones del onclick y onlongclick del recycler
        recyclerView.addOnItemTouchListener(new AlumnoRecyclerViewListener(this, recyclerView, new AlumnoRecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(RecyclerTatuadores.this, FichaTatuadorActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));


    }

    private void cargartatuadores() {

        Tatuador.getTatuadorList().clear();

        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues t1 = new ContentValues();
        t1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO,"Satan");
        t1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE,"Beñat");
        t1.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS,"Smith");
        t1.put(DBHelper.entidadTatuador.COLUMN_NAME_EMAIL,"SBS@gmail.com");
        t1.put(DBHelper.entidadTatuador.COLUMN_NAME_TELEFONO,"666666666");
        t1.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO,"001");
        db.insert(DBHelper.entidadTatuador.TABLE_NAME,null,t1);

        //Columnas
        String[] proyeccion = {DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, DBHelper.entidadTatuador.COLUMN_NAME_EMAIL, DBHelper.entidadTatuador.COLUMN_NAME_TELEFONO, DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO};
        //Respuesta
        Cursor cursor = db.query(DBHelper.entidadTatuador.TABLE_NAME, proyeccion, null, null, null, null, null);
        // recoger los datos
        while (cursor.moveToNext()) {
            String NombreArt = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO));
            String Nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            String Apellidos = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS));
            String Email = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_EMAIL));
            String Telefono = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_TELEFONO));
            String IDEstudio = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            Tatuador t = new Tatuador(NombreArt, Nombre, Apellidos, Email, Telefono, IDEstudio);
            Tatuador.getTatuadorList().add(t);
        }
        cursor.close();

        /*Tatuador t = new Tatuador("PinxaUvas", "Antonio", "Lopez Garcia", "APU@gmail.com", "653951284", "Hola");
        Tatuador.getTatuadorList().add(t);*/

        recyclerView = findViewById(R.id.recyclerFragment);
        adaptador = new AdaptadorTatuadores(getApplicationContext(), Tatuador.getTatuadorList());
        recyclerView.setAdapter(adaptador);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = findViewById(R.id.recyclerFragment);
    }

}

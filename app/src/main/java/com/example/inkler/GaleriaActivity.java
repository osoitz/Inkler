package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class GaleriaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DBHelper dbHelper;
    private SQLiteDatabase dbsqlite;
    private Galeria BDSQLite;
    private String nombre;
    private int tatuaje;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);



        //Iniciar DB
        dbHelper = new DBHelper(getBaseContext());
        dbsqlite = dbHelper.getWritableDatabase();

        String [] proyeccion = {DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO};
        Cursor alumnosSQLite = dbsqlite.query(DBHelper.entidadTatuador.TABLE_NAME,proyeccion,null,null,null,null,null);

        Galeria.getGaleriaList().clear();

        Log.d("tag", "antes de entarr: ");
        while (alumnosSQLite.moveToNext()){
            Log.d("tag", "onCreate: ");

            tatuaje = alumnosSQLite.getInt(alumnosSQLite.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            nombre = alumnosSQLite.getString(alumnosSQLite.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            Log.d("tag", "tatu: " + nombre);
            //guardamos los datos de sqlite en guardarsqlite y los pasamos a la clase Alumno
            BDSQLite = new Galeria(tatuaje, nombre);
            Galeria.getGaleriaList().add(BDSQLite);
        }
        alumnosSQLite.close();

        RecyclerView recyclerView = findViewById(R.id.recyclerGaleria);
        AdaptadorGaleria adaptador = new AdaptadorGaleria(GaleriaActivity.this, Galeria.getGaleriaList());
        recyclerView.setAdapter(adaptador);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(GaleriaActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        //onclick para ver los datos del alumno selccionado en la activity DatosAlumno
        recyclerView.addOnItemTouchListener(new GaleriaRecyclerViewListener(GaleriaActivity.this, recyclerView, new GaleriaRecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {



            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));

    }
}

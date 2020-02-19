package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_AnadirTatuador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__anadir_tatuador);
        final Spinner spinner=findViewById(R.id.SpinnerNombreEstudios);
        SpinnerAdapter adapter;
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, rellenarSpinner(cargarSpinner()));
        spinner.setAdapter(adapter);

        Button btnAnadirTatuador = findViewById(R.id.btnAñadirTatuador);
        btnAnadirTatuador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView et_nombre=findViewById(R.id.contentNombre);
                TextView et_apellidos=findViewById(R.id.contentApellido);
                TextView et_nombreArt=findViewById(R.id.contentNombreArtistico);

                String st_nombre = et_nombre.getText().toString();
                String st_apellidos = et_apellidos.getText().toString();
                String st_nombreArtistico = et_nombreArt.getText().toString();
                String st_Estudio = spinner.getSelectedItem().toString();
                Log.d("tag","Lo que esta en el spinner es: "+ st_Estudio);
            }
        });

    }

    public ArrayList<String> cargarSpinner(){
        ArrayList<String> stringarray = new ArrayList<>();
        stringarray.clear();
        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Columnas
        String[] proyeccion = {DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE};
        //Respuesta
        Cursor cursor = db.query(DBHelper.entidadEstudio.TABLE_NAME,proyeccion,null,null,null,null,null);
        while (cursor.moveToNext()){
            String nombreEstudio = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE));
            stringarray.add(nombreEstudio);
        }
    return stringarray;

    }
    public String[] rellenarSpinner (ArrayList<String> stringarray){
        String[] nombres = new String[stringarray.size()];

        for(int i=0; i<stringarray.size();i++){
            nombres[i]=stringarray.get(i);
        }
        return  nombres;
    }
    public int RecogerId (String nombreEstudio){
        int idEstudio = 1;

        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Columnas
        String[] proyeccion = {DBHelper.entidadEstudio._ID};

        //Respuesta
        Cursor cursor = db.query(
                DBHelper.entidadEstudio.TABLE_NAME,
                proyeccion,
                DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()){

        }



        return idEstudio;
    }
}

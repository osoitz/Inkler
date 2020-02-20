package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_AnadirTatuador extends AppCompatActivity {

    private EditText et_nombre;
    private EditText et_apellidos;
    private EditText et_nombreArt;
    private boolean anadir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__anadir_tatuador);
        et_nombre = findViewById(R.id.contentNombre);
        et_apellidos = findViewById(R.id.contentApellido);
        et_nombreArt = findViewById(R.id.contentNombreArtistico);
        anadir = getIntent().getBooleanExtra("añadir",false);
        final Spinner spinner=findViewById(R.id.SpinnerNombreEstudios);
        SpinnerAdapter adapter;
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, rellenarSpinner(cargarSpinner()));
        spinner.setAdapter(adapter);
        Button btnAnadirTatuador = findViewById(R.id.btnAñadirTatuador);

        if (anadir) {
            btnAnadirTatuador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String st_nombre = et_nombre.getText().toString();
                    String st_apellidos = et_apellidos.getText().toString();
                    String st_nombreArtistico = et_nombreArt.getText().toString();
                    String st_Estudio = spinner.getSelectedItem().toString();
                    Log.d("tag", "Lo que esta en el spinner es: " + st_Estudio);
                    if (st_nombre.equals("") || st_apellidos.equals("") || st_nombreArtistico.equals("") || st_Estudio.equals("")) {
                        Toast.makeText(getApplicationContext(), "You may fill every empty land to insert something", Toast.LENGTH_SHORT).show();
                    } else {
                        int IdEstudio = RecogerId(st_Estudio);
                        // Iniciar base de datos
                        DBHelper dbHelper = new DBHelper(getBaseContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues e1 = new ContentValues();
                        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, st_nombre);
                        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, st_apellidos);
                        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, st_nombreArtistico);
                        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO, IdEstudio);
                        db.insert(DBHelper.entidadTatuador.TABLE_NAME, null, e1);
                        Toast.makeText(getApplicationContext(), "El tatuador " + st_nombre + " ha sido creado", Toast.LENGTH_SHORT).show();
                        et_apellidos.setText("");
                        et_nombreArt.setText("");
                        et_nombre.setText("");
                        Intent intent = new Intent(Activity_AnadirTatuador.this, RecyclerTatuadores.class);
                        startActivity(intent);
                    }
                }
            });
        }else{
            Log.d("tag","Entra a modificar");
            String idTat =DatosApp.getIdTat();
            Tatuador tatuador = recogerTatuador(idTat);
            et_nombre.setText(tatuador.getNombreArt());
            et_apellidos.setText(tatuador.getNombre());
            et_nombreArt.setText(tatuador.getApellidos());
        }

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
        cursor.close();
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
        int idEstudio=0;

        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Columnas
        String[] proyeccion = {DBHelper.entidadEstudio._ID};
        String selection = DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE + " = ?";
        String[] selectionArgs = new String[] { "" + nombreEstudio } ;
        //Respuesta
        Cursor cursor = db.query(
                DBHelper.entidadEstudio.TABLE_NAME,
                proyeccion,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();

            idEstudio= cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio._ID));
        }
        cursor.close();



        return idEstudio;
    }
    public Tatuador recogerTatuador(String IdTatuador){
        Tatuador tatuador = new Tatuador();
        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Columnas
        String[] projection = {
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE,
                DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO,
                DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO};
        String selection = DBHelper.entidadTatuador._ID + " = ?";
        String[] selectionArgs = new String[] { "" + IdTatuador } ;
        //Respuesta
        Cursor cursor = db.query(
                DBHelper.entidadTatuador.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS));
            String nombreArtistico = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO));
            String idEstudio = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            tatuador= new Tatuador(IdTatuador,nombre,apellido,nombreArtistico,idEstudio);
        }
        return tatuador;
    }


}

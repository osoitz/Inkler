package com.example.inkler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_AnadirTatuador extends AppCompatActivity {

    private EditText et_nombre;
    private EditText et_apellidos;
    private EditText et_nombreArt;
    private DBlocal db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBlocal(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__anadir_tatuador);
        et_nombre = findViewById(R.id.contentNombre);
        et_apellidos = findViewById(R.id.contentApellido);
        et_nombreArt = findViewById(R.id.contentNombreArtistico);
        boolean anadir = getIntent().getBooleanExtra("añadir", false);
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

                    if (st_nombre.equals("") || st_apellidos.equals("") || st_nombreArtistico.equals("") || st_Estudio.equals("")) {
                        Toast.makeText(getApplicationContext(), "You may fill every empty land to insert something", Toast.LENGTH_SHORT).show();
                    } else {
                        int IdEstudio = db.RecogerIdEstudio(st_Estudio);
                        db.insertarTatuador(st_nombre,st_apellidos,st_nombreArtistico,IdEstudio);
                        Toast.makeText(getApplicationContext(), "El tatuador " + st_nombre + " ha sido creado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_AnadirTatuador.this, RecyclerTatuadores.class);
                        startActivity(intent);
                    }
                }
            });
        }else{
            btnAnadirTatuador.setText("Modificar Tatuador");
            String idTat =DatosApp.getIdTat();
            Tatuador tatuador = db.recogerTatuador(idTat);
            et_nombre.setText(tatuador.getNombre());
            et_apellidos.setText(tatuador.getApellidos());
            et_nombreArt.setText(tatuador.getNombreArt());
            spinner.setSelection(posicionEstudio(cargarSpinner(),tatuador.getIDEstudio()));
            btnAnadirTatuador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
    public int posicionEstudio(ArrayList<String>stringarray,String nombreEstudio){
        int posicionEstudio=0;

        String EstudioNombre= db.recogerEstudio(nombreEstudio).getNombre();

        for(int i=0;i<stringarray.size();i++){
            if(stringarray.get(i).equals(EstudioNombre)){
                posicionEstudio=i;
            }
        }



        return posicionEstudio;
    }


}

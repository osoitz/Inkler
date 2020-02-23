package com.example.inkler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
//Todo, ¿es posible reducir llamadas a la BD y complejidad ciclomatica?
public class ActivityAnadirTatuador extends AppCompatActivity {

    private EditText et_nombre;
    private EditText et_apellidos;
    private EditText et_nombreArt;
    private DBlocal db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBlocal(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_tatuador);
        et_nombre = findViewById(R.id.contentNombre);
        et_apellidos = findViewById(R.id.contentApellido);
        et_nombreArt = findViewById(R.id.contentNombreArtistico);
        final boolean anadir = getIntent().getBooleanExtra("añadir", false);
        final Spinner spinner=findViewById(R.id.SpinnerNombreEstudios);
        SpinnerAdapter adapter;
        ArrayList<String> nombresEstudios = db.recogerNombresEstudios();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, rellenarSpinner(nombresEstudios));
        spinner.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.btnAñadirTatuador);
        FloatingActionButton nuevaWeb = findViewById(R.id.tatuAñadirWeb);
        nuevaWeb.setVisibility(View.GONE);

        if(!anadir){
            final int idTatuador = App.getIdTatuador();
            final Tatuador tatuador = db.recogerTatuador(idTatuador);

            rellenarDatosTatuador(tatuador);

            spinner.setSelection(posicionEstudio(nombresEstudios,tatuador.getIdEstudio()));
            nuevaWeb.setVisibility(View.VISIBLE);
            nuevaWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityAnadirTatuador.this);
                    alertDialog.setTitle(getString(R.string.nuevaWeb));

                    final EditText input = new EditText(ActivityAnadirTatuador.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton(getString(R.string.contraseñabtn), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String web = input.getText().toString();
                            db.insertarWeb(tatuador.getIdEstudio(), web, idTatuador);
                        }
                    });
                    alertDialog.show();
                }
            });
        }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String st_nombre = et_nombre.getText().toString();
                    String st_apellidos = et_apellidos.getText().toString();
                    String st_nombreArtistico = et_nombreArt.getText().toString();
                    String st_Estudio = spinner.getSelectedItem().toString();
                    int IdEstudio = db.recogerIdEstudio(st_Estudio);
                    if (st_nombre.equals("") || st_apellidos.equals("") || st_nombreArtistico.equals("") || st_Estudio.equals("")) {
                        Toast.makeText(getApplicationContext(), "You may fill every empty land to insert something", Toast.LENGTH_SHORT).show();
                    } else if(anadir) {

                        db.insertarTatuador(st_nombre,st_apellidos,st_nombreArtistico,IdEstudio);
                        Toast.makeText(getApplicationContext(), "El tatuador " + st_nombre + " ha sido creado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityAnadirTatuador.this, ActivityListaTatuadores.class);
                        startActivity(intent);
                    } else{
                        db.modificarTatuador(App.getIdTatuador(),st_nombre,st_apellidos,st_nombreArtistico,IdEstudio);
                        Toast.makeText(getApplicationContext(),"Los cambios se han realizado con exito", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ActivityAnadirTatuador.this, ActivityFichaTatuador.class);
                        intent.putExtra("id", App.getIdTatuador());
                        startActivity(intent);

                    }
                }
            });

        }

    private void rellenarDatosTatuador(Tatuador tatuador){
        et_nombre.setText(tatuador.getNombre());
        et_apellidos.setText(tatuador.getApellidos());
        et_nombreArt.setText(tatuador.getNombreArtistico());
    }

    private String[] rellenarSpinner (ArrayList<String> stringarray){
        String[] nombres = new String[stringarray.size()];

        for(int i=0; i<stringarray.size();i++){
            nombres[i]=stringarray.get(i);
        }
        return  nombres;
    }

    private int posicionEstudio(ArrayList<String> estudios, int idEstudio){
        int posicionEstudio=0;

        String nombreEstudio = db.recogerEstudio(idEstudio).getNombre();

        for(int i=0;i<estudios.size();i++){
            if(estudios.get(i).equals(nombreEstudio)){
                posicionEstudio = i;
            }
        }

        return posicionEstudio;
    }

}

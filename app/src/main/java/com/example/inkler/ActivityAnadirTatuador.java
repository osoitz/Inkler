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
//TODO: ¿es posible reducir llamadas a la BD y complejidad ciclomatica?
public class ActivityAnadirTatuador extends AppCompatActivity {

    private EditText et_nombre;
    private EditText et_apellidos;
    private EditText et_nombreArt;
    private DBlocal db;
    Spinner spinner;
    int idTatuador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBlocal(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_tatuador);
        et_nombre = findViewById(R.id.contentNombre);
        et_apellidos = findViewById(R.id.contentApellido);
        et_nombreArt = findViewById(R.id.contentNombreArtistico);
        final boolean anadir = getIntent().getBooleanExtra("añadir", false);
        spinner = findViewById(R.id.SpinnerNombreEstudios);
        SpinnerAdapter adapter;
        ArrayList<String> nombresEstudios = db.recogerNombresEstudios();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, rellenarSpinner(nombresEstudios));
        spinner.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.btnAñadirTatuador);
        FloatingActionButton nuevaWeb = findViewById(R.id.tatuAñadirWeb);
        nuevaWeb.setVisibility(View.GONE);

        if(!anadir){
            idTatuador = App.getIdTatuador();
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

                    alertDialog.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Web web = new Web();
                            web.setUrl(input.getText().toString());
                            web.setIdTatuador(idTatuador);
                            db.insertarWeb(web);
                        }
                    });
                    alertDialog.show();
                }
            });
        }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tatuador tatuador = recogerDatos();

                    if (anadir) {
                        db.insertarTatuador(tatuador);
                        Toast.makeText(getApplicationContext(), "El tatuador " + tatuador.getNombre() + " ha sido creado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityAnadirTatuador.this, ActivityListaTatuadores.class);
                        startActivity(intent);
                    } else{
                        tatuador.setId(idTatuador);
                        db.modificarTatuador(tatuador);
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

    private Tatuador recogerDatos(){
        /*
        if (st_nombre.equals("") || st_apellidos.equals("") || st_nombreArtistico.equals("") || st_Estudio.equals("")) {
                Toast.makeText(getApplicationContext(), "You may fill every empty land to insert something", Toast.LENGTH_SHORT).show();

         */
        Tatuador tatuador = new Tatuador();
        tatuador.setNombre(et_nombre.getText().toString());
        tatuador.setApellidos(et_apellidos.getText().toString());
        tatuador.setNombreArtistico(et_nombreArt.getText().toString());
        //TODO: Se puede coger el Id estudo mejor?
        String st_Estudio = spinner.getSelectedItem().toString();
        int idEstudio = db.recogerIdEstudio(st_Estudio);
        tatuador.setIdEstudio(idEstudio);
        return tatuador;
    }

}

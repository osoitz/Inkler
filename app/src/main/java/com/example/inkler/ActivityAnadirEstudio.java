package com.example.inkler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityAnadirEstudio extends AppCompatActivity {
    private static final String TAG = "ANADIR_ESTUDIO" ;
    private EditText et_nombre;
    private EditText et_telefono;
    private EditText et_direccion;
    private EditText et_email;
    private EditText et_longitud;
    private EditText et_latitud;
    private int idEstudio;
    private List<Web> webs = new ArrayList<>();
    private RecyclerView recyclerViewWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_estudio);
        final DBlocal db = new DBlocal(getApplicationContext());
        et_nombre = findViewById(R.id.contentNombre);
        et_telefono = findViewById(R.id.contentTelefono);
        et_direccion = findViewById(R.id.contentDireccion);
        et_email = findViewById(R.id.contentMail);
        et_longitud = findViewById(R.id.contentLongitud);
        et_latitud = findViewById(R.id.contentLatitud);
        recyclerViewWeb = findViewById(R.id.recycleranadirestudioweb);

        final FloatingActionButton botonGuardar = findViewById(R.id.btnAñadirEstudio);
        FloatingActionButton nuevaWeb = findViewById(R.id.estuAñadirWeb);
        nuevaWeb.setVisibility(View.GONE);

        final boolean anadir = getIntent().getBooleanExtra("añadir",false);


        if (!anadir) {
            //Estamos en modificar
            idEstudio = getIntent().getIntExtra("idEstudio", -1);
            Log.d(TAG, "onCreate: " + anadir + " " + idEstudio);
            Estudio estudio =  db.recogerEstudio(idEstudio);
            rellenarDatos(estudio);
            webs = db.recogerWebsEstudio(estudio.getIdEstudio());
            rellenarWebsEstudio();
            nuevaWeb.setVisibility(View.VISIBLE);
            nuevaWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityAnadirEstudio.this);
                    alertDialog.setTitle(getString(R.string.nuevaWeb));

                    final EditText input = new EditText(ActivityAnadirEstudio.this);
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
                            web.setIdEstudio(idEstudio);
                            db.insertarWeb(web);
                            webs = db.recogerWebsEstudio(idEstudio);
                            rellenarWebsEstudio();
                        }
                    });
                    alertDialog.show();
                }
            });

        }


        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Estudio nuevoEstudio = recogerDatos();

                // Iniciar base de datos
                if (anadir) {
                    db.insertarEstudio(nuevoEstudio);
                    Toast.makeText(getApplicationContext(), "El estudio " + nuevoEstudio.getNombre() + " ha sido creado", Toast.LENGTH_SHORT).show();
                } else {
                    nuevoEstudio.setIdEstudio(idEstudio);
                    db.modificarEstudio(nuevoEstudio);
                    Toast.makeText(getApplicationContext(), "El estudio " + nuevoEstudio.getNombre() + " ha sido modificado", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ActivityAnadirEstudio.this, ActivityListaTatuadores.class);
                startActivity(intent);
            }

    });

}

    private void rellenarDatos(Estudio estudio) {
        et_nombre.setText(estudio.getNombre());
        et_direccion.setText(estudio.getDireccion());
        et_email.setText(estudio.getEmail());
        et_telefono.setText(estudio.getTelefono());
        et_longitud.setText(String.valueOf(estudio.getLongitud()));
        et_latitud.setText(String.valueOf(estudio.getLatitud()));
    }

    private Estudio recogerDatos() {
    /*
        if (
                st_nombre.equals("") || st_direccion.equals("") || st_email.equals("") || st_latitud.equals("") || st_longitud.equals("") || st_telefono.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.fill_all, Toast.LENGTH_LONG).show();
        } else {

     */
        Estudio estudio = new Estudio();
        estudio.setNombre(et_nombre.getText().toString());
        estudio.setTelefono(et_telefono.getText().toString());
        estudio.setDireccion(et_direccion.getText().toString());
        estudio.setEmail(et_email.getText().toString());
        estudio.setLatitud(Double.parseDouble(et_latitud.getText().toString()));
        estudio.setLongitud(Double.parseDouble(et_longitud.getText().toString()));
        return estudio;
    }

    private void rellenarWebsEstudio(){
        AdaptadorWeb adaptadorWeb = new AdaptadorWeb(getApplicationContext(), webs);
        recyclerViewWeb.setAdapter(adaptadorWeb);
        RecyclerView.LayoutManager layoutManagerWeb = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewWeb.setLayoutManager(layoutManagerWeb);
    }
}
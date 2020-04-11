package com.example.inkler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityAnadirEstudio extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_estudio);
        final DBlocal db = new DBlocal(getApplicationContext());
        final boolean anadir = getIntent().getBooleanExtra(getString(R.string.anadir),false);
        final EditText et_nombre = findViewById(R.id.contentNombre);
        final EditText et_telefono = findViewById(R.id.contentTelefono);
        final EditText et_direccion = findViewById(R.id.contentDireccion);
        final EditText et_email = findViewById(R.id.contentMail);
        final EditText et_longitud = findViewById(R.id.contentLongitud);
        final EditText et_latitud = findViewById(R.id.contentLatitud);

        FloatingActionButton fab = findViewById(R.id.btnAñadirEstudio);
        FloatingActionButton nuevaWeb = findViewById(R.id.estuAñadirWeb);
        nuevaWeb.setVisibility(View.GONE);

        if (!anadir) {
            final int idTatuador = App.getIdTatuador();
            final int idEstudio = db.recogerTatuador(idTatuador).getIdEstudio();
            et_nombre.setText(db.recogerEstudio(idTatuador).getNombre());
            et_direccion.setText(db.recogerEstudio(idEstudio).getDireccion());
            et_email.setText(db.recogerEstudio(idEstudio).getEmail());
            et_telefono.setText(db.recogerEstudio(idEstudio).getTelefono());
            et_longitud.setText(String.valueOf(db.recogerEstudio(idEstudio).getLongitud()));
            et_latitud.setText(String.valueOf(db.recogerEstudio(idEstudio).getLatitud()));
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
                            String web = input.getText().toString();
                            db.insertarWeb(idEstudio, web, idTatuador);
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
                String st_telefono = et_telefono.getText().toString();
                String st_direccion = et_direccion.getText().toString();
                String st_email = et_email.getText().toString();
                String st_longitud = et_longitud.getText().toString();
                String st_latitud = et_latitud.getText().toString();

                if (st_nombre.equals("") || st_direccion.equals("") || st_email.equals("") || st_latitud.equals("") || st_longitud.equals("") || st_telefono.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.fill_all, Toast.LENGTH_LONG).show();
                } else {
                    double latitud = Double.parseDouble(st_latitud);
                    double longitud = Double.parseDouble(st_longitud);
                    // Iniciar base de datos
                    if (anadir) {
                        db.insertarEstudio(st_nombre, st_direccion, latitud, longitud, st_email, st_telefono);
                        Toast.makeText(getApplicationContext(), getString(R.string.estudio) + st_nombre + getString(R.string.creado), Toast.LENGTH_SHORT).show();
                    } else {
                        db.modificarEstudio(db.recogerTatuador(App.getIdTatuador()).getIdEstudio(), st_nombre, st_direccion, latitud, longitud, st_email, st_telefono);
                        Toast.makeText(getApplicationContext(), getString(R.string.estudio)  + st_nombre + getString(R.string.modificado) , Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(ActivityAnadirEstudio.this, ActivityListaTatuadores.class);
                    startActivity(intent);
                }
            }
        });



    }


}

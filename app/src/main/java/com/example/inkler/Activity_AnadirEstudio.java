package com.example.inkler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_AnadirEstudio extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__anadir_estudio);
        final DBlocal db = new DBlocal(getApplicationContext());
        final boolean anadir = getIntent().getBooleanExtra("añadir",false);
        final EditText et_nombre = findViewById(R.id.contentNombre);
        final EditText et_telefono = findViewById(R.id.contentTelefono);
        final EditText et_direccion = findViewById(R.id.contentDireccion);
        final EditText et_email = findViewById(R.id.contentMail);
        final EditText et_longitud = findViewById(R.id.contentLongitud);
        final EditText et_latitud = findViewById(R.id.contentLatitud);

        Button btn_anadir_estudio = findViewById(R.id.btnAñadirEstudio);

        if (!anadir) {
            String idTat = DatosApp.getIdTat();
            String idEst = db.recogerTatuador(idTat).getIDEstudio();
            et_nombre.setText(db.recogerEstudio(idTat).getNombre());
            et_direccion.setText(db.recogerEstudio(idEst).getDireccion());
            et_email.setText(db.recogerEstudio(idEst).getEmail());
            et_telefono.setText(db.recogerEstudio(idEst).getTelefono());
            et_longitud.setText(String.valueOf(db.recogerEstudio(idEst).getLongitud()));
            et_latitud.setText(String.valueOf(db.recogerEstudio(idEst).getLatitud()));
        }
        btn_anadir_estudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st_nombre = et_nombre.getText().toString();
                String st_telefono = et_telefono.getText().toString();
                String st_direccion = et_direccion.getText().toString();
                String st_email = et_email.getText().toString();
                String st_longitud = et_longitud.getText().toString();
                String st_latitud = et_latitud.getText().toString();

                if (st_nombre.equals("") || st_direccion.equals("") || st_email.equals("") || st_latitud.equals("") || st_longitud.equals("") || st_telefono.equals("")) {
                    Toast.makeText(getApplicationContext(), "Por favor rellena todos los datos", Toast.LENGTH_LONG).show();
                } else {
                    double latitud = Double.parseDouble(st_latitud);
                    double longitud = Double.parseDouble(st_longitud);
                    // Iniciar base de datos
                    if (anadir) {
                        db.insertarEstudio(st_nombre, st_direccion, latitud, longitud, st_email, st_telefono);
                        Toast.makeText(getApplicationContext(), "El estudio " + st_nombre + " ha sido creado", Toast.LENGTH_SHORT).show();
                    } else {
                        db.modificarEstudio(db.recogerTatuador(DatosApp.getIdTat()).getIDEstudio(), st_nombre, st_direccion, latitud, longitud, st_email, st_telefono);
                        Toast.makeText(getApplicationContext(), "El estudio " + st_nombre + " ha sido modificado", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(Activity_AnadirEstudio.this, RecyclerTatuadores.class);
                    startActivity(intent);
                }
            }
        });
    }


}

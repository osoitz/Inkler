package com.example.inkler;

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
        boolean anadir = getIntent().getBooleanExtra("añadir",false);
        final EditText et_nombre = findViewById(R.id.contentNombre);
        final EditText et_telefono = findViewById(R.id.contentTelefono);
        final EditText et_direccion = findViewById(R.id.contentDireccion);
        final EditText et_email = findViewById(R.id.contentMail);
        final EditText et_longitud = findViewById(R.id.contentLongitud);
        final EditText et_latitud = findViewById(R.id.contentLatitud);

        Button btn_anadir_estudio = findViewById(R.id.btnAñadirEstudio);

        if (anadir) {
            Log.d("TAG", "AÑADIR");
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
                        DBHelper dbHelper = new DBHelper(getBaseContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues e1 = new ContentValues();
                        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE, st_nombre);
                        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION, st_direccion);
                        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL, st_email);
                        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO, st_telefono);
                        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD, longitud);
                        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD, latitud);
                        db.insert(DBHelper.entidadEstudio.TABLE_NAME, null, e1);
                        Toast.makeText(getApplicationContext(), "El estudio " + st_nombre + " ha sido creado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_AnadirEstudio.this, RecyclerTatuadores.class);
                        startActivity(intent);
                    }
                }
            });
        } else {
            Log.d("TAG", "MODIFICAR");
            String idTat = DatosApp.getIdTat();
            String idEst = recogerTatuador(idTat).getIDEstudio();
            et_nombre.setText(recogerEstudio(idTat).getNombre());
            et_direccion.setText(recogerEstudio(idEst).getDireccion());
            et_email.setText(recogerEstudio(idEst).getEmail());
            et_telefono.setText(recogerEstudio(idEst).getTelefono());
            et_longitud.setText(String.valueOf(recogerEstudio(idEst).getLongitud()));
            et_latitud.setText(String.valueOf(recogerEstudio(idEst).getLatitud()));

        }
    }

    private Estudio recogerEstudio (String id) {
        Log.d("TAG", "RECOGER ESTUDIO");
        Estudio estudio = new Estudio();
        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Columnas
        String[] projection = {
                DBHelper.entidadEstudio._ID,
                DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE,
                DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION,
                DBHelper.entidadEstudio.COLUMN_NAME_LATITUD,
                DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD,
                DBHelper.entidadEstudio.COLUMN_NAME_EMAIL,
                DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO,
        };

        //Respuesta
        String[] selectionArgs = new String[] { "" + id } ;
        Cursor cursor = db.query(
                DBHelper.entidadEstudio.TABLE_NAME,
                projection,
                " _ID = ? ",
                selectionArgs,
                null,
                null,
                null);
        // recoger los datos
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            estudio.setID(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio._ID))));
            estudio.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE)));
            estudio.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION)));
            estudio.setLatitud(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD))));
            estudio.setLongitud(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD))));
            estudio.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL)));
            estudio.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO)));
        }
        return estudio;
    }

    public Tatuador recogerTatuador(String IdTatuador){
        Log.d("TAG", "RECOGER TATUADOR");
        Tatuador tatuador = new Tatuador();
        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Columnas
        String[] proyeccion = {DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE,DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS,DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO,DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO};
        String selection = DBHelper.entidadTatuador._ID + " = ?";
        String[] selectionArgs = new String[] { "" + IdTatuador } ;
        //Respuesta
        Cursor cursor = db.query(
                DBHelper.entidadTatuador.TABLE_NAME,
                proyeccion,
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
            tatuador= new Tatuador(IdTatuador,nombreArtistico,nombre,apellido,idEstudio);
        }
        return tatuador;
    }
}

package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class FichaTatuadorActivity extends AppCompatActivity {
    private MapView mapView;
    private TextView tlfno;
    private TextView NombreArt;
    private TextView NombreTat;
    private TextView EmailTat;
    public ArrayList<Tatuador>tatuadors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String idTat= getIntent().getStringExtra("id");
        NombreArt=findViewById(R.id.nombreArtistico);
        NombreTat=findViewById(R.id.nombreApellidos);
        EmailTat=findViewById(R.id.TattooMail);
        //tatuadors.clear();
        recogerTatuador(idTat);
        rellenar_txt();
        Mapbox.getInstance(this, "pk.eyJ1IjoiZXF1aXBhc28xIiwiYSI6ImNrMnhhMjg0YzA5cmEzanBtNndxejQ0ZWgifQ.QLRB9ZbTIevBBxwNYvjelw");
        setContentView(R.layout.activity_ficha_tatuador);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments.


                    }
                });
            }
        });
        tlfno = findViewById(R.id.phone_number);
        SpannableString mitextoU = new SpannableString(tlfno.getText().toString());
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tlfno.setText(mitextoU);
        tlfno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String num= tlfno.getText().toString();
                intent.setData(Uri.parse("tel:"+num));
                startActivity(intent);
            }
        });
    }

    public void recogerTatuador (String id){

        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Columnas
        String[] projection = {
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE,
                DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS,
                DBHelper.entidadTatuador.COLUMN_NAME_EMAIL,
                DBHelper.entidadTatuador.COLUMN_NAME_TELEFONO,
                DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO
        };
        //Respuesta
        String[] selectionArgs= { id };
        Cursor cursor = db.query(
                DBHelper.entidadTatuador.TABLE_NAME,
                projection,
                DBHelper.entidadTatuador._ID + " = ? ",
                selectionArgs,
                null,
                null,
                null);
        // recoger los datos
        while (cursor.moveToNext()) {
            String idtat = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID));
            String nombreArt = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_EMAIL));
            String IDEstudio = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            String telefono = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_TELEFONO));
            Tatuador t = new Tatuador(idtat,nombreArt,nombre,apellidos,email,telefono,IDEstudio);
            tatuadors.add(t);
     }
        cursor.close();


    }

    public void rellenar_txt(){
        String nombre = "("+tatuadors.get(0).getNombre()+" "+tatuadors.get(0).getApellidos()+")";
        NombreArt.setText(tatuadors.get(0).getNombreArt());
        NombreTat.setText(nombre);
        EmailTat.setText(tatuadors.get(0).getEmail());
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // Menu de la barra con botones atras e inicio
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        menu.setGroupVisible(R.id.gatras, true);
        return true;
    }

    //Funcion de los botones del menu
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_atras) {
            Intent intent = new Intent(FichaTatuadorActivity.this, RecyclerTatuadores.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
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
    private ImageView vermas;
    private TextView NombreTat;
    private TextView EmailTat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String idTat = getIntent().getStringExtra("id");
        Mapbox.getInstance(this, "pk.eyJ1IjoiZXF1aXBhc28xIiwiYSI6ImNrMnhhMjg0YzA5cmEzanBtNndxejQ0ZWgifQ.QLRB9ZbTIevBBxwNYvjelw");
        final Integer INITIAL_ZOOM = 10;
        final Integer millisecondSpeed = 1000;
        setContentView(R.layout.activity_ficha_tatuador);
        NombreTat=findViewById(R.id.nombreApellidos);
        EmailTat=findViewById(R.id.TattooMail);
        Tatuador miTatuador = recogerTatuador(idTat);
        Toast.makeText(getApplicationContext(),idTat + " : " + miTatuador.getIDEstudio(), Toast.LENGTH_LONG).show();
        final Estudio miEstudio = recogerEstudio(miTatuador.getIDEstudio());
        rellenar_txt(miTatuador);

        vermas = findViewById(R.id.ivvermas);
        vermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FichaTatuadorActivity.this, GaleriaActivity.class));
            }
        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
                        //Markagailua
                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(miEstudio.getLatitud(), miEstudio.getLongitud()))
                                .title(miEstudio.getNombre())
                        );

                        //Kamera posiziora
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(miEstudio.getLatitud(), miEstudio.getLongitud()))
                                .zoom(INITIAL_ZOOM)
                                .tilt(20)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), millisecondSpeed);

                        //Listener
                        /*
                        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                //Soinua
                                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.misc021);
                                mediaPlayer.start();
                                //Toast.makeText(Mapa.this, marker.getTitle(), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), Fitxa.class);
                                startActivity(i);
                                return true;
                            }
                        });

                         */

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

    private Tatuador recogerTatuador (String id){
        Tatuador tatuador = new Tatuador();
        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Columnas
        String[] projection = {
                DBHelper.entidadTatuador._ID,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE,
                DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS,
                DBHelper.entidadTatuador.COLUMN_NAME_EMAIL,
                DBHelper.entidadTatuador.COLUMN_NAME_TELEFONO,
                DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO
        };

        //Respuesta
        String[] selectionArgs = new String[] { "" + id } ;
        Cursor cursor = db.query(
                DBHelper.entidadTatuador.TABLE_NAME,
                projection,
                 " _ID = ? ",
                selectionArgs,
                null,
                null,
                null);
        // recoger los datos
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            tatuador.setId(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID)));
            tatuador.setNombreArt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO)));
            tatuador.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE)));
            tatuador.setApellidos(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS)));
            tatuador.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_EMAIL)));
            tatuador.setIDEstudio(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO)));
            tatuador.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_TELEFONO)));

     }
        cursor.close();
        return tatuador;

    }

    private Estudio recogerEstudio (String id) {
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
        cursor.close();
        return estudio;
    }



    public void rellenar_txt(Tatuador miTatuador){
        TextView NombreArt;
       String nombre = "("+miTatuador.getNombre()+" "+miTatuador.getApellidos()+")";
        NombreArt = findViewById(R.id.nombreArtistico);
        NombreArt.setText(miTatuador.getNombreArt());
        NombreTat.setText(nombre);
        EmailTat.setText(miTatuador.getEmail());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        if (DatosApp.isAdmin()) {
            menu.setGroupVisible(R.id.añadir, true);
            menu.setGroupVisible(R.id.modificar, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        /*if (id == R.id.añadir_tatuador) {
            Intent intent = new Intent(FichaTatuadorActivity.this, Activity_AnadirTatuador.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.añadir_estudio) {
            Intent intent = new Intent(FichaTatuadorActivity.this, Activity_AnadirEstudio.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_tatuador) {
            //Intent intent = new Intent(FichaTatuadorActivity.this, Activity_ModificarTatuador.class);
            //startActivity(intent);
            return true;
        } else if (id == R.id.modificar_estudio) {
            //Intent intent = new Intent(FichaTatuadorActivity.this, Activity_ModificarEstudio.class);
            //startActivity(intent);
            return true;

        }*/

        return super.onOptionsItemSelected(item);
    }

}

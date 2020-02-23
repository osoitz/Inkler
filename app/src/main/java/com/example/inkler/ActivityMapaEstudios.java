package com.example.inkler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.mapbox.mapboxsdk.maps.UiSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * The most basic example of adding a map to an activity.
 */
public class ActivityMapaEstudios extends AppCompatActivity {

    private MapView mapView;
    private int INITIAL_ZOOM = 5;
    private int millisecondSpeed = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapBoxAcessToken));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_mapa_estudios);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments.

                        //Desactivamos la inclinacion del mapa, de esa forma no pueden aparecer los puntos dos veces
                        UiSettings uiSettings = mapboxMap.getUiSettings();
                        uiSettings.setTiltGesturesEnabled(false);

                        //Añadimos los markers de los estudios y posicionameos la camara
                        final DBlocal db = new DBlocal(getApplicationContext());
                        ArrayList<Estudio> estudios = db.recogerEstudios();
                        Double minLat = 90.0; //Estan al reves a posta, no lo corrijais!
                        Double maxLat = -90.0;
                        Double minLon = 180.0;
                        Double maxLon = -180.0;

                        for (Estudio estudio : estudios) {
                            //Ponemos cada marker
                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(estudio.getLatitud(), estudio.getLongitud()))
                                    .title(estudio.getNombre())
                                    .setSnippet(estudio.getDireccion())
                            );

                            //Calculamos lat y lon min y max
                            if (estudio.getLatitud() < minLat) {
                                minLat = estudio.getLatitud();
                            }
                            else if (estudio.getLatitud() > maxLat){
                                maxLat = estudio.getLatitud();
                            }
                            if (estudio.getLongitud() < minLon) {
                                minLon = estudio.getLongitud();
                            }
                            else if (estudio.getLongitud() > maxLon){
                                maxLon = estudio.getLongitud();
                            }
                        }

                        //Colocamos la camara en el centro del area delimitada por los puntos
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng((minLat + maxLat)/2, (minLon + maxLon)/2))
                                .zoom(INITIAL_ZOOM)
                                .tilt(20)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), millisecondSpeed);

                        //Floating Action Button
                        FloatingActionButton fabLista = findViewById(R.id.fabLista);

                        fabLista.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), ActivityListaTatuadores.class);
                                startActivity(intent);
                            }
                        });

                        //Listener markers
                        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                Toast.makeText(ActivityMapaEstudios.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ActivityFichaEstudio.class);
                                intent.putExtra("idEstudio", db.recogerIdEstudio(marker.getTitle()));
                                startActivity(intent);
                                //Si pasamos por aqui es que no nos hemos ido (creo)
                                return false;
                            }
                        });
                    }
                });
            }
        });
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
        if (App.isAdmin()) {
            menu.setGroupVisible(R.id.añadir, true);
            menu.setGroupVisible(R.id.modificar, true);
            menu.setGroupVisible(R.id.logout, true);
        } else {
            menu.setGroupVisible(R.id.login, true);
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
        if (id == R.id.admin){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.contraseñatitle));

            final EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            alertDialog.setView(input);

            alertDialog.setPositiveButton(getString(R.string.contraseñabtn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String password = input.getText().toString();
                    if (getString(R.string.contraseña).equals(password)){
                        App.setAdmin(true);
                        invalidateOptionsMenu();
                    }
                }
            });
            alertDialog.show();
        } else if (id == R.id.noadmin) {
            App.setAdmin(false);
            invalidateOptionsMenu();
        }
        else if (id == R.id.añadir_tatuador) {
            Intent intent = new Intent(ActivityMapaEstudios.this, ActivityAnadirTatuador.class);
            intent.putExtra("añadir",true);
            startActivity(intent);
            return true;
        } else if (id == R.id.añadir_estudio) {
            Intent intent = new Intent(ActivityMapaEstudios.this, ActivityAnadirEstudio.class);
            intent.putExtra("añadir",true);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_tatuador) {
            Intent intent = new Intent(ActivityMapaEstudios.this, ActivityAnadirTatuador.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_estudio) {
            Intent intent = new Intent(ActivityMapaEstudios.this, ActivityAnadirEstudio.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
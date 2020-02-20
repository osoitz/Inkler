package com.example.inkler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * The most basic example of adding a map to an activity.
 */
public class Mapa extends AppCompatActivity {

    private MapView mapView;
    Integer INITIAL_ZOOM = 5;
    Integer millisecondSpeed = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapBoxAcessToken));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_mapa);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
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
                                Intent intent = new Intent(getApplicationContext(), RecyclerTatuadores.class);
                                startActivity(intent);
                            }
                        });

                        //Listener markers
                        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                Toast.makeText(Mapa.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), FichaEstudio.class);
                                intent.putExtra("idEstudio", db.RecogerIdEstudio(marker.getTitle()));
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
}
package com.example.inkler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.ArrayList;

public class FichaEstudio extends AppCompatActivity {
    private MapView mapView;
    private MetodosComunes metodosComunes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBlocal db = new DBlocal(getApplicationContext());
        metodosComunes=new MetodosComunes();
        int idEstudio = getIntent().getIntExtra("idEstudio",0);
        final Estudio estudio = db.recogerEstudio(Integer.toString(idEstudio));
        final Integer INITIAL_ZOOM = 16;
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapBoxAcessToken));
        final Integer millisecondSpeed = 1000;
        setContentView(R.layout.activity_ficha_estudio);

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
                                .position(new LatLng(estudio.getLatitud(), estudio.getLongitud()))
                                .title(estudio.getNombre())
                        );

                        //Kamera posiziora
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(estudio.getLatitud(), estudio.getLongitud()))
                                .zoom(INITIAL_ZOOM)
                                .tilt(20)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), millisecondSpeed);

                    }
                });
            }
        });

        TextView NombreEstudio = findViewById(R.id.labelNombreEstudio);
        NombreEstudio.setText(estudio.getNombre());
        TextView DireccionEstudio = findViewById(R.id.printDireccion);
        DireccionEstudio.setText(estudio.getDireccion());
        rellenarWebsEstudio(db.recogerWebsEstudio(Integer.toString(estudio.getID())));
        TextView Email = findViewById(R.id.contentMailEstudio);
        Email.setText(estudio.getEmail());



        final TextView telefono = findViewById(R.id.contentTelefono);

        SpannableString mitextoU = new SpannableString(telefono.getText().toString());
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        telefono.setText(mitextoU);
        telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String num= telefono.getText().toString();
                intent.setData(Uri.parse("tel:"+num));
                startActivity(intent);
            }
        });
    }
    private void rellenarWebsEstudio(ArrayList<String> urls){
        TextView websEstudio = findViewById(R.id.contentWebEstudio);
        websEstudio.setText(Html.fromHtml(metodosComunes.crearContenidoHTML(urls)));
    }
}

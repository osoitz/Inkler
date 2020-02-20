package com.example.inkler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import androidx.annotation.NonNull;

import java.net.URL;
import java.util.ArrayList;


public class FichaTatuadorActivity extends AppCompatActivity {
    private MapView mapView;
    private TextView telefono;
    private ImageView vermas;
    private boolean anadir;
    private MetodosComunes metodosComunes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Instanciamos la clasde que tiene los metodos de la DB
        DBlocal db = new DBlocal(getApplicationContext());

        super.onCreate(savedInstanceState);
        String idTat = "";
        if(getIntent().getStringExtra("id") == null){
            idTat = DatosApp.getIdTat();
        }else{
            idTat = getIntent().getStringExtra("id");
            DatosApp.setIdTat(idTat);
        }

        Mapbox.getInstance(this, getString(R.string.mapBoxAcessToken));
        final Integer INITIAL_ZOOM = 16;
        final Integer millisecondSpeed = 1000;
        setContentView(R.layout.activity_ficha_tatuador);
        metodosComunes=new MetodosComunes();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Tatuador miTatuador = db.recogerTatuador(idTat);
        final Estudio miEstudio = db.recogerEstudio(miTatuador.getIDEstudio());
        //Toast.makeText(getApplicationContext(),miEstudio.getLatitud() + " : " + miEstudio.getLongitud(), Toast.LENGTH_LONG).show();
        rellenar_txt(miTatuador, miEstudio);
        rellenarWebsTatuador(db.recogerWebsTatuador(miTatuador.getId()));
        rellenarWebsEstudio(db.recogerWebsEstudio(Integer.toString(miEstudio.getID())));

        vermas = findViewById(R.id.ivvermas);
        vermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FichaTatuadorActivity.this, GaleriaActivity.class);

                startActivity(intent);
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

                    }
                });
            }
        });
        telefono = findViewById(R.id.telefonoEstudio);
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

    private void rellenar_txt(Tatuador miTatuador, Estudio miestudio){
        TextView nombreArtistico = findViewById(R.id.nombreArtistico);
        TextView nombreTatuador =findViewById(R.id.nombreApellidos);
        TextView nombreEstudio =findViewById(R.id.NombreEstudio);
        TextView direccionEstudio =findViewById(R.id.direccionEstudio);
        TextView mailEstudio =findViewById(R.id.mailEstudio);
        TextView telefonoEstudio =findViewById(R.id.telefonoEstudio);

        nombreArtistico.setText(miTatuador.getNombreArt());
        String nombre = "(" + miTatuador.getNombre() + " " + miTatuador.getApellidos() + ")";
        nombreTatuador.setText(nombre);

        nombreEstudio.setText(miestudio.getNombre());
        direccionEstudio.setText(miestudio.getDireccion());
        mailEstudio.setText(miestudio.getEmail());
        telefonoEstudio.setText(miestudio.getTelefono());
    }


    private void rellenarWebsTatuador(ArrayList<String> urls){
        TextView websTatuador = findViewById(R.id.websTatuador);
        websTatuador.setText(Html.fromHtml(metodosComunes.crearContenidoHTML(urls)));
    }

    private void rellenarWebsEstudio(ArrayList<String> urls){
        TextView websEstudio = findViewById(R.id.websEstudio);
        websEstudio.setText(Html.fromHtml(metodosComunes.crearContenidoHTML(urls)));
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
                        DatosApp.setAdmin(true);
                        invalidateOptionsMenu();
                    }
                }
            });
            alertDialog.show();
        } else if (id == R.id.noadmin) {
            DatosApp.setAdmin(false);
            invalidateOptionsMenu();
        }
        else if (id == R.id.añadir_tatuador) {
            Intent intent = new Intent(FichaTatuadorActivity.this, Activity_AnadirTatuador.class);
            anadir = true;
            intent.putExtra("añadir",anadir);
            startActivity(intent);
            return true;
        } else if (id == R.id.añadir_estudio) {
            Intent intent = new Intent(FichaTatuadorActivity.this, Activity_AnadirEstudio.class);
            intent.putExtra("añadir",true);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_tatuador) {
            Intent intent = new Intent(FichaTatuadorActivity.this, Activity_AnadirTatuador.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_estudio) {
            Intent intent = new Intent(FichaTatuadorActivity.this, Activity_AnadirEstudio.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}

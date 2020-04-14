package com.example.inkler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class ActivityFichaEstudio extends AppCompatActivity {
    private static final String TAG = "FICHAESTUDIO";
    private MapView mapView;
    private DBlocal db;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewWeb;
    private Estudio estudio;
    private List<Web> webs = new ArrayList<>();
    private List<Tatuador> tatuadores = new ArrayList<>();
    private int idEstudio;
    private int INITIAL_ZOOM = 14;
    private int millisecondSpeed = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, App.mapBoxAcessToken);
        setContentView(R.layout.activity_ficha_estudio);

        //Variables
        idEstudio = getIntent().getIntExtra("idEstudio",0);
        recyclerView = findViewById(R.id.recyclerTatEstudio);
        recyclerViewWeb = findViewById(R.id.recyclerestudioweb);


        //Coger datos
        db = new DBlocal(getApplicationContext());
        estudio = db.recogerEstudio(idEstudio);
        rellenarCampos();
        webs = db.recogerWebsEstudio(estudio.getIdEstudio());
        rellenarWebsEstudio();
        cargartatuadores();

        // onclick  del recycler de tatuadores
        recyclerView.addOnItemTouchListener(new RecyclerViewListener(this, recyclerView, new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ActivityFichaEstudio.this, ActivityFichaTatuador.class);
                Tatuador tatuador = tatuadores.get(position);
                intent.putExtra("idTatuador",tatuador.getId());
                startActivity(intent);
            }
/*
            @Override
            public void onLongItemClick(View view, int position) {
                //Nichts
            }
  */
        }));

        //onclick del recycler de webs
        recyclerViewWeb.addOnItemTouchListener(new RecyclerViewListener(this, recyclerView, new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ActivityFichaEstudio.this, ActivityNavegador.class);
                Web web = webs.get(position);
                intent.putExtra("url", web.getUrl());
                startActivity(intent);
            }
        }));

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

    private void rellenarCampos() {
        TextView NombreEstudio = findViewById(R.id.labelNombreEstudio);
        NombreEstudio.setText(estudio.getNombre());
        TextView DireccionEstudio = findViewById(R.id.printDireccion);
        DireccionEstudio.setText(estudio.getDireccion());

        final TextView Email = findViewById(R.id.contentMailEstudio);
        Email.setText(estudio.getEmail());

        TextView contentTelefono = findViewById(R.id.contentTelefono);
        final String telefono = estudio.getTelefono();

        SpannableString mitextoU = new SpannableString(telefono);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        contentTelefono.setText(mitextoU);
        contentTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + telefono));
                startActivity(intent);
            }
        });
    }

    private void rellenarWebsEstudio(){
        AdaptadorWeb adaptadorWeb = new AdaptadorWeb(getApplicationContext(), webs);
        recyclerViewWeb.setAdapter(adaptadorWeb);
        RecyclerView.LayoutManager layoutManagerWeb = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewWeb.setLayoutManager(layoutManagerWeb);
    }


    private void cargartatuadores() {
        tatuadores = db.recogerTatuadoresEstudio(idEstudio);
        AdaptadorTatuadores adaptador = new AdaptadorTatuadores(getApplicationContext(), tatuadores);
        recyclerView.setAdapter(adaptador);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        if (App.isAdmin()) {
            menu.setGroupVisible(R.id.añadir_est, true);
            menu.setGroupVisible(R.id.modificar_est, true);
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
        } else if (id == R.id.añadir_estudio) {
            Intent intent = new Intent(ActivityFichaEstudio.this, ActivityAnadirEstudio.class);
            intent.putExtra("añadir",true);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_estudio) {
            Intent intent = new Intent(ActivityFichaEstudio.this, ActivityAnadirEstudio.class);
            intent.putExtra("idEstudio", idEstudio);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}



package com.example.inkler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class FichaEstudio extends AppCompatActivity {
    private MapView mapView;
    private MetodosComunes metodosComunes;
    private DBlocal db;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorTatuadores adaptador;
    private Estudio estudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBlocal(getApplicationContext());
        metodosComunes=new MetodosComunes();
        final int idEstudio = getIntent().getIntExtra("idEstudio",0);
        estudio = db.recogerEstudio(Integer.toString(idEstudio));
        final Integer INITIAL_ZOOM = 16;
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapBoxAcessToken));
        final Integer millisecondSpeed = 1000;
        setContentView(R.layout.activity_ficha_estudio);

        cargartatuadores();

        //Acciones del onclick y onlongclick del recycler
        recyclerView.addOnItemTouchListener(new TatuadoresRecyclerViewListener(this, recyclerView, new TatuadoresRecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(FichaEstudio.this, FichaTatuadorActivity.class);
                Tatuador tatuador = Tatuador.getTatuadorList().get(position);
                intent.putExtra("id",tatuador.getId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //Nichts
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
    private void cargartatuadores() {

        Tatuador.getTatuadorList().clear();
        String nombreEstudio = estudio.getNombre();
        int idEstudio = db.RecogerIdEstudio(nombreEstudio);
        String idEstudioMetodo = String.valueOf(idEstudio);
        db.cargarTatuadoresFiltrado(idEstudioMetodo);

        recyclerView = findViewById(R.id.recyclerTatEstudio);
        adaptador = new AdaptadorTatuadores(getApplicationContext(), Tatuador.getTatuadorList());
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
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
            Intent intent = new Intent(FichaEstudio.this, Activity_AnadirTatuador.class);
            intent.putExtra("añadir",true);
            startActivity(intent);
            return true;
        } else if (id == R.id.añadir_estudio) {
            Intent intent = new Intent(FichaEstudio.this, Activity_AnadirEstudio.class);
            intent.putExtra("añadir",true);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_tatuador) {
            Intent intent = new Intent(FichaEstudio.this, Activity_AnadirTatuador.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_estudio) {
            Intent intent = new Intent(FichaEstudio.this, Activity_AnadirEstudio.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}


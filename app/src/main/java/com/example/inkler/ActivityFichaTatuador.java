package com.example.inkler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class ActivityFichaTatuador extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorWeb adaptador;
    private ImageView vermas;
    private boolean anadir;

    List<Web> webs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Instanciamos la clasde que tiene los metodos de la DB
        DBlocal db = new DBlocal(getApplicationContext());
        super.onCreate(savedInstanceState);

        final int idTatuador;

        if(getIntent().getIntExtra("idTatuador", -1) == -1){
            idTatuador = App.getIdTatuador();
        }else{
            idTatuador = getIntent().getIntExtra("idTatuador", -1);
            App.setIdTatuador(idTatuador);
        }
        setContentView(R.layout.activity_ficha_tatuador);
        //metodosComunes=new MetodosComunes();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Tatuador tatuador = db.recogerTatuador(idTatuador);
        final Estudio estudio = db.recogerEstudio(tatuador.getIdEstudio());
        //Toast.makeText(getApplicationContext(),estudio.getLatitud() + " : " + estudio.getLongitud(), Toast.LENGTH_LONG).show();
        rellenar_txt(tatuador, estudio);
        rellenarWebsTatuador(db.recogerWebsTatuador(tatuador.getId()));
        vermas = findViewById(R.id.ivvermas);
        vermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityFichaTatuador.this, ActivityGaleria.class);
                intent.putExtra("idTatuador", idTatuador);
                startActivity(intent);
            }
        });
        TextView et_nombreEstudio = findViewById(R.id.nombreEstudio);
        String nombreEstudio = et_nombreEstudio.getText().toString();
        final int idEstudio = db.recogerIdEstudio(nombreEstudio);
        et_nombreEstudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityFichaTatuador.this, ActivityFichaEstudio.class);
                intent.putExtra("idEstudio",idEstudio);
                startActivity(intent);
            }
        });

        //Acciones del onclick y onlongclick del recycler
        recyclerView.addOnItemTouchListener(new RecyclerViewListener(this, recyclerView, new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ActivityFichaTatuador.this, ActivityNavegador.class);

                Web w = webs.get(position);
                intent.putExtra("url", w.getUrl());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //Nichts
            }
        }));



    }

    private void rellenar_txt(Tatuador miTatuador, Estudio miestudio){
        TextView nombreArtistico = findViewById(R.id.nombreArtistico);
        TextView nombreTatuador =findViewById(R.id.nombreApellidos);
        TextView nombreEstudio =findViewById(R.id.nombreEstudio);

        nombreArtistico.setText(miTatuador.getNombreArtistico());
        String nombre = "(" + miTatuador.getNombre() + " " + miTatuador.getApellidos() + ")";
        nombreTatuador.setText(nombre);

        SpannableString mitextoU = new SpannableString(miestudio.getNombre());
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        nombreEstudio.setText(mitextoU);
    }


    private void rellenarWebsTatuador(List<Web> urls){
        webs.addAll(urls);
        recyclerView = findViewById(R.id.recyclertatuadorweb);
        adaptador = new AdaptadorWeb(getApplicationContext(), webs);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = findViewById(R.id.recyclertatuadorweb);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
            Intent intent = new Intent(ActivityFichaTatuador.this, ActivityAnadirTatuador.class);
            anadir = true;
            intent.putExtra("añadir",anadir);
            startActivity(intent);
            return true;
        } else if (id == R.id.añadir_estudio) {
            Intent intent = new Intent(ActivityFichaTatuador.this, ActivityAnadirEstudio.class);
            intent.putExtra("añadir",true);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_tatuador) {
            Intent intent = new Intent(ActivityFichaTatuador.this, ActivityAnadirTatuador.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.modificar_estudio) {
            Intent intent = new Intent(ActivityFichaTatuador.this, ActivityAnadirEstudio.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
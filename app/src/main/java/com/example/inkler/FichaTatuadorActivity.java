package com.example.inkler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class FichaTatuadorActivity extends AppCompatActivity {

    private ImageView vermas;
    private boolean anadir;
    private MetodosComunes metodosComunes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Instanciamos la clasde que tiene los metodos de la DB
        DBlocal db = new DBlocal(getApplicationContext());
        super.onCreate(savedInstanceState);

        final String idTatuador;
        if(getIntent().getStringExtra("id") == null){
            idTatuador = DatosApp.getIdTatuador();
        }else{
            idTatuador = getIntent().getStringExtra("id");
            DatosApp.setIdTatuador(idTatuador);
        }
        setContentView(R.layout.activity_ficha_tatuador);
        metodosComunes=new MetodosComunes();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Tatuador miTatuador = db.recogerTatuador(idTatuador);
        final Estudio miEstudio = db.recogerEstudio(miTatuador.getIDEstudio());
        //Toast.makeText(getApplicationContext(),miEstudio.getLatitud() + " : " + miEstudio.getLongitud(), Toast.LENGTH_LONG).show();
        rellenar_txt(miTatuador, miEstudio);
        rellenarWebsTatuador(db.recogerWebsTatuador(miTatuador.getId()));
        vermas = findViewById(R.id.ivvermas);
        vermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FichaTatuadorActivity.this, GaleriaActivity.class);
                intent.putExtra("idTatuador", idTatuador);
                startActivity(intent);
            }
        });
        TextView et_nombreEstudio = findViewById(R.id.nombreEstudio);
        String nombreEstudio = et_nombreEstudio.getText().toString();
        final int idEstudio = db.RecogerIdEstudio(nombreEstudio);
        et_nombreEstudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FichaTatuadorActivity.this,FichaEstudio.class);
                intent.putExtra("idEstudio",idEstudio);
                startActivity(intent);
            }
        });





    }

    private void rellenar_txt(Tatuador miTatuador, Estudio miestudio){
        TextView nombreArtistico = findViewById(R.id.nombreArtistico);
        TextView nombreTatuador =findViewById(R.id.nombreApellidos);
        TextView nombreEstudio =findViewById(R.id.nombreEstudio);

        nombreArtistico.setText(miTatuador.getNombreArt());
        String nombre = "(" + miTatuador.getNombre() + " " + miTatuador.getApellidos() + ")";
        nombreTatuador.setText(nombre);

        nombreEstudio.setText(miestudio.getNombre());
    }


    private void rellenarWebsTatuador(ArrayList<String> urls){
        TextView websTatuador = findViewById(R.id.websTatuador);
        websTatuador.setText(Html.fromHtml(metodosComunes.crearContenidoHTML(urls)));
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

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ActivityListaTatuadores extends AppCompatActivity {

    // Variables necesarias
    private RecyclerView recyclerView;
    //private AdaptadorTatuadores adaptador;
    private List<Tatuador> tatuadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tatuadores);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityMapaEstudios.class);
                startActivity(intent);
            }
        });

        //Alimentamos el adaptador desde la BD
        cargarTatuadores();

        //Acciones del onclick y onlongclick del recycler
        recyclerView.addOnItemTouchListener(new RecyclerViewListener(this, recyclerView, new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ActivityListaTatuadores.this, ActivityFichaTatuador.class);
                Tatuador tatuador = tatuadores.get(position);
                App.setIdTatuador(tatuador.getId());
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
    }

    private void cargarTatuadores() {
        DBlocal db = new DBlocal(getApplicationContext());
        tatuadores = db.recogerTatuadores();

        recyclerView = findViewById(R.id.recyclerFragment);
        AdaptadorTatuadores adaptador = new AdaptadorTatuadores(getApplicationContext(), tatuadores);
        recyclerView.setAdapter(adaptador);

        //TODO: ¿Podemos hacer esto sin cargarnos el id del layout?
        ConstraintLayout cl = findViewById(R.id.activity_lista_tatuadores);
        RecyclerView.LayoutManager layoutManager;
        if (cl == null) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        } else {
            layoutManager = new GridLayoutManager(getApplicationContext(), 5);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = findViewById(R.id.recyclerFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        if (App.isAdmin()) {
            menu.setGroupVisible(R.id.añadir_tat, true);
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
        } else if (id == R.id.añadir_tatuador) {
            Intent intent = new Intent(ActivityListaTatuadores.this, ActivityAnadirTatuador.class);
            intent.putExtra("añadir",true);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
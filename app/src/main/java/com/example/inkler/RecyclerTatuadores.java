package com.example.inkler;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class RecyclerTatuadores extends AppCompatActivity {

    // Variables necesarias
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorTatuadores adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_tatuadores);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Mapa.class);
                startActivity(intent);
            }
        });

        cargartatuadores();

        //Acciones del onclick y onlongclick del recycler
        recyclerView.addOnItemTouchListener(new TatuadoresRecyclerViewListener(this, recyclerView, new TatuadoresRecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(RecyclerTatuadores.this, FichaTatuadorActivity.class);
                Tatuador tatuador = Tatuador.getTatuadorList().get(position);
                intent.putExtra("id",tatuador.getId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));


    }

    private void cargartatuadores() {

        Tatuador.getTatuadorList().clear();

        // Iniciar base de datos
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Columnas
        String[] proyeccion = {DBHelper.entidadTatuador._ID,DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO};
        //Respuesta
        Cursor cursor = db.query(DBHelper.entidadTatuador.TABLE_NAME, proyeccion, null, null, null, null, null);
        // recoger los datos
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID));
            String nombreArt = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS));
            String IDEstudio = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            Tatuador t = new Tatuador(id,nombreArt, nombre, apellidos, IDEstudio);
            Tatuador.getTatuadorList().add(t);
        }
        cursor.close();

        /*Tatuador t = new Tatuador("PinxaUvas", "Antonio", "Lopez Garcia", "APU@gmail.com", "653951284", "Hola");
        Tatuador.getTatuadorList().add(t);*/

        recyclerView = findViewById(R.id.recyclerFragment);
        adaptador = new AdaptadorTatuadores(getApplicationContext(), Tatuador.getTatuadorList());
        recyclerView.setAdapter(adaptador);
        ConstraintLayout cl = findViewById(R.id.recycler_tatuadores);
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
        if (DatosApp.isAdmin()) {
            menu.setGroupVisible(R.id.añadir, true);
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
                        DatosApp.setAdmin(true);
                        invalidateOptionsMenu();
                    }
                }
            });
            alertDialog.show();

        } else if (id == R.id.noadmin) {
            DatosApp.setAdmin(false);
            invalidateOptionsMenu();
        }else if (id == R.id.añadir_estudio) {
            Intent intent = new Intent(RecyclerTatuadores.this, Activity_AnadirEstudio.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.añadir_tatuador) {
            Intent intent = new Intent(RecyclerTatuadores.this, Activity_AnadirTatuador.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
package com.example.inkler;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerTatuadores extends AppCompatActivity {

    // Variables necesarias
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorAlumnos adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlite();

        //Acciones del onclick y onlongclick del recycler
        recyclerView.addOnItemTouchListener(new AlumnoRecyclerViewListener(this, recyclerView, new AlumnoRecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));


    }

    private void sqlite() {
        Alumno a = new Alumno("A", "A", "A", "A");
        Alumno.getAlumnoList().add(a);

        recyclerView = findViewById(R.id.recyclerFragment);
        adaptador = new AdaptadorAlumnos(getApplicationContext(), Alumno.getAlumnoList());
        recyclerView.setAdapter(adaptador);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = findViewById(R.id.recyclerFragment);
    }

}

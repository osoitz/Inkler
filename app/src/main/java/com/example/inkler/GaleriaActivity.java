package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GaleriaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorGaleria adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);



        recyclerView = findViewById(R.id.recyclerGaleria);
        adaptador = new AdaptadorGaleria(getApplicationContext(), Galeria.getGaleriaList());
        recyclerView.setAdapter(adaptador);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = findViewById(R.id.recyclerGaleria);

        recyclerView.addOnItemTouchListener(new GaleriaRecyclerViewListener(this, recyclerView, new GaleriaRecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


            }
            public void onFragmentInteraction(Uri uri){
                //you can leave it empty
            }
            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));

    }
}

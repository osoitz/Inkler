package com.example.inkler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Clase adaptador para el recyclerview de los tatuadores

public class AdaptadorTatuadores extends RecyclerView.Adapter<AdaptadorTatuadores.ViewHolder> {
    private LayoutInflater inflador;
    private List<Tatuador> tatuadores;

    //Constructor
    AdaptadorTatuadores(Context contexto, List<Tatuador> tatuadores){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tatuadores = tatuadores;
    }


    //Clase ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombreArtistico;
        TextView nombre;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreArtistico = itemView.findViewById(R.id.nombreArtistico);
            nombre = itemView.findViewById(R.id.nombre);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = inflador.inflate(R.layout.contenido_recycler_tatuador,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Meto los datos de tatuador al selector
        Tatuador tatuador = tatuadores.get(position);
        holder.nombreArtistico.setText(tatuador.getNombreArtistico());
    }

    @Override
    public int getItemCount() {
        return tatuadores.size();
    }

}
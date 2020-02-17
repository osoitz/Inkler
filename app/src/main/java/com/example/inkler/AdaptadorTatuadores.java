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

// Clase adaptador para el recyclerview de los alumnos

public class AdaptadorTatuadores extends RecyclerView.Adapter<AdaptadorTatuadores.ViewHolder> {
    private LayoutInflater inflador;
    private List<Tatuador> listatatuadores;

    //Constructor
    AdaptadorTatuadores(Context contexto, List<Tatuador> listatatuador){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listatatuadores = listatatuador;
    }


    //Clase ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView NombreArt;
        TextView Nombre;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreArt = itemView.findViewById(R.id.NombreArt);
            Nombre = itemView.findViewById(R.id.Nombre);

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
        //Meto los datos de alumno al selector
        Tatuador tatuador = listatatuadores.get(position);
        holder.NombreArt.setText(tatuador.getNombreArt());
    }

    @Override
    public int getItemCount() {
        return listatatuadores.size();
    }

}
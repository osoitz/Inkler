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

public class AdaptadorTatuadorWeb extends RecyclerView.Adapter<AdaptadorTatuadorWeb.ViewHolder> {
    private LayoutInflater inflador;
    private List<Web> listatatuadoresweb;

    //Constructor
    AdaptadorTatuadorWeb(Context contexto, List<Web> listatatuador){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listatatuadoresweb = listatatuador;
    }


    //Clase ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView Web;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Web = itemView.findViewById(R.id.WebTat);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = inflador.inflate(R.layout.contenido_recycler_tatuador_web,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Meto los datos de alumno al selector
        Web web = listatatuadoresweb.get(position);
        holder.Web.setText(web.getURL());
    }

    @Override
    public int getItemCount() {
        return listatatuadoresweb.size();
    }

}
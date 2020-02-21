package com.example.inkler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.provider.FontsContract.Columns.RESULT_CODE;

public class AdaptadorGaleria extends RecyclerView.Adapter<AdaptadorGaleria.ViewHolder> {
    private LayoutInflater inflador;
    protected List<Galeria> listalibro;
    private Context contexto;


    public AdaptadorGaleria(Context contexto, List<Galeria> listalibro){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listalibro = listalibro;
        this.contexto=contexto;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView tatuaje;
        //public  TextView tatuaje;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tatuaje = itemView.findViewById(R.id.tatuaje);


        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.galeria_selector,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Meto los datos de libro al selector
        Galeria galeria = listalibro.get(position);
        try{
            Log.d("tag", "onBindViewHolder: "+ galeria.tatuaje);
            holder.tatuaje.setImageResource(Integer.parseInt(galeria.tatuaje));
            holder.tatuaje.setTag(Integer.parseInt(galeria.tatuaje));
        } catch (Exception e) {
            Log.d("tag", "onBindViewHolder: "+ galeria.tatuaje);
            holder.tatuaje.setImageURI(Uri.parse(galeria.tatuaje));
            holder.tatuaje.setTag(Uri.parse(galeria.tatuaje));
        }

        //holder.tatuaje.setText(galeria.nombre);
    }

    @Override
    public int getItemCount() {
        return listalibro.size();
    }

}
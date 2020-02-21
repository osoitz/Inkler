package com.example.inkler;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorGaleria extends RecyclerView.Adapter<AdaptadorGaleria.ViewHolder> {
    private LayoutInflater inflador;
    protected ArrayList<Bitmap> fotos;
    private Context contexto;


    public AdaptadorGaleria(Context contexto, ArrayList<Bitmap> fotos){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fotos = fotos;
        this.contexto = contexto;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView tatuaje;

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
        Bitmap foto = fotos.get(position);
        try{
            //Log.d("tag", "onBindViewHolder: "+ galeria.tatuaje);
            holder.tatuaje.setImageBitmap(foto);
            holder.tatuaje.setTag(position);
        } catch (Exception e) {
            //Log.d("tag", "onBindViewHolder: "+ galeria.tatuaje);
            //holder.tatuaje.setImageURI(Uri.parse(galeria.tatuaje));
            //holder.tatuaje.setTag(Uri.parse(galeria.tatuaje));
        }

        //holder.tatuaje.setText(galeria.nombre);
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

}
package com.example.inkler;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorGaleria extends RecyclerView.Adapter<AdaptadorGaleria.ViewHolder> {
    private final LayoutInflater inflador;
    private final ArrayList<Foto> fotos;
    //private final Context contexto;


    AdaptadorGaleria(Context contexto, ArrayList<Foto> fotos){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fotos = fotos;
        //this.contexto = contexto;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView tatuaje;
        final TextView identificador;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tatuaje = itemView.findViewById(R.id.tatuaje);
            identificador = itemView.findViewById(R.id.identificador);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.galeria_selector, null); //TODO Layout inflation without a parent, avoid passing null
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Meto los datos al selector
        Foto foto = fotos.get(position);
        Bitmap bitmap = foto.getBmp();
        long idFoto = foto.getIdFoto();
        System.out.println(idFoto);
        try{
            //Log.d("tag", "onBindViewHolder: "+ galeria.tatuaje);
            holder.tatuaje.setImageBitmap(bitmap);
            holder.tatuaje.setTag(position);
            holder.identificador.setText(Long.toString(idFoto));

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
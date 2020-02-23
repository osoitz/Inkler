package com.example.inkler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
    private List<Web> webs;

    //Constructor
    AdaptadorTatuadorWeb(Context contexto, List<Web> webs){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.webs = webs;
    }


    //Clase ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView webTatuador;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            webTatuador = itemView.findViewById(R.id.webTatuador);
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
        Web web = webs.get(position);
        String url = web.getUrl();
        String host = App.extraerHost(url);
        SpannableString mitextoU = new SpannableString(host);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        holder.webTatuador.setText(mitextoU);
    }

    @Override
    public int getItemCount() {
        return webs.size();
    }

}
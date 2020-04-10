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

// Clase adaptador para el recyclerview de las webs

public class AdaptadorWeb extends RecyclerView.Adapter<AdaptadorWeb.ViewHolder> {
    private final LayoutInflater inflador;
    private final List<Web> webs;

    //Constructor
    AdaptadorWeb(Context contexto, List<Web> webs){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.webs = webs;
    }


    //Clase ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView nombreWeb;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreWeb = itemView.findViewById(R.id.nombreWeb);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = inflador.inflate(R.layout.contenido_recycler_web,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Meto los datos de la web al selector
        Web web = webs.get(position);
        String url = web.getUrl();
        String host = App.extraerHost(url);
        SpannableString nombreDecorado = new SpannableString(host);
        nombreDecorado.setSpan(new UnderlineSpan(), 0, nombreDecorado.length(), 0);
        holder.nombreWeb.setText(nombreDecorado);
    }

    @Override
    public int getItemCount() {
        return webs.size();
    }

}
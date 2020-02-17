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

public class AdaptadorAlumnos extends RecyclerView.Adapter<AdaptadorAlumnos.ViewHolder> {
    private LayoutInflater inflador;
    private List<Alumno> listaalumnos;

    //Constructor
    AdaptadorAlumnos(Context contexto, List<Alumno> listaalumno){
        inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaalumnos = listaalumno;
    }


    //Clase ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView DNI;
        TextView Nombre;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            DNI = itemView.findViewById(R.id.DNI);
            Nombre = itemView.findViewById(R.id.Nombre);

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = inflador.inflate(R.layout.contenido_recycler_trabajador,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Meto los datos de alumno al selector
        Alumno alumno = listaalumnos.get(position);
        holder.DNI.setText(alumno.getDni());
        holder.Nombre.setText(alumno.getNombre());
    }

    @Override
    public int getItemCount() {
        return listaalumnos.size();
    }

}
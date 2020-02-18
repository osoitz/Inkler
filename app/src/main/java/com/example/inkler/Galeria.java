package com.example.inkler;

import java.util.ArrayList;
import java.util.List;

public class Galeria {
    public int tatuaje;
    public String nombre;

    public Galeria(int tatuaje, String nombre){
        this.tatuaje = tatuaje;
        this.nombre = nombre;
    }

    public int gettatuaje() {
        return tatuaje;
    }

    public void settatuaje(int tatuaje) {
        this.tatuaje = tatuaje;
    }

    public String getnombre() {
        return nombre;
    }

    public void setnombre(String nombre) {
        this.nombre = nombre;
    }




    static List<Galeria> galeriaList = new ArrayList<Galeria>();

    public static List<Galeria> getGaleriaList() {
        return galeriaList;
    }



}

package com.example.inkler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Clase Tatuador con las listas de alumnos y los getters y setters.

public class Tatuador {
    private int id;
    private String NombreArt;
    private String Nombre;
    private String Apellidos;
    private int IDEstudio;


    Tatuador(int id,String NombreArt, String Nombre, String Apellidos, int IDEstudio){
        this.id=id;
        this.NombreArt = NombreArt;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.IDEstudio = IDEstudio;
    }

    Tatuador(){}

    String getNombreArt() {
        return NombreArt;
    }

    void setNombreArt(String dni) {
        this.NombreArt = dni;
    }

    int getIDEstudio() {
        return IDEstudio;
    }

    void setIDEstudio(int IDEstudio) {
        this.IDEstudio = IDEstudio;
    }

    String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    String getApellidos() {
        return Apellidos;
    }

    void setApellidos(String apellidos) {
        this.Apellidos = apellidos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Hashcode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tatuador alumno = (Tatuador) o;
        return NombreArt.equals(alumno.NombreArt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NombreArt);
    }
}

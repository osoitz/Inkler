package com.example.inkler;

import java.util.Objects;

//Clase Tatuador con las listas de alumnos y los getters y setters.

public class Tatuador {
    private int id;
    private String nombreArtistico;
    private String nombre;
    private String apellidos;
    private int idEstudio;


    Tatuador(int id, String nombreArtistico, String Nombre, String Apellidos, int idEstudio){
        this.id=id;
        this.nombreArtistico = nombreArtistico;
        this.nombre = Nombre;
        this.apellidos = Apellidos;
        this.idEstudio = idEstudio;
    }

    Tatuador(){}

    String getNombreArtistico() {
        return nombreArtistico;
    }

    void setNombreArtistico(String dni) {
        this.nombreArtistico = dni;
    }

    int getIdEstudio() {
        return idEstudio;
    }

    void setIdEstudio(int idEstudio) {
        this.idEstudio = idEstudio;
    }

    String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    String getApellidos() {
        return apellidos;
    }

    void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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
        Tatuador tatuador = (Tatuador) o;
        return nombreArtistico.equals(tatuador.nombreArtistico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreArtistico);
    }
}

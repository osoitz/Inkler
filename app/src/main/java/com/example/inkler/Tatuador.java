package com.example.inkler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Clase Tatuador con las listas de alumnos y los getters y setters.

public class Tatuador {
    private String id;
    private String NombreArt;
    private String Nombre;
    private String Apellidos;
    private String Email;
    private String Telefono;
    private String IDEstudio;


    Tatuador(String id,String NombreArt, String Nombre, String Apellidos, String Email, String Telefono, String IDEstudio){
        this.id=id;
        this.NombreArt = NombreArt;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Email = Email;
        this.Telefono = Telefono;
        this.IDEstudio = IDEstudio;
    }

    Tatuador(){}

    String getNombreArt() {
        return NombreArt;
    }

    public void setNombreArt(String dni) {
        this.NombreArt = dni;
    }

    String getIDEstudio() {
        return IDEstudio;
    }

    public void setIDEstudio(String IDEstudio) {
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

    public void setApellidos(String apellidos) {
        this.Apellidos = apellidos;
    }

    String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }

    String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private static List<Tatuador> tatuadorList = new ArrayList<>();

    // Getter de la lista de alumnos
    static List<Tatuador> getTatuadorList() {
        return tatuadorList;
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

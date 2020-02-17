package com.example.inkler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Clase Tatuador con las listas de alumnos y los getters y setters.

public class Tatuador {
    private String NombreArt;
    private String Nombre;
    private String Apellidos;
    private String Email;
    private String Telefono;


    Tatuador(String NombreArt, String Nombre, String Apellidos, String Email, String Telefono){
        this.NombreArt = NombreArt;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Email = Email;
        this.Telefono = Telefono;
    }

    String getNombreArt() {
        return NombreArt;
    }

    public void setNombreArt(String dni) {
        this.NombreArt = dni;
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

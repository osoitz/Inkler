package com.example.inkler;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//Clase Alumno con las listas de alumnos y los getters y setters.

public class Alumno {
    private String DNI;
    private String Nombre;
    private String Apellidos;
    private String Curso;


    Alumno(String DNI, String Nombre, String Apellidos, String Curso){
        this.DNI = DNI;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Curso = Curso;
    }

    String getDni() {
        return DNI;
    }

    public void setDni(String dni) {
        this.DNI = dni;
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

    String getCurso() {
        return Curso;
    }

    public void setCurso(String curso) {
        this.Curso = curso;
    }

    private static List<Alumno> alumnoList = new ArrayList<>();

    // Getter de la lista de alumnos
    static List<Alumno> getAlumnoList() {
        // Cada vez que se hace un get de la lista, se pasa a un linkedhashset para y se vuelve a pasar a la lista para que se borren los datos duplicados.
        Set<Alumno> linkedHashSet = new LinkedHashSet<>(alumnoList);
        alumnoList.clear();
        alumnoList.addAll(linkedHashSet);
        return alumnoList;
    }

    private static List<Alumno> alumnoListSQLite = new ArrayList<>();

    static List<Alumno> getAlumnoListSQLite() {
        return alumnoListSQLite;
    }

    private static List<Alumno> alumnoListFirebase = new ArrayList<>();

    static List<Alumno> getAlumnoListFirebase() {
        return alumnoListFirebase;
    }

    //Hashcode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return DNI.equals(alumno.DNI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DNI);
    }
}

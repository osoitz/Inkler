package com.example.inkler;

public class Estudio {
    private int idEstudio;
    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private String email;
    private String telefono;

    public Estudio(int idEstudio, String Nombre, String Direccion, double latitud, double longitud, String Email, String Telefono){
        this.idEstudio = idEstudio;
        this.nombre = Nombre;
        this.direccion = Direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.email = Email;
        this.telefono = Telefono;
    }

    public Estudio() {}

    public int getIdEstudio() {return idEstudio;}

    public void setIdEstudio(int idEstudio) {
        this.idEstudio = idEstudio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getTelefono() {
        return telefono;
    }


    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}

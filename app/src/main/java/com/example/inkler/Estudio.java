package com.example.inkler;

public class Estudio {
    private int ID;
    private String Nombre;
    private String Direccion;
    private double latitud;
    private double longitud;
    private String Email;
    private String Telefono;

    public Estudio(int ID, String Nombre, String Direccion, double latitud, double longitud, String Email, String Telefono){
        this.ID = ID;
        this.Nombre = Nombre;
        this.Direccion = Direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.Email = Email;
        this.Telefono = Telefono;

    }

    public Estudio() {}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
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
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public String getTelefono() {
        return Telefono;
    }


    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

}

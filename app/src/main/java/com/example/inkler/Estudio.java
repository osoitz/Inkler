package com.example.inkler;

public class Estudio {
    int IdEstudio;
    String Nombre;
    String Direccion;
    double lat;
    double longitud;
    String Email;
    String Web;

    public Estudio(int IdEstudio, String Nombre, String Direccion, double lat, double longitud, String Email,String Web){
        this.IdEstudio=IdEstudio;
        this.Nombre=Nombre;
        this.Direccion=Direccion;
        this.lat=lat;
        this.longitud=longitud;
        this.Email=Email;
        this.Web=Web;
    }

    public int getIdEstudio() {
        return IdEstudio;
    }

    public void setIdEstudio(int idEstudio) {
        IdEstudio = idEstudio;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
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

    public String getWeb() {
        return Web;
    }

    public void setWeb(String web) {
        Web = web;
    }

    

}

package com.example.inkler;

class Estudio {
    private int idEstudio;
    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private String email;
    private String telefono;

    /*
    Estudio(int idEstudio, String Nombre, String Direccion, double latitud, double longitud, String Email, String Telefono){
        this.idEstudio = idEstudio;
        this.nombre = Nombre;
        this.direccion = Direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.email = Email;
        this.telefono = Telefono;
    }
*/
    Estudio() {}

    int getIdEstudio() {return idEstudio;}

    void setIdEstudio(int idEstudio) {
        this.idEstudio = idEstudio;
    }

    String getNombre() {
        return nombre;
    }

    void setNombre(String nombre) {
        this.nombre = nombre;
    }

    String getDireccion() {
        return direccion;
    }

    void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    double getLatitud() {
        return latitud;
    }

    void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    double getLongitud() {
        return longitud;
    }

    void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }


    String getTelefono() {
        return telefono;
    }


    void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}

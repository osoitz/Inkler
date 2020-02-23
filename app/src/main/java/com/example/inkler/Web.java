package com.example.inkler;

public class Web {
    String url;
    int idTatuador;
    int idEstudio;

    public Web (String url, int IdTatuador, int IdEstudio){
        this.url = url;
        this.idEstudio =IdEstudio;
        this.idTatuador =IdTatuador;
    }
    public Web(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdTatuador() {
        return idTatuador;
    }

    public void setIdTatuador(int idTatuador) {
        this.idTatuador = idTatuador;
    }

    public int getIdEstudio() {
        return idEstudio;
    }

    public void setIdEstudio(int idEstudio) {
        this.idEstudio = idEstudio;
    }

}

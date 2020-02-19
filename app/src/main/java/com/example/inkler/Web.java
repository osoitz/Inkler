package com.example.inkler;

public class Web {
    String URL;
    int IdTatuador;
    int IdEstudio;

    public Web (String URL, int IdTatuador, int IdEstudio){
        this.URL=URL;
        this.IdEstudio=IdEstudio;
        this.IdTatuador=IdTatuador;
    }
    public Web(){}

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getIdTatuador() {
        return IdTatuador;
    }

    public void setIdTatuador(int idTatuador) {
        IdTatuador = idTatuador;
    }

    public int getIdEstudio() {
        return IdEstudio;
    }

    public void setIdEstudio(int idEstudio) {
        IdEstudio = idEstudio;
    }
}

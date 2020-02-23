package com.example.inkler;

class Web {
    private String url;
    private int idTatuador;
    private int idEstudio;

    public Web (String url, int IdTatuador, int IdEstudio){
        this.url = url;
        this.idEstudio =IdEstudio;
        this.idTatuador =IdTatuador;
    }
    Web(){}

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public int getIdTatuador() {
        return idTatuador;
    }

    void setIdTatuador(int idTatuador) {
        this.idTatuador = idTatuador;
    }

    public int getIdEstudio() {
        return idEstudio;
    }

    void setIdEstudio(int idEstudio) {
        this.idEstudio = idEstudio;
    }

}

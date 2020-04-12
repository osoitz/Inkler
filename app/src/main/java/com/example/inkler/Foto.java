package com.example.inkler;

import android.graphics.Bitmap;

public class Foto {

    //public String hash;
    private long idFoto;
    private Bitmap bmp;

    public Foto(long idFoto, Bitmap bmp) {
        this.idFoto = idFoto;
        this.bmp = bmp;
    }

    public long getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(long idFoto) {
        this.idFoto = idFoto;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }





}

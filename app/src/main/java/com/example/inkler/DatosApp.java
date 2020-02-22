package com.example.inkler;

import android.app.Application;

public class DatosApp extends Application {

    private static boolean admin;
    private static int idTatuador;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
    }

    public static boolean isAdmin() {
        return admin;
    }

    public static void setAdmin(boolean admin) {
        DatosApp.admin = admin;
    }

    public static int getIdTatuador() {
        return idTatuador;
    }

    public static void setIdTatuador(int idTatuador) {
        DatosApp.idTatuador = idTatuador;
    }
}

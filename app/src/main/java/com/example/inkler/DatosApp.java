package com.example.inkler;

import android.app.Application;

public class DatosApp extends Application {

    private static boolean admin;
    private static String idTat;
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

    public static String getIdTat() {
        return idTat;
    }

    public static void setIdTat(String idTat) {
        DatosApp.idTat = idTat;
    }
}

package com.example.inkler;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.net.URL;

public class App extends Application {

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
        App.admin = admin;
    }

    public static int getIdTatuador() {
        return idTatuador;
    }

    public static void setIdTatuador(int idTatuador) {
        App.idTatuador = idTatuador;
    }

    //Metodos comunes
    /*
    public static String crearContenidoHTML(ArrayList<String> urls){
        String contenidoCampo ="";
        for (String urlString : urls){
            String host = extraerHost(urlString);
            contenidoCampo = contenidoCampo + "<a href='" + urlString + "'>"+ host +"</a><br>";
        }
        //System.out.println(contenidoCampo);
        return contenidoCampo;
    }
*/


    public static String extraerHost(String urlString){
        String host = urlString;
        try {
            URL miUrl = new URL(urlString);
            host = miUrl.getHost();
        }
        catch (Exception e) {
            //Nada de nada
        }
        return host;
    }

    //Imagenes
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}

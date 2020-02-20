package com.example.inkler;

import android.text.Html;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MetodosComunes {


    public String crearContenidoHTML(ArrayList<String> urls){
        String contenidoCampo ="";
        for (String urlString : urls){
            String host = urlString;
            try {
                URL miUrl = new URL(urlString);
                host = miUrl.getHost();
            }
            catch (Exception e) {
                //Nada de nada
            }

            contenidoCampo = contenidoCampo + "<a href='" + urlString + "'>"+ host +"</a><br>";
        }
        //System.out.println(contenidoCampo);
        return contenidoCampo;
    }



}

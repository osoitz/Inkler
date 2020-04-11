package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBlocal db = new DBlocal(getApplicationContext());

        InputStream inputStream = getResources().openRawResource(R.raw.datosejemplo);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String eachline = null;
        String cadenaJSON = "";
        try {
            eachline = bufferedReader.readLine();
            while (eachline != null) {
                // `the words in the file are separated by space`, so to get each words
                //String[] words = eachline.split(" ");
                cadenaJSON += eachline;
                eachline = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("CADENA JSON");
        //System.out.println(cadenaJSON);

        try {
            JSONObject miJSON = new JSONObject(cadenaJSON);
            JSONArray estudios = miJSON.getJSONArray("estudios");

            for(int i=0;i<estudios.length();i++)
            {
                JSONObject estudio = estudios.getJSONObject(i);
                String enombre;
                String edireccion;
                double glat = 0;
                double glong = 0;
                String eemail;
                String etelefono;

                enombre = estudio.getString("nombre");
                edireccion = estudio.getString("direccion");
                JSONArray geo = estudio.getJSONArray("geo");
                for(int j = 0; j < geo.length(); j++) {
                    JSONObject egeo = geo.getJSONObject(j);
                    glat = egeo.getDouble("latitud");
                    glong = egeo.getDouble("longitud");
                }
                eemail = estudio.getString("email");
                etelefono = estudio.getString("telefono");

                Log.d("JSON", "NOMBRE: " + enombre);
                Log.d("JSON", "DIRECCION: " + edireccion);
                Log.d("JSON", "LATITUD: " + glat);
                Log.d("JSON", "LONGITUD: " + glong);
                Log.d("JSON", "EMAIL: " + eemail);
                Log.d("JSON", "TELEFONO: " + etelefono);

                //db.insertarEstudio(enombre, edireccion, egeo, , eemail, etelefono);

                JSONArray tatuadores = estudio.getJSONArray("tatuadores");

                for(int j = 0; j < tatuadores.length(); j++) {
                    JSONObject tatuador = tatuadores.getJSONObject(j);
                    Log.d("JSON", "NOMBRE ARTISTICO: " + tatuador.getString("nombreArtistico"));
                    Log.d("JSON", "NOMBRE: " + tatuador.getString("nombre"));
                    Log.d("JSON", "APELLIDOS: " + tatuador.getString("apellidos"));

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }





        Gson gson = new Gson();
        String jsonArray = "{'id':25,'nombreArtistico':'AKA','nombre':'Name','apellidos':'Surname','idEstudio':1}";
        Tatuador t = gson.fromJson(jsonArray, Tatuador.class);
        Log.d("AAA", "Nombre: " + t.getNombre() + " Apellidos: " + t.getApellidos() + " ID: " + t.getId() + " Nombre Artistico: " + t.getNombreArtistico() + " ID Estudio: " + t.getIdEstudio());

        boolean llena = db.checkEmpty();
        if (llena){
            Log.d("DBCHECK", "LA BASE DE DATOS TIENE INFORMACION");
        } else {
            Log.d("DBCHECK", "LA BASE DE DATOS ESTA VACIA, RELLENANDO...");
            db.rellenarDB();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(MainActivity.this, ActivityListaTatuadores.class);
                startActivity(intent);
            }
        }, 2000);   //2 seconds

    }



}

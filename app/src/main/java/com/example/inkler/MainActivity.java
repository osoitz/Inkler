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

        boolean llena = db.checkEmpty();
        if (llena){
            Log.d("DBCHECK", "LA BASE DE DATOS TIENE INFORMACION");
        } else {
            Log.d("DBCHECK", "LA BASE DE DATOS ESTA VACIA, RELLENANDO...");
            //db.rellenarDB();
            rellenarDB();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(MainActivity.this, ActivityListaTatuadores.class);
                startActivity(intent);
            }
        }, 2000);   //2 seconds

    }

    void rellenarDB(){
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
                int idestudio;

                //Recoger datos del estudio del JSON
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

                //Insertar y recoger el ID del estudio
                db.insertarEstudio(enombre, edireccion, glat, glong, eemail, etelefono);
                idestudio = db.recogerIdEstudio(enombre);
                Log.d("JSON", "ID ESTUDIO: " + idestudio);

                JSONArray websestudio = estudio.getJSONArray("webs");
                for(int j = 0; j < websestudio.length(); j++) {
                    //Recoger datos de la web
                    JSONObject web = websestudio.getJSONObject(j);
                    String w = web.getString("web");
                    Log.d("JSON", "WEB ESTUDIO: " + w);
                    Web oWeb = new Web();
                    oWeb.setIdEstudio(idestudio);
                    oWeb.setUrl(w);
                    db.insertarWeb(oWeb.getIdEstudio(), oWeb.getUrl(), oWeb.getIdTatuador());
                }

                JSONArray tatuadores = estudio.getJSONArray("tatuadores");
                for(int j = 0; j < tatuadores.length(); j++) {
                    //Recoger datos del tatuador
                    JSONObject tatuador = tatuadores.getJSONObject(j);
                    String tnombreart = tatuador.getString("nombreArtistico");
                    String tnombre = tatuador.getString("nombre");
                    String tapellidos = tatuador.getString("apellidos");
                    Log.d("JSON", "NOMBRE ARTISTICO: " + tnombreart);
                    Log.d("JSON", "NOMBRE: " + tnombre);
                    Log.d("JSON", "APELLIDOS: " + tapellidos);

                    //Insertar tatuador y recoger id
                    db.insertarTatuador(tnombre, tapellidos, tnombreart, idestudio);
                    int idTatuador = db.recogerIdTatuador(tnombre);
                    Log.d("JSON", "ID TATUADOR: " + idTatuador);

                    JSONArray webstatuador = tatuador.getJSONArray("webs");
                    for(int p = 0; p < websestudio.length(); p++) {
                        //Recoger datos de la web
                        JSONObject web = webstatuador.getJSONObject(p);
                        String w = web.getString("web");
                        Log.d("JSON", "WEB TATUADOR: " + w);
                        Web oWeb = new Web();
                        oWeb.setIdTatuador(idTatuador);
                        oWeb.setUrl(w);
                        db.insertarWeb(oWeb.getIdEstudio(), oWeb.getUrl(), oWeb.getIdTatuador());
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}

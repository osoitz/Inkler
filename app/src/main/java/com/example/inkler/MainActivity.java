package com.example.inkler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
                Estudio nuevoEstudio = new Estudio();

                //Recoger datos del estudio del JSON
                nuevoEstudio.setNombre(estudio.getString("nombre"));
                nuevoEstudio.setDireccion(estudio.getString("direccion"));
                JSONArray geo = estudio.getJSONArray("geo");
                for(int j = 0; j < geo.length(); j++) {
                    JSONObject egeo = geo.getJSONObject(j);
                    nuevoEstudio.setLatitud(egeo.getDouble("latitud"));
                    nuevoEstudio.setLongitud(egeo.getDouble("longitud"));
                }
                nuevoEstudio.setEmail(estudio.getString("email"));
                nuevoEstudio.setTelefono(estudio.getString("telefono"));

                Log.d("JSON", "NOMBRE: " + nuevoEstudio.getNombre());
                Log.d("JSON", "DIRECCION: " + nuevoEstudio.getDireccion());
                Log.d("JSON", "LATITUD: " + nuevoEstudio.getLatitud());
                Log.d("JSON", "LONGITUD: " + nuevoEstudio.getLongitud());
                Log.d("JSON", "EMAIL: " + nuevoEstudio.getEmail());
                Log.d("JSON", "TELEFONO: " + nuevoEstudio.getTelefono());

                //Insertar el estudio y recoger su ID
                int idEstudio = (int) db.insertarEstudio(nuevoEstudio);

                Log.d("JSON", "ID ESTUDIO: " + idEstudio);

                JSONArray websestudio = estudio.getJSONArray("webs");
                for(int j = 0; j < websestudio.length(); j++) {
                    //Recoger datos de la web
                    JSONObject web = websestudio.getJSONObject(j);
                    String w = web.getString("web");
                    Log.d("JSON", "WEB ESTUDIO: " + w);
                    Web oWeb = new Web();
                    oWeb.setIdEstudio(idEstudio);
                    oWeb.setUrl(w);
                    db.insertarWeb(oWeb);
                }

                JSONArray tatuadores = estudio.getJSONArray("tatuadores");
                for(int j = 0; j < tatuadores.length(); j++) {
                    //Recoger datos del tatuador
                    JSONObject tatuador = tatuadores.getJSONObject(j);
                    Tatuador nuevoTatuador = new Tatuador();
                    nuevoTatuador.setNombreArtistico(tatuador.getString("nombreArtistico"));
                    nuevoTatuador.setNombre(tatuador.getString("nombre"));
                    nuevoTatuador.setApellidos(tatuador.getString("apellidos"));
                    nuevoTatuador.setIdEstudio(idEstudio);

                    Log.d("JSON", "NOMBRE ARTISTICO: " + nuevoTatuador.getNombreArtistico());
                    Log.d("JSON", "NOMBRE: " + nuevoTatuador.getNombre());
                    Log.d("JSON", "APELLIDOS: " + nuevoTatuador.getApellidos());

                    //Insertar tatuador y recoger id
                    long idTatuador = db.insertarTatuador(nuevoTatuador);
                    //int idTatuador = db.recogerIdTatuador(tnombre);
                    Log.d("JSON", "ID TATUADOR: " + idTatuador);

                    JSONArray webstatuador = tatuador.getJSONArray("webs");
                    for(int p = 0; p < webstatuador.length(); p++) {
                        //Recoger datos de la web
                        JSONObject web = webstatuador.getJSONObject(p);
                        String w = web.getString("web");
                        Log.d("JSON", "WEB TATUADOR: " + w);
                        Web oWeb = new Web();
                        oWeb.setIdTatuador((int)idTatuador);
                        oWeb.setUrl(w);
                        db.insertarWeb(oWeb);
                    }

                    JSONArray fotosTatuador = tatuador.getJSONArray("fotos");
                    for(int q = 0; q < fotosTatuador.length(); q++) {
                        //Recoger datos de la web
                        JSONObject foto = fotosTatuador.getJSONObject(q);
                        String nombreFoto = foto.getString("foto");
                        Log.d("JSON", "Foto: " + nombreFoto);
                        InputStream imageStream = getResources().openRawResource(getResources().getIdentifier(nombreFoto,"raw", getPackageName()));
                        //InputStream imageStream = this.getResources().openRawResource(R.raw.nombreFoto);
                        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                        long rowid = db.insertarFoto(App.getBytes(bitmap), (int)idTatuador);
                        Log.d("JSON", "Foto: " + nombreFoto + " Id: "+ rowid);

                    }


                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}

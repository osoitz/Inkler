package com.example.inkler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.BaseColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DBlocal   {

    private DBHelper dbHelper;
    private SQLiteDatabase db;


    public DBlocal(Context context){
        //Local BD
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    
    
    public Tatuador recogerTatuador (String id){
        Tatuador tatuador = new Tatuador();
        // Iniciar base de datos

        //Columnas
        String[] projection = {
                DBHelper.entidadTatuador._ID,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE,
                DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS,
                DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO
        };

        //Respuesta
        String[] selectionArgs = new String[] { "" + id } ;
        Cursor cursor = db.query(
                DBHelper.entidadTatuador.TABLE_NAME,
                projection,
                " _ID = ? ",
                selectionArgs,
                null,
                null,
                null);
        // recoger los datos
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            tatuador.setId(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID)));
            tatuador.setNombreArt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO)));
            tatuador.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE)));
            tatuador.setApellidos(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS)));
            tatuador.setIDEstudio(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO)));

        }
        cursor.close();
        //dbHelper.close();
        return tatuador;
    }

    public Estudio recogerEstudio (String id) {
        Estudio estudio = new Estudio();


        //Columnas
        String[] projection = {
                DBHelper.entidadEstudio._ID,
                DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE,
                DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION,
                DBHelper.entidadEstudio.COLUMN_NAME_LATITUD,
                DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD,
                DBHelper.entidadEstudio.COLUMN_NAME_EMAIL,
                DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO,
        };

        //Respuesta
        String[] selectionArgs = new String[] { "" + id } ;
        Cursor cursor = db.query(
                DBHelper.entidadEstudio.TABLE_NAME,
                projection,
                " _ID = ? ",
                selectionArgs,
                null,
                null,
                null);
        // recoger los datos
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            estudio.setID(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio._ID))));
            estudio.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE)));
            estudio.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION)));
            estudio.setLatitud(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD))));
            estudio.setLongitud(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD))));
            estudio.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL)));
            estudio.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO)));
        }
        cursor.close();
        //dbHelper.close();
        return estudio;
    }

    public ArrayList<String> recogerWebsTatuador (String id) {
        ArrayList<String> websTatuador = new ArrayList<>();

        //Columnas
        String[] projection = {
                DBHelper.entidadWeb.COLUMN_NAME_URL
        };

        String selection = DBHelper.entidadWeb.COLUMN_NAME_ID_TATUADOR + " = ?";
        String[] selectionArgs = new String[] { "" + id } ;
        //Respuesta

        Cursor cursor = db.query(
                DBHelper.entidadWeb.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        ;
        while(cursor.moveToNext()) {
            String url = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBHelper.entidadWeb.COLUMN_NAME_URL));
            websTatuador.add(url);
        }
        cursor.close();
        //dbHelper.close();
        return websTatuador;
    }

    public ArrayList<String> recogerWebsEstudio (String id) {
        ArrayList<String> webs = new ArrayList<>();


        //Columnas
        String[] projection = {
                DBHelper.entidadWeb.COLUMN_NAME_URL
        };

        String selection = DBHelper.entidadWeb.COLUMN_NAME_ID_ESTUDIO + " = ?";
        String[] selectionArgs = new String[] { "" + id } ;
        //Respuesta

        Cursor cursor = db.query(
                DBHelper.entidadWeb.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        ;
        while(cursor.moveToNext()) {
            String url = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBHelper.entidadWeb.COLUMN_NAME_URL));
            webs.add(url);
        }
        cursor.close();
        //dbHelper.close();
        return webs;
    }

    public ArrayList<String> recogerFotos (String id){
        ArrayList<String> fotos = new ArrayList<>();

        // Definimos la query
        String[] projection = {
                BaseColumns._ID,
                DBHelper.entidadFoto.COLUMN_NAME_FOTO,
                DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR
        };

        // Se filtra el resultado dependiendo de idTat
        String selection =  DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR + " = ?";
        String[] selectionArgs = { id };

        // Ordenamos la query
        String sortOrder = null;

        Cursor galeriaSQLite = db.query(
                DBHelper.entidadFoto.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        Galeria.getGaleriaList().clear();

        while (galeriaSQLite.moveToNext()){

            String tatuaje = galeriaSQLite.getString(galeriaSQLite.getColumnIndexOrThrow(DBHelper.entidadFoto.COLUMN_NAME_FOTO));
            String nombre = galeriaSQLite.getString(galeriaSQLite.getColumnIndexOrThrow(DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR));
            //guardamos los datos de sqlite en guardarsqlite y los pasamos a la clase Alumno
            Galeria BDSQLite = new Galeria(tatuaje, nombre);
            Galeria.getGaleriaList().add(BDSQLite);
        }
        galeriaSQLite.close();
        return fotos;
    }
    public void insertarFoto (Bitmap bitmap, String id) {
        // Create a new map of values, where column names are the keys
   /*    ContentValues values = new ContentValues();
        values.put(DBHelper.entidadFoto.COLUMN_NAME_FOTO, bitmap);
        values.put(DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR, id);

        // Insert the new row, returning the primary key value of the new row
        db.insert(DBHelper.entidadFoto.TABLE_NAME, null, values);
*/

        //Con este metodo guardaremos la foto tanto en la base de datos como en la memoria interna
        //del telefono
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR,id);
        //System.out.println(sUsuario);
        long newRowId = db.insert(DBHelper.entidadFoto.TABLE_NAME, null, values);
        String IDfoto = String.valueOf(newRowId);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        String fname = IDfoto +".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

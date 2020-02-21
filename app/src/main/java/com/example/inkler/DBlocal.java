package com.example.inkler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.List;

public class DBlocal   {

    private DBHelper dbHelper;
    private SQLiteDatabase db;


    public DBlocal(Context context){
        //Local BD
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void cargarTatuadores (){
        //Columnas
        String[] proyeccion = {DBHelper.entidadTatuador._ID,DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO};
        //Respuesta
        Cursor cursor = db.query(DBHelper.entidadTatuador.TABLE_NAME, proyeccion, null, null, null, null, null);
        // recoger los datos
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID));
            String nombreArt = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS));
            String IDEstudio = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            Tatuador t = new Tatuador(id,nombreArt, nombre, apellidos, IDEstudio);
            Tatuador.getTatuadorList().add(t);
        }
        cursor.close();
    }
    public void cargarTatuadoresFiltrado (String idEstudio){
        //Columnas
        String[] proyeccion = {
                DBHelper.entidadTatuador._ID,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE,
                DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS,
                DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO};



        //Respuesta
        String selection = DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO + " = ?";
        String[] selectionArgs = new String[] { "" + idEstudio } ;

        //Respuesta
        Cursor cursor = db.query(
                DBHelper.entidadTatuador.TABLE_NAME,
                proyeccion,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        // recoger los datos
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID));
            String nombreArt = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS));
            String IDEstudio = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            Tatuador t = new Tatuador(id,nombreArt, nombre, apellidos, IDEstudio);
            Tatuador.getTatuadorList().add(t);
        }
        cursor.close();
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

    public ArrayList<Estudio> recogerEstudios () {
        ArrayList<Estudio> estudios = new ArrayList<>();
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
        //String[] selectionArgs = new String[] { "" + id } ;

        Cursor cursor = db.query(
                DBHelper.entidadEstudio.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        // recoger los datos
        while(cursor.moveToNext()) {
            Estudio estudio = new Estudio();
            estudio.setID(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio._ID))));
            estudio.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE)));
            estudio.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION)));
            estudio.setLatitud(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD))));
            estudio.setLongitud(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD))));
            estudio.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL)));
            estudio.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO)));
            estudios.add(estudio);
        }

        cursor.close();
        return estudios;
    }

    public List<Web> recogerWebsTatuador (String id) {
        List<Web> webs = new ArrayList<>();
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

        while(cursor.moveToNext()) {
            Web web = new Web();
            web.setURL(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadWeb.COLUMN_NAME_URL)));
            Log.i("TAG", "WEB TATUADOR: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadWeb.COLUMN_NAME_URL)));
            webs.add(web);
        }
        cursor.close();
        //dbHelper.close();
        return webs;
    }

    public List<Web> recogerWebsEstudio (String id) {
        List<Web> webs = new ArrayList<>();


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

        while(cursor.moveToNext()) {
            Web web = new Web();
            web.setURL(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadWeb.COLUMN_NAME_URL)));
            Log.i("TAG", "WEB ESTUDIO: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadWeb.COLUMN_NAME_URL)));
            webs.add(web);
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
            //guardamos los datos de sqlite en guardar sqlite y los pasamos a la clase Alumno
            //Log.d("tag", "recogerFotos: "+ tatuaje);
            Galeria BDSQLite = new Galeria(tatuaje, nombre);
            Galeria.getGaleriaList().add(BDSQLite);
        }
        galeriaSQLite.close();
        return fotos;
    }
    public void insertarFoto (Bitmap bitmap, String id) {

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

    public int RecogerIdEstudio (String nombreEstudio){
        int idEstudio=0;

        //Columnas
        String[] proyeccion = {DBHelper.entidadEstudio._ID};
        String selection = DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE + " = ?";
        String[] selectionArgs = new String[] { "" + nombreEstudio } ;
        //Respuesta
        Cursor cursor = db.query(
                DBHelper.entidadEstudio.TABLE_NAME,
                proyeccion,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();

            idEstudio= cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio._ID));
        }
        cursor.close();



        return idEstudio;
    }

    public void insertarTatuador(String st_nombre,String st_apellidos, String st_nombreArtistico, int IdEstudio){
        ContentValues e1 = new ContentValues();
        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, st_nombre);
        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, st_apellidos);
        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, st_nombreArtistico);
        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO, IdEstudio);
        db.insert(DBHelper.entidadTatuador.TABLE_NAME, null, e1);
    }

    public void modificarTatuador(String id, String st_nombre, String st_apellidos, String st_nombreArtistico, int IdEstudio){
        ContentValues e1 = new ContentValues();
        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, st_nombre);
        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, st_apellidos);
        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, st_nombreArtistico);
        e1.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO, IdEstudio);
        //Columnas del where
        String selection = DBHelper.entidadTatuador._ID + " = ?";
        //Argumentos del where
        String [] selectionargs = {id};
        db.update(DBHelper.entidadTatuador.TABLE_NAME,e1,selection,selectionargs);
    }

    public void insertarEstudio (String nombre, String direccion, double latitud, double longitud, String email, String Telefono) {
        ContentValues e1 = new ContentValues();
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE, nombre);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION, direccion);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL, email);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO, Telefono);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD, longitud);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD, latitud);
        db.insert(DBHelper.entidadEstudio.TABLE_NAME, null, e1);
    }

    public void modificarEstudio (String id, String nombre, String direccion, double latitud, double longitud, String email, String Telefono) {

        ContentValues e1 = new ContentValues();
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE, nombre);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION, direccion);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL, email);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO, Telefono);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD, longitud);
        e1.put(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD, latitud);
        //Columnas del where
        String selection = DBHelper.entidadEstudio._ID + " = ?";
        //Argumentos del where
        String [] selectionargs = {id};
        db.update(DBHelper.entidadEstudio.TABLE_NAME,e1,selection,selectionargs);

    }



}

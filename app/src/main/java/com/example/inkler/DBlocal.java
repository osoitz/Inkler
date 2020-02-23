package com.example.inkler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBlocal   {

    //private DBHelper dbHelper;
    //private SQLiteDatabase db;
    private Context context;

    public DBlocal(Context context){
        //Local BD
        this.context = context;
    }

    public SQLiteDatabase abrirDB(){
        DBHelper dbHelper = new DBHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db;
    }

    public List<Tatuador> recogerTatuadores(){
        SQLiteDatabase db = abrirDB();
        List<Tatuador> tatuadores = new ArrayList<>();

        //Columnas
        String[] proyeccion = {
                DBHelper.entidadTatuador._ID,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO,
                DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE,
                DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS,
                DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO};

        //Respuesta
        Cursor cursor = db.query(DBHelper.entidadTatuador.TABLE_NAME, proyeccion, null, null, null, null, null);

        // recoger los datos
        while (cursor.moveToNext()) {
            int idTatuador = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID));
            String nombreArt = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS));
            int idEstudio = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            Tatuador tatuador = new Tatuador(idTatuador, nombreArt, nombre, apellidos, idEstudio);
            tatuadores.add(tatuador);
            //Tatuador.getTatuadorList().add(t);
        }
        cursor.close();
        db.close();
        return tatuadores;
    }

    public List<Tatuador> recogerTatuadoresEstudio(int idEstudio){
        SQLiteDatabase db = abrirDB();
        List<Tatuador> tatuadores = new ArrayList<>();
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
            int idTatuador = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID));
            String nombreArtistico = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS));
            //int idEstudio = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO));
            Tatuador tatuador = new Tatuador(idTatuador, nombreArtistico, nombre, apellidos, idEstudio);
            tatuadores.add(tatuador);
        }

        cursor.close();
        db.close();
        return tatuadores;
    }

    public Tatuador recogerTatuador (int idTatuador){
        SQLiteDatabase db = abrirDB();
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
        String[] selectionArgs = new String[] { "" + idTatuador } ;

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
            tatuador.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador._ID)));
            tatuador.setNombreArtistico(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO)));
            tatuador.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE)));
            tatuador.setApellidos(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS)));
            tatuador.setIdEstudio(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO)));

        }
        cursor.close();
        db.close();
        return tatuador;
    }


    public Estudio recogerEstudio (int idEstudio) {
        SQLiteDatabase db = abrirDB();
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
        String[] selectionArgs = new String[] { "" + idEstudio } ;
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
            estudio.setIdEstudio(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio._ID)));
            estudio.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE)));
            estudio.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION)));
            estudio.setLatitud(Double.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD))));
            estudio.setLongitud(Double.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD))));
            estudio.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL)));
            estudio.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO)));
        }
        cursor.close();
        db.close();
        return estudio;
    }


    public ArrayList<Estudio> recogerEstudios () {
        SQLiteDatabase db = abrirDB();
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
            estudio.setIdEstudio(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio._ID)));
            estudio.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE)));
            estudio.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION)));
            estudio.setLatitud(Double.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD))));
            estudio.setLongitud(Double.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD))));
            estudio.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL)));
            estudio.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO)));
            estudios.add(estudio);
        }

        cursor.close();
        db.close();
        return estudios;
    }


    public List<Web> recogerWebsTatuador (int id) {
        SQLiteDatabase db = abrirDB();
        //ArrayList<String> websTatuador = new ArrayList<>();
        List<Web> webs= new ArrayList<Web>();

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
            String url = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadWeb.COLUMN_NAME_URL));
            Web web = new Web();
            web.setUrl(url);
            web.setIdTatuador(id);
            webs.add(web);
        }
        cursor.close();
        db.close();
        return webs;
    }

    public List<Web> recogerWebsEstudio (int idEstudio) {
        SQLiteDatabase db = abrirDB();
        //ArrayList<String> webs = new ArrayList<>();
        List<Web> webs= new ArrayList<Web>();


        //Columnas
        String[] projection = {
                DBHelper.entidadWeb.COLUMN_NAME_URL
        };

        String selection = DBHelper.entidadWeb.COLUMN_NAME_ID_ESTUDIO + " = ?";
        String[] selectionArgs = new String[] { "" + idEstudio } ;

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
            String url = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBHelper.entidadWeb.COLUMN_NAME_URL));
            Web web = new Web();
            web.setUrl(url);
            web.setIdEstudio(idEstudio);
            webs.add(web);
        }

        cursor.close();
        db.close();
        return webs;
    }

    public ArrayList<Bitmap> recogerFotosTatuador (int idTatuador){
        SQLiteDatabase db = abrirDB();
        Log.d("HOLA!", "entramos en recogerFotosTatuador! " + idTatuador);
        ArrayList<Bitmap> fotos = new ArrayList<>();

        // Definimos la query
        String[] projection = {
                DBHelper.entidadFoto.COLUMN_NAME_FOTO,
        };

        // Se filtra el resultado dependiendo de idTatuador
        String selection =  DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR + " = ?";
        String[] selectionArgs = new String[] { "" + idTatuador } ;;

        Cursor cursor = db.query(
                DBHelper.entidadFoto.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()){
            Log.d("HOLA!", "Estamos en el bucle de recogerFotosTatuador!");
            byte[] fotoStream = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.entidadFoto.COLUMN_NAME_FOTO));
            Bitmap foto = DBBitmapUtility.getImage(fotoStream);
            fotos.add(foto);
            //System.out.println("Hola");
        }

        cursor.close();
        db.close();
        Log.d("HOLA!", "Nos vamos de recogerFotosTatuador! " + fotos.size());
        return fotos;
    }
    public long insertarFoto (byte[] bitmap, int idTatuador) {
        SQLiteDatabase db = abrirDB();

        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR, idTatuador);
        values.put(DBHelper.entidadFoto.COLUMN_NAME_FOTO,bitmap);

        long newRowId = db.insert(DBHelper.entidadFoto.TABLE_NAME, null, values);

        return newRowId;

    }

    public int recogerIdEstudio(String nombreEstudio){
        SQLiteDatabase db = abrirDB();
        int idEstudio = 0;

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
        if (cursor.getCount() > 0){
            cursor.moveToFirst();

            idEstudio = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio._ID));
        }
        cursor.close();
        db.close();
        return idEstudio;
    }

    public void insertarTatuador(String st_nombre,String st_apellidos, String st_nombreArtistico, int idEstudio){
        SQLiteDatabase db = abrirDB();
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, st_nombre);
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, st_apellidos);
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, st_nombreArtistico);
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO, idEstudio);
        db.insert(DBHelper.entidadTatuador.TABLE_NAME, null, values);
        db.close();
    }

    public void modificarTatuador(int idTatuador, String st_nombre, String st_apellidos, String st_nombreArtistico, int IdEstudio){
        SQLiteDatabase db = abrirDB();
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, st_nombre);
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, st_apellidos);
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, st_nombreArtistico);
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO, IdEstudio);
        //Columnas del where
        String selection = DBHelper.entidadTatuador._ID + " = ?";
        //Argumentos del where
        String [] selectionargs = { "" + idTatuador };
        db.update(DBHelper.entidadTatuador.TABLE_NAME,values,selection,selectionargs);
        db.close();
    }

    public void insertarEstudio (String nombre, String direccion, double latitud, double longitud, String email, String Telefono) {
        SQLiteDatabase db = abrirDB();
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE, nombre);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION, direccion);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL, email);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO, Telefono);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD, longitud);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD, latitud);
        db.insert(DBHelper.entidadEstudio.TABLE_NAME, null, values);
        db.close();
    }

    public void modificarEstudio (int idEstudio, String nombre, String direccion, double latitud, double longitud, String email, String Telefono) {
        SQLiteDatabase db = abrirDB();
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE, nombre);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION, direccion);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL, email);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO, Telefono);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD, longitud);
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD, latitud);
        //Columnas del where
        String selection = DBHelper.entidadEstudio._ID + " = ?";
        //Argumentos del where
        String [] selectionargs = { "" + idEstudio };
        db.update(DBHelper.entidadEstudio.TABLE_NAME,values,selection,selectionargs);
        db.close();
    }

    public void insertarWeb (int idEstudio, String web, int idTatuador) {
        SQLiteDatabase db = abrirDB();
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadWeb.COLUMN_NAME_URL, web);
        values.put(DBHelper.entidadWeb.COLUMN_NAME_ID_ESTUDIO, idEstudio);
        values.put(DBHelper.entidadWeb.COLUMN_NAME_ID_TATUADOR, idTatuador);

        db.insert(DBHelper.entidadWeb.TABLE_NAME, null, values);
        db.close();
    }



}

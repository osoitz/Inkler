package com.example.inkler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class DBlocal   {

    private SQLiteDatabase db;
    //private final Context context;
    private final DBHelper dbHelper;

    DBlocal(Context context){
        this.dbHelper = new DBHelper(context);
    }

    private void abrirDB(boolean escritura){
        if (escritura) {
            this.db = dbHelper.getWritableDatabase();
        }
        else {
            this.db = dbHelper.getReadableDatabase();
        }
    }

    boolean checkEmpty(){
        abrirDB(false);
        int ntatuadores = 0;
        boolean tatuadores = false;
        //Columnas
        String[] proyeccion = {DBHelper.entidadTatuador._ID};
        //Respuesta
        Cursor cursor = db.query(DBHelper.entidadTatuador.TABLE_NAME, proyeccion, null, null, null, null, null);
        // recoger los datos
        while (cursor.moveToNext()) {
            ntatuadores++;
        }
        cursor.close();
        db.close();
        if (ntatuadores > 0){
            tatuadores = true;
        }
        cursor.close();
        db.close();
        return tatuadores;
    }

    List<Tatuador> recogerTatuadores(){
        abrirDB(false);
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

    List<Tatuador> recogerTatuadoresEstudio(int idEstudio){
        abrirDB(false);
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

    Tatuador recogerTatuador (int idTatuador){
        abrirDB(false);
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


    Estudio recogerEstudio (int idEstudio) {
        abrirDB(false);
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
            estudio.setLatitud(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD)));
            estudio.setLongitud(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD)));
            estudio.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL)));
            estudio.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO)));
        }
        cursor.close();
        db.close();
        return estudio;
    }


    ArrayList<Estudio> recogerEstudios () {
        abrirDB(false);
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
            estudio.setLatitud(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD)));
            estudio.setLongitud(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD)));
            estudio.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL)));
            estudio.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO)));
            estudios.add(estudio);
        }

        cursor.close();
        db.close();
        return estudios;
    }

    ArrayList<String> recogerNombresEstudios () {
        abrirDB(false);
        ArrayList<String> nombresEstudios = new ArrayList<>();
        //Columnas
        String[] projection = {
                DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE,
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
            nombresEstudios.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE)));
        }
        cursor.close();
        db.close();
        return nombresEstudios;
    }


    List<Web> recogerWebsTatuador (int id) {
        abrirDB(false);
        //ArrayList<String> websTatuador = new ArrayList<>();
        List<Web> webs= new ArrayList<>();

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

    List<Web> recogerWebsEstudio (int idEstudio) {
        abrirDB(false);
        //ArrayList<String> webs = new ArrayList<>();
        List<Web> webs= new ArrayList<>();

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

    ArrayList<Foto> recogerFotosTatuador (int idTatuador){
        abrirDB(false);
        Log.d("HOLA!", "entramos en recogerFotosTatuador! " + idTatuador);
        ArrayList<Foto> fotos = new ArrayList<>();

        // Definimos la query
        String[] projection = {
                DBHelper.entidadFoto._ID,
                DBHelper.entidadFoto.COLUMN_NAME_FOTO,
        };

        // Se filtra el resultado dependiendo de idTatuador
        String selection =  DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR + " = ?";
        String[] selectionArgs = new String[] { "" + idTatuador } ;

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
            //Log.d("HOLA!", "Estamos en el bucle de recogerFotosTatuador!");
            //Long idFoto = cursor.getLong(cursor.getColumnIndex("_ID"));
            Long idFoto = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.entidadFoto._ID));
            byte[] fotoStream = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.entidadFoto.COLUMN_NAME_FOTO));
            Bitmap bitmap = App.getImage(fotoStream);
            fotos.add(new Foto(idFoto, bitmap));
            //System.out.println("Hola");
        }

        cursor.close();
        db.close();
        Log.d("HOLA!", "Nos vamos de recogerFotosTatuador! " + fotos.size());
        return fotos;
    }

    int recogerIdEstudio(String nombreEstudio){
        abrirDB(false);
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



    long insertarFoto (byte[] bitmap, int idTatuador) {
        abrirDB(true);
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR, idTatuador);
        values.put(DBHelper.entidadFoto.COLUMN_NAME_FOTO,bitmap);

        //Devuelve rowid
        long rowid = db.insert(DBHelper.entidadFoto.TABLE_NAME, null, values);
        db.close();
        return rowid;
    }

    void borrarFoto (String idFoto) {
        abrirDB(true);
        /*
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadFoto.COLUMN_NAME_ID_TATUADOR, idTatuador);
        values.put(DBHelper.entidadFoto.COLUMN_NAME_FOTO,bitmap);

        //Devuelve rowid
        return db.insert(DBHelper.entidadFoto.TABLE_NAME, null, values);
         */

        // Define 'where' part of query.
        String selection = DBHelper.entidadFoto._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { idFoto };
        // Issue SQL statement.
        db.delete(DBHelper.entidadFoto.TABLE_NAME, selection, selectionArgs);

    }

    long insertarTatuador(Tatuador tatuador){
        abrirDB(true);
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, tatuador.getNombre());
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, tatuador.getApellidos());
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, tatuador.getNombreArtistico());
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO, tatuador.getIdEstudio());
        long idTatuador = db.insert(DBHelper.entidadTatuador.TABLE_NAME, null, values);
        db.close();
        return idTatuador;
    }

    void modificarTatuador(Tatuador tatuador){
        abrirDB(true);
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE, tatuador.getNombre());
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_APELLIDOS, tatuador.getApellidos());
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO, tatuador.getNombreArtistico());
        values.put(DBHelper.entidadTatuador.COLUMN_NAME_ID_ESTUDIO, tatuador.getIdEstudio());
        //Columnas del where
        String selection = DBHelper.entidadTatuador._ID + " = ?";
        //Argumentos del where
        String [] selectionargs = { "" + tatuador.getId() };
        db.update(DBHelper.entidadTatuador.TABLE_NAME,values,selection,selectionargs);
        db.close();
    }

    long insertarEstudio (Estudio estudio) {
        abrirDB(true);
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE, estudio.getNombre());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION, estudio.getDireccion());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL, estudio.getEmail());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO, estudio.getTelefono());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD, estudio.getLatitud());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD, estudio.getLongitud());

        long rowid = db.insert(DBHelper.entidadEstudio.TABLE_NAME, null, values);
        db.close();
        return rowid;
    }

    void modificarEstudio (Estudio estudio) {
        abrirDB(true);
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_NOMBRE, estudio.getNombre());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_DIRECCION, estudio.getDireccion());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_EMAIL, estudio.getEmail());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_TELEFONO, estudio.getTelefono());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_LATITUD, estudio.getLatitud());
        values.put(DBHelper.entidadEstudio.COLUMN_NAME_LONGITUD, estudio.getLongitud());

        //Columnas del where
        String selection = DBHelper.entidadEstudio._ID + " = ?";
        //Argumentos del where
        String [] selectionargs = { "" + estudio.getIdEstudio() };
        db.update(DBHelper.entidadEstudio.TABLE_NAME,values,selection,selectionargs);
        db.close();
    }

    long insertarWeb (Web web) {
        abrirDB(true);
        ContentValues values = new ContentValues();
        values.put(DBHelper.entidadWeb.COLUMN_NAME_URL, web.getUrl());
        values.put(DBHelper.entidadWeb.COLUMN_NAME_ID_ESTUDIO, web.getIdEstudio());
        values.put(DBHelper.entidadWeb.COLUMN_NAME_ID_TATUADOR, web.getIdTatuador());
        long rowid = db.insert(DBHelper.entidadWeb.TABLE_NAME, null, values);
        db.close();
        return rowid;
    }

}

package com.example.inkler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


class DBHelper extends SQLiteOpenHelper {

    //si cambiamos el modelo debe cambiar la version de la base de datos, así se ejecutarán los metodos onupgrade u ondowngrade
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Inkler.db";

    //Primero creamos unos string en los que guardar los nombres de tablas, columnas... para centralizar la información por si hay que hacer cambios
    static class entidadTatuador implements BaseColumns{
        static final String TABLE_NAME = "Tatuador";
        static final String COLUMN_NAME_NOMBRE = "Nombre" ;
        static final String COLUMN_NAME_APELLIDOS="Apellidos";
        static final String COLUMN_NAME_NOMBRE_ARTISTICO="Nombre_Artistico";
        static final String COLUMN_NAME_ID_ESTUDIO="ID_Estudio";
    }

    static class entidadEstudio implements BaseColumns{
        static final String TABLE_NAME = "Estudio";
        static final String COLUMN_NAME_NOMBRE = "Nombre" ;
        static final String COLUMN_NAME_DIRECCION="Direccion";
        static final String COLUMN_NAME_LATITUD="Latitud";
        static final String COLUMN_NAME_LONGITUD="Longitud";
        static final String COLUMN_NAME_EMAIL="Email";
        static final String COLUMN_NAME_TELEFONO="Telefono";
    }

    static class entidadWeb implements BaseColumns{
        static final String TABLE_NAME = "Web";
        static final String COLUMN_NAME_URL ="URL";
        static final String COLUMN_NAME_ID_TATUADOR ="ID_Tatuador";
        static final String COLUMN_NAME_ID_ESTUDIO ="ID_Estudio";
    }

    static class entidadFoto implements BaseColumns{
        static final String TABLE_NAME = "Fotos";
        static final String COLUMN_NAME_HASH ="Hash";
        static final String COLUMN_NAME_FOTO ="Bitmap";
        static final String COLUMN_NAME_ID_TATUADOR ="ID_Tatuador";
    }

    //Tambien creamos strings de las sentencias SQL CREATE y DELETE que usaremos al inicio (no así los INSERT, SELECT...)
    private static final String SQL_CREATE_TABLE_TATUADOR =
            "CREATE TABLE " + entidadTatuador.TABLE_NAME + " (" +
                    entidadTatuador._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    entidadTatuador.COLUMN_NAME_NOMBRE + " TEXT," +
                    entidadTatuador.COLUMN_NAME_APELLIDOS + " TEXT," +
                    entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO+ " TEXT," +
                    entidadTatuador.COLUMN_NAME_ID_ESTUDIO+ " INTEGER)";

    private static final String SQL_DELETE_TABLE_TATUADOR =
            "DROP TABLE IF EXISTS " + entidadTatuador.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_ESTUDIO =
            "CREATE TABLE " + entidadEstudio.TABLE_NAME + " (" +
                    entidadEstudio._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    entidadEstudio.COLUMN_NAME_NOMBRE + " TEXT," +
                    entidadEstudio.COLUMN_NAME_DIRECCION + " TEXT," +
                    entidadEstudio.COLUMN_NAME_LATITUD + " REAL," +
                    entidadEstudio.COLUMN_NAME_LONGITUD + " REAL," +
                    entidadEstudio.COLUMN_NAME_TELEFONO + " TEXT," +
                    entidadEstudio.COLUMN_NAME_EMAIL+ " TEXT)";

    private static final String SQL_DELETE_TABLE_ESTUDIO =
            "DROP TABLE IF EXISTS " + entidadEstudio.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_WEB =
            "CREATE TABLE " + entidadWeb.TABLE_NAME + " (" +
                    entidadWeb._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    entidadWeb.COLUMN_NAME_URL + " TEXT," +
                    entidadWeb.COLUMN_NAME_ID_ESTUDIO + " INTEGER," +
                    entidadWeb.COLUMN_NAME_ID_TATUADOR + " INTEGER)";

    private static final String SQL_DELETE_TABLE_WEB =
            "DROP TABLE IF EXISTS " + entidadWeb.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_FOTO =
            "CREATE TABLE " + entidadFoto.TABLE_NAME + " (" +
                    entidadFoto._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    entidadFoto.COLUMN_NAME_HASH + " NUMERIC," +
                    entidadFoto.COLUMN_NAME_FOTO + " BLOB," +
                    entidadFoto.COLUMN_NAME_ID_TATUADOR + " INTEGER)";

    private static final String SQL_DELETE_TABLE_FOTO =
            "DROP TABLE IF EXISTS " + entidadFoto.TABLE_NAME;


    //Constructor
    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL(SQL_CREATE_TABLE_TATUADOR);
        db.execSQL(SQL_CREATE_TABLE_ESTUDIO);
        db.execSQL(SQL_CREATE_TABLE_WEB);
        db.execSQL(SQL_CREATE_TABLE_FOTO);

    }

    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_TABLE_TATUADOR);
        db.execSQL(SQL_DELETE_TABLE_ESTUDIO);
        db.execSQL(SQL_DELETE_TABLE_WEB);
        db.execSQL(SQL_DELETE_TABLE_FOTO);
        onCreate(db);
    }

    public void onDowngrade (SQLiteDatabase db,int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    void delete(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_TABLE_TATUADOR);
        db.execSQL(SQL_DELETE_TABLE_ESTUDIO);
        db.execSQL(SQL_DELETE_TABLE_WEB);
        db.execSQL(SQL_DELETE_TABLE_FOTO);
        onCreate(db);
    }

}

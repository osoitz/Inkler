package com.example.inkler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DBHelper extends SQLiteOpenHelper {

    //si cambiamos el modelo debe cambiar la version de la base de datos, así se ejecutarán los metodos onupgrade u ondowngrade
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Inkler.db";

    //Primero creamos unos string en los que guardar los nombres de tablas, columnas... para centralizar la información por si hay que hacer cambios
    public static class entidadTatuador implements BaseColumns{
        public static final String TABLE_NAME = "Tatuador";
        public static final String COLUMN_NAME_NOMBRE = "Nombre" ;
        public static final String COLUMN_NAME_APELLIDOS="Apellidos";
        public static final String COLUMN_NAME_NOMBRE_ARTISTICO="Nombre_Artistico";
        public static final String COLUMN_NAME_EMAIL="Email";
        public static final String COLUMN_NAME_TELEFONO="Telefono";
        public static final String COLUMN_NAME_ID_ESTUDIO="ID_Estudio";
    }

    public static class entidadEstudio implements BaseColumns{
        public static final String TABLE_NAME = "Estudio";
        public static final String COLUMN_NAME_NOMBRE = "Nombre" ;
        public static final String COLUMN_NAME_DIRECCION="Direccion";
        public static final String COLUMN_NAME_LATITUD="Latitud";
        public static final String COLUMN_NAME_LONGITUD="Longitud";
        public static final String COLUMN_NAME_EMAIL="Email";
        public static final String COLUMN_NAME_TELEFONO="Telefono";
    }

    public static class entidadWeb implements BaseColumns{
        public static final String TABLE_NAME = "Web";
        public static final String COLUMN_NAME_URL ="URL";
        public static final String COLUMN_NAME_ID_TATUADOR ="ID_Tatuador";
        public static final String COLUMN_NAME_ID_ESTUDIO ="ID_Estudio";
    }

    //Tambien creamos strings de las sentencias SQL CREATE y DELETE que usaremos al inicio (no así los INSERT, SELECT...)
    private static final String SQL_CREATE_TABLE_TATUADOR =
            "CREATE TABLE " + entidadTatuador.TABLE_NAME + " (" +
                    entidadTatuador._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    entidadTatuador.COLUMN_NAME_NOMBRE + " TEXT," +
                    entidadTatuador.COLUMN_NAME_APELLIDOS + " TEXT," +
                    entidadTatuador.COLUMN_NAME_NOMBRE_ARTISTICO+ " TEXT," +
                    entidadTatuador.COLUMN_NAME_EMAIL + " TEXT," +
                    entidadTatuador.COLUMN_NAME_TELEFONO + " TEXT," +
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
                    entidadEstudio.COLUMN_NAME_EMAIL+ " INTEGER)";

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
    
    //Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL(SQL_CREATE_TABLE_TATUADOR);
        db.execSQL(SQL_CREATE_TABLE_ESTUDIO);
        db.execSQL(SQL_CREATE_TABLE_WEB);
    }

    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_TABLE_TATUADOR);
        db.execSQL(SQL_DELETE_TABLE_ESTUDIO);
        db.execSQL(SQL_DELETE_TABLE_WEB);
        onCreate(db);
    }

    public void onDowngrade (SQLiteDatabase db,int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

}

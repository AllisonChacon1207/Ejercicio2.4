package com.example.ejercicio2_4.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteConexion extends SQLiteOpenHelper
{
    Context context;
    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, dbname, factory, version);
    }

    public SQLiteConexion(Context context) {
        super(context, Transacciones.NameDatabase, null, Transacciones.versionDatabase);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
      db.execSQL(Transacciones.CreateTableSignature);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(Transacciones.DROPTableSignature);
        onCreate(db);
    }

}

package com.jon_cantero.gastosdecoche.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VehiclesDb extends SQLiteOpenHelper {

    String createVehicles = "CREATE TABLE vehicles(id INTEGER PRIMARY KEY AUTOINCREMENT, modelo text,marca text, tipo numeric, itv text)";
    String createFuels = "CREATE TABLE fuels(id INTEGER PRIMARY KEY AUTOINCREMENT, fecha text,cuantia numeric, litros numeric, kilometros numeric, consumo_medio_anterior numeric, vehicles_id integger, FOREIGN KEY(vehicles_id) REFERENCES vehicles(id))";
    String createExpenses = "CREATE TABLE expenses(id INTEGER PRIMARY KEY AUTOINCREMENT, fecha text, cuantia numeric, concepto text, categoria numeric, tipo_de_gasto numeric, vehicles_id integger, FOREIGN KEY(vehicles_id) REFERENCES vehicles(id))";

    public VehiclesDb(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(contexto,nombre,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(createVehicles);
        db.execSQL(createFuels);
        db.execSQL(createExpenses);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int versionAnt,int versionNue)
    {
        db.execSQL("DROP TABLE IF EXISTS vehicles");
        db.execSQL("DROP TABLE IF EXISTS fuels");
        db.execSQL("DROP TABLE IF EXISTS expenses");

        db.execSQL(createVehicles);
        db.execSQL(createFuels);
        db.execSQL(createExpenses);
    }

}

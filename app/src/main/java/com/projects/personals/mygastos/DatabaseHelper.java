package com.projects.personals.mygastos;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "gastos.db";
    private static final int  DATABASE_VERSION = 6;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String createTableQuery =  "CREATE TABLE gastos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "monto DOUBLE," +
                "mes TEXT," +
                "dia INTEGER,"+
                "year INTEGER,"+
                "type TEXT)";
        db.execSQL(createTableQuery);
    }
    public List<gastosList>  orderGastosby(String month, String param){
        List<gastosList> gastosOrdered = new ArrayList<>();


        SQLiteDatabase db = getReadableDatabase();

        String selection = "mes = ?";
        String[] selectionArgs = {month};

        Cursor cursor = db.query(
                "gastos",
                null,
                selection,
                selectionArgs,
                null,
                null,
                param
        );
        while (cursor.moveToNext()) {
            // Obtener los datos del cursor y crear objetos gastosList
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));

            int idInt = (int) id;
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

            String montoStr = cursor.getString(cursor.getColumnIndexOrThrow("monto"));
            double monto = Double.parseDouble(montoStr);

            String mes = cursor.getString(cursor.getColumnIndexOrThrow("mes"));


            String diaStr = cursor.getString(cursor.getColumnIndexOrThrow("dia"));
            int dia = Integer.parseInt(diaStr);

            String yearStr = cursor.getString(cursor.getColumnIndexOrThrow("year"));
            int year = Integer.parseInt(yearStr);

            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));

            gastosList gasto = new gastosList( name,idInt, monto, mes, dia, year, type);
            gastosOrdered.add(gasto);
        }

        cursor.close();
        db.close();

        return gastosOrdered;
    }
    public List<gastosList> getAllGastos() {
        List<gastosList> mygastos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("gastos", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));  // Cambia "_id" al nombre real de la columna de ID en tu base de datos
            int idInt = (int) id;
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

            String montoStr = cursor.getString(cursor.getColumnIndexOrThrow("monto"));
            double monto = Double.parseDouble(montoStr);

            String mes = cursor.getString(cursor.getColumnIndexOrThrow("mes"));


            String diaStr = cursor.getString(cursor.getColumnIndexOrThrow("dia"));
            int dia = Integer.parseInt(diaStr);

            String yearStr = cursor.getString(cursor.getColumnIndexOrThrow("year"));
            int year = Integer.parseInt(yearStr);

            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            mygastos.add(new gastosList(name,idInt,monto,mes,dia,year,type));
        }

        cursor.close();
        return mygastos;
    }

    public double calcularTotalGastosPorMes(String mes){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(monto) FROM gastos WHERE mes = ?";

        String[] selectionArgs = {String.valueOf(mes)};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        double total = 0;

        if(cursor.moveToFirst()){
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }
    public void deleteGasto(int id){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.delete("gastos",whereClause,whereArgs);
        db.close();
    }
    public List<gastosList> getGastosByMonth(String month) {
        List<gastosList> gastosByMonth = new ArrayList<>();


        SQLiteDatabase db = getReadableDatabase();

        String selection = "mes = ?";
        String[] selectionArgs = {month};

        Cursor cursor = db.query(
                "gastos",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            // Obtener los datos del cursor y crear objetos gastosList
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));

            int idInt = (int) id;
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

            String montoStr = cursor.getString(cursor.getColumnIndexOrThrow("monto"));
            double monto = Double.parseDouble(montoStr);

            String mes = cursor.getString(cursor.getColumnIndexOrThrow("mes"));


            String diaStr = cursor.getString(cursor.getColumnIndexOrThrow("dia"));
            int dia = Integer.parseInt(diaStr);

            String yearStr = cursor.getString(cursor.getColumnIndexOrThrow("year"));
            int year = Integer.parseInt(yearStr);

            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));

            gastosList gasto = new gastosList( name,idInt, monto, mes, dia, year, type);
            gastosByMonth.add(gasto);
        }

        cursor.close();
        db.close();

        return gastosByMonth;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Realizar una migración para agregar las nuevas columnas
            db.execSQL("ALTER TABLE gastos RENAME COLUMN tipo TO type");
        }
    }
}

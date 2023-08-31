package com.projects.personals.mygastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    public EditText name;
    public EditText monto;
    public EditText tipo;
    public EditText dia;
    public String mes;
    public EditText year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Spinner monthSpinner = findViewById(R.id.selectFieldMonth);


        // Crear un ArrayAdapter con los nombres de los meses
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.months_array, android.R.layout.simple_spinner_item);

        // Especificar el diseño para los elementos desplegables
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el ArrayAdapter al Spinner
        monthSpinner.setAdapter(adapter);

        // Manejar eventos de selección del Spinner si es necesario
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = parent.getItemAtPosition(position).toString();
                mes = selectedMonth;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acción cuando no se selecciona nada
            }
        });
    }
    public void AddGasto(View v){
        //RECOPILACION DE INPUTS
        name =findViewById(R.id.nameInput);
        monto = findViewById(R.id.montoInput);
        tipo = findViewById(R.id.typeInput);
        year = findViewById(R.id.selectFieldYear);
        dia = findViewById(R.id.selectFieldDay);

        //PASAMOS A LOS DATOS QUE NOS PIDEN EN LA BASE DE DATOS

        String valorName = name.getText().toString();
        double valorMonto = Double.parseDouble(monto.getText().toString());
        String valorTipo = tipo.getText().toString();
        int valorYear = Integer.parseInt(year.getText().toString());
        int valorDia = Integer.parseInt(dia.getText().toString());

        DatabaseHelper databaseHelper = new DatabaseHelper(this);


        // Obtener una referencia a la base de datos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Crear un ContentValues para agregar los datos
        ContentValues values = new ContentValues();
        values.put("name", valorName);
        values.put("monto", valorMonto);
        values.put("mes", mes);
        values.put("dia", valorDia);
        values.put("year",valorYear);
        values.put("type",valorTipo);

        // Insertar los datos en la tabla
        long newRowId = db.insert("gastos", null, values);

        if (newRowId != -1) {
            // Inserción exitosa, puedes mostrar un mensaje o realizar alguna acción
            Toast.makeText(this, "Inserción exitosa", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity2.this, MainActivity.class));
                    finish();
                }
            }, Toast.LENGTH_SHORT + 500);
        } else {
            // Error en la inserción, puedes mostrar un mensaje de error
            // ...
        }

        // Cerrar la base de datos
        db.close();
    }

}
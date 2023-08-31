package com.projects.personals.mygastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    public EditText name;
    public EditText monto;
    public String tipo;
    public EditText dia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Spinner tiposSelected = findViewById(R.id.selectFieldTipos);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.tipos_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);

        tiposSelected.setAdapter(adapter2 );

        Calendar calendar = Calendar.getInstance();
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        InputFilter[] filters = { new InputFilterMinMax(1, lastDayOfMonth) };
        dia = findViewById(R.id.selectFieldDay);
        dia.setFilters(filters);
        // Aplicar el ArrayAdapter al Spinner


        // Manejar eventos de selección del Spinner si es necesario
        tiposSelected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String tipoExtract= parent.getItemAtPosition(position).toString();
                tipo = tipoExtract;
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Acción cuando no se selecciona nada
            }
        });
    }
    public void volver(View v){
        startActivity(new Intent(MainActivity2.this, MainActivity.class));
        finish();
    }
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    public void AddGasto(View v){
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int yearActual = calendar.get(Calendar.YEAR);

        // Obtener el nombre del mes en español
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es", "ES"));

        String[] monthNames = dfs.getMonths();
        String currentMonthName = monthNames[currentMonth];

        //RECOPILACION DE INPUTS
        name =findViewById(R.id.nameInput);
        monto = findViewById(R.id.montoInput);

        dia = findViewById(R.id.selectFieldDay);

        //PASAMOS A LOS DATOS QUE NOS PIDEN EN LA BASE DE DATOS

        String valorName = name.getText().toString();
        double valorMonto = Double.parseDouble(monto.getText().toString());
        int valorDia = Integer.parseInt(dia.getText().toString());

        DatabaseHelper databaseHelper = new DatabaseHelper(this);


        // Obtener una referencia a la base de datos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Crear un ContentValues para agregar los datos
        ContentValues values = new ContentValues();
        values.put("name", valorName);
        values.put("monto", valorMonto);
        values.put("mes", capitalizeFirstLetter(currentMonthName));
        values.put("dia", valorDia);
        values.put("year",yearActual);
        values.put("type",tipo);

        // Insertar los datos en la tabla
        long newRowId = db.insert("gastos", null, values);

        if (newRowId != -1) {

            Log.d("test","click");
            // Inserción exitosa, puedes mostrar un mensaje o realizar alguna acción
            Toast.makeText(this, "Gasto añadido", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity2.this, MainActivity.class));
                    finish();
                }
            }, Toast.LENGTH_SHORT + 500);
        } else {
            Toast.makeText(this, "Error al ingresar", Toast.LENGTH_SHORT).show();
        }

        // Cerrar la base de datos
        db.close();
    }

}
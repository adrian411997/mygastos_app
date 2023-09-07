package com.projects.personals.mygastos.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.personals.mygastos.DatabaseHelper;
import com.projects.personals.mygastos.ListAdapter;
import com.projects.personals.mygastos.R;
import com.projects.personals.mygastos.databinding.FragmentHomeBinding;
import com.projects.personals.mygastos.gastosList;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper databaseHelper;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Context context = requireContext();
        databaseHelper = new DatabaseHelper(context);
        Spinner monthSpinner = root.findViewById(R.id.selectMonthsPanel);
        Spinner orderSpinner = root.findViewById(R.id.orderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.months_array, android.R.layout.simple_spinner_item);

        // Especificar el diseño para los elementos desplegables
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el ArrayAdapter al Spinner
        monthSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                context, R.array.orders_array, android.R.layout.simple_spinner_item);

        // Especificar el diseño para los elementos desplegables
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el ArrayAdapter al Spinner
        orderSpinner.setAdapter(adapter2);
        // Obtener el mes actual
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        // Establecer el mes actual como valor seleccionado en el Spinner
        monthSpinner.setSelection(currentMonth);

        List<gastosList> listaGasto = databaseHelper.getGastosByMonth(monthSpinner.getSelectedItem().toString());

        RecyclerView recyclerView = root.findViewById(R.id.list);
        ListAdapter listAdapter = new ListAdapter(listaGasto, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);


        TextView totalMount = root.findViewById(R.id.totalAmount);
        String total = String.valueOf(databaseHelper.calcularTotalGastosPorMes(monthSpinner.getSelectedItem().toString()));
        totalMount.setText("$".concat(total));

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedMonth = parentView.getItemAtPosition(position).toString();

                List<gastosList> gastosBySelectedMonth = databaseHelper.getGastosByMonth(selectedMonth);
                //Log.d("HomeFragment", "Número de elementos en la lista filtrada: " + gastosBySelectedMonth.size());

                // Configurar el RecyclerView con los gastos filtrados
                ListAdapter listAdapter = new ListAdapter(gastosBySelectedMonth, context);
                recyclerView.setAdapter(listAdapter);

                TextView totalMount = root.findViewById(R.id.totalAmount);
                String total = String.valueOf(databaseHelper.calcularTotalGastosPorMes(monthSpinner.getSelectedItem().toString()));
                totalMount.setText("$".concat(total));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada en este caso
            }
        });
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedMonth = monthSpinner.getSelectedItem().toString() ;
                if(position == 1){
                    String mountoOrder = Character.toLowerCase(parentView.getItemAtPosition(position).toString().charAt(0)) + parentView.getItemAtPosition(position).toString().substring(1);
                    List<gastosList> gastosOrderedByMount = databaseHelper.orderGastosby(selectedMonth,mountoOrder);
                    ListAdapter listAdapter = new ListAdapter(gastosOrderedByMount,context);
                    recyclerView.setAdapter(listAdapter);
                }else if(position == 2){
                    String dayOrder = Character.toLowerCase(parentView.getItemAtPosition(position).toString().charAt(0)) + parentView.getItemAtPosition(position).toString().substring(1);
                    List<gastosList> gastosOrderedByMount = databaseHelper.orderGastosby(selectedMonth,dayOrder);
                    ListAdapter listAdapter = new ListAdapter(gastosOrderedByMount,context);
                    recyclerView.setAdapter(listAdapter);
                }else if(position ==3){
                    String nameOrder ="name";
                    List<gastosList> gastosOrderedByMount = databaseHelper.orderGastosby(selectedMonth,nameOrder);
                    ListAdapter listAdapter = new ListAdapter(gastosOrderedByMount,context);
                    recyclerView.setAdapter(listAdapter);
                }else{
                    List<gastosList> gastosBySelectedMonth = databaseHelper.getGastosByMonth(selectedMonth);
                    //Log.d("HomeFragment", "Número de elementos en la lista filtrada: " + gastosBySelectedMonth.size());

                    // Configurar el RecyclerView con los gastos filtrados
                    ListAdapter listAdapter = new ListAdapter(gastosBySelectedMonth, context);
                    recyclerView.setAdapter(listAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Manejar caso cuando no se selecciona nada
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
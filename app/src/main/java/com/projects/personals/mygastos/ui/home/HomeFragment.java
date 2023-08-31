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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.months_array, android.R.layout.simple_spinner_item);

        // Especificar el diseño para los elementos desplegables
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el ArrayAdapter al Spinner
        monthSpinner.setAdapter(adapter);

        // Obtener el mes actual
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        // Establecer el mes actual como valor seleccionado en el Spinner
        monthSpinner.setSelection(currentMonth);

        List<gastosList> listaGasto = databaseHelper.getGastosByMonth(monthSpinner.getSelectedItem().toString());
        Log.d("MainActivity", "Número de elementos en la lista: " + listaGasto.size());
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.projects.personals.mygastos;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    public List<gastosList> mData;
    public LayoutInflater mInflater;
    private Context context;
    DatabaseHelper databaseHelper;
    public ListAdapter(List<gastosList> itemList,Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.databaseHelper = new DatabaseHelper(context); // Inicializar la instancia de DatabaseHelper
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element,null);
        return new ListAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
        int itemId = mData.get(position).id;

        FloatingActionButton deleteButton = holder.itemView.findViewById(R.id.deleteItem);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner months = view.getRootView().findViewById(R.id.selectMonthsPanel);
                String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                Calendar calendar = Calendar.getInstance();
                int currentMonth = calendar.get(Calendar.MONTH);
                boolean duda = meses[currentMonth].equals(months.getSelectedItem().toString() );
                Log.d("MiApp",String.valueOf(duda));
                Log.d("MiApp",meses[currentMonth]);
                Log.d("MiApp",months.getSelectedItem().toString());
                if(meses[currentMonth].equals(months.getSelectedItem().toString() )) {

;                   int clickedPosition = holder.getAdapterPosition();
                    int clickedItemId = Integer.parseInt(holder.itemIdTextView.getText().toString());
                    databaseHelper.deleteGasto(clickedItemId);
                    mData.remove(clickedPosition);
                    notifyItemRemoved(clickedPosition);


                    TextView total = view.getRootView().findViewById(R.id.totalAmount);

                    String totalStr = String.valueOf(databaseHelper.calcularTotalGastosPorMes(months.getSelectedItem().toString()));
                    total.setText("$".concat(totalStr));
                }
            }
        });
    }

    public void setItems(List<gastosList> items){
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,fecha,tipo,monto,itemIdTextView;

        ViewHolder(View itemView ){
            super(itemView);

            monto = itemView.findViewById(R.id.viewMonto);
            name = itemView.findViewById(R.id.viewName);
            fecha = itemView.findViewById(R.id.viewFecha);
            tipo = itemView.findViewById(R.id.viewTipo);
            itemIdTextView = itemView.findViewById(R.id.itemId); // Inicializar el TextView
        }
        void bindData(final gastosList item){


            String date = String.valueOf(item.getYear());
            String dateComplete = date.concat("/").concat(String.valueOf(item.getMesInt())).concat("/").concat(String.valueOf(item.getDia()));
            name.setText(item.getNombre());
            String signo = "$";
            monto.setText(signo.concat(String.valueOf(item.getMonto())));
            fecha.setText(dateComplete);
            tipo.setText(item.getType());
            itemIdTextView.setText(String.valueOf(item.getId()));
        }
    }
}

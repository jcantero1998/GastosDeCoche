package com.jon_cantero.gastosdecoche.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jon_cantero.gastosdecoche.Models.Expenses;
import com.jon_cantero.gastosdecoche.R;

import java.util.List;

public class ExpenseAdapter extends BaseAdapter {
    //region propiedades
    //implementamos los métodos abstractos
    private Context context;
    private int layout;
    private List<Expenses> expenses;
    //endregion

    //region constuctor
    public ExpenseAdapter(Context context, int layout, List<Expenses> expenses) {
        this.context = context;
        this.layout = layout;
        this.expenses = expenses;
    }
    //endregion

    //region metodos
    // Le dice al activity cuántas veces hay que iterar sobre un listview
    @Override
    public int getCount() {
        return this.expenses.size();
    }

    // Para obtener un item, me devuelve el item de la posicion
    @Override
    public Object getItem(int position) {
        return this.expenses.get(position);
    }

    //Para obtener el id de un item
    @Override
    public long getItemId(int id) {
        return id;
    }

    // Metodo donde se dibuja lo que queremos hacer (convertView es la lista de vistas que se va a dibujar)
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // Copiamos la vista que vamos a inflar
        View v = convertView;
        if (convertView ==null){
            // Usamos la clase LayoutInflater que se obtiene de un método de la misma clase pasándole u contexto
            // Inflamos la vista que nos hallegado con el layout personalizado
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            // Le indicamos el Layout que hemos creado antes
            v = layoutInflater.inflate(R.layout.list_expenses_view, null);
        }

        // Nos traemos el valor de la posición
        String fecha = expenses.get(position).getFecha();
        String concepto = expenses.get(position).getConcepto();

        // Rellenar el textView del Layout
        TextView textViewFecha = (TextView) v.findViewById(R.id.textViewFecha);
        textViewFecha.setText(fecha);
        TextView textViewConcepto = (TextView) v.findViewById(R.id.textViewConcepto);
        textViewConcepto.setText(concepto);

        // Devolvemos la vista inflada y modificada
        return v;
    }
}
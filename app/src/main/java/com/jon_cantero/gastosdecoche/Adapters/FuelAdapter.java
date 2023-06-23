package com.jon_cantero.gastosdecoche.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jon_cantero.gastosdecoche.Models.Fuel;
import com.jon_cantero.gastosdecoche.R;

import java.util.List;

public class FuelAdapter extends BaseAdapter {
    //region propiedades
    //implementamos los métodos abstractos
    private Context context;
    private int layout;
    private List<Fuel> fuels;
    //endregion

    //region constuctor
    public FuelAdapter(Context context, int layout, List<Fuel> fuels) {
        this.context = context;
        this.layout = layout;
        this.fuels = fuels;
    }
    //endregion

    //region metodos
    // Le dice al activity cuántas veces hay que iterar sobre un listview
    @Override
    public int getCount() {
        return this.fuels.size();
    }

    // Para obtener un item, me devuelve el item de la posicion
    @Override
    public Object getItem(int position) {
        return this.fuels.get(position);
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
            v = layoutInflater.inflate(R.layout.list_fuels_view, null);
        }

        // Nos traemos el valor de la posición
        String fecha = fuels.get(position).getFecha();
        float cuantia = fuels.get(position).getCuantia();

        // Rellenar el textView del Layout
        TextView textViewFecha = (TextView) v.findViewById(R.id.textViewFecha);
        textViewFecha.setText(fecha);
        TextView textViewCuantia = (TextView) v.findViewById(R.id.textViewCuantia);
        textViewCuantia.setText(String.valueOf(cuantia)+"€");

        // Devolvemos la vista inflada y modificada
        return v;
    }
}
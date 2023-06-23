package com.jon_cantero.gastosdecoche.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jon_cantero.gastosdecoche.Models.Vehicle;
import com.jon_cantero.gastosdecoche.R;

import java.util.List;

public class VehicleAdapter extends BaseAdapter {
    //region propiedades
    //implementamos los métodos abstractos
    private Context context;
    private int layout;
    private List<Vehicle> vehiculos;
    //endregion

    //region constuctor
    public VehicleAdapter(Context context, int layout, List<Vehicle> vehiculos) {
        this.context = context;
        this.layout = layout;
        this.vehiculos = vehiculos;
    }
    //endregion

    //region metodos
    // Le dice al activity cuántas veces hay que iterar sobre un listview
    @Override
    public int getCount() {
        return this.vehiculos.size();
    }

    // Para obtener un item, me devuelve el item de la posicion
    @Override
    public Object getItem(int position) {
        return this.vehiculos.get(position);
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
            v = layoutInflater.inflate(R.layout.list_vehicles_view, null);
        }

        // Nos traemos el valor de la posición
        String marca = vehiculos.get(position).getMarca();
        String modelo = vehiculos.get(position).getModelo();
        int tipo = vehiculos.get(position).getTipo();

        // Rellenar el textView del Layout
        TextView textViewMarca = (TextView) v.findViewById(R.id.textViewMarca);
        textViewMarca.setText(marca);
        TextView textViewModelo = (TextView) v.findViewById(R.id.textViewModelo);
        textViewModelo.setText(modelo);

        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

        switch (tipo) {
            case 0:
                imageView.setImageResource(R.drawable.car);
                break;
            case 1:
                imageView.setImageResource(R.drawable.scooter);
                break;
            case 2:
                imageView.setImageResource(R.drawable.motorcycle);
                break;
            case 3:
                imageView.setImageResource(R.drawable.truck);
                break;
            case 4:
                imageView.setImageResource(R.drawable.other);
                break;
        }


        // Devolvemos la vista inflada y modificada
        return v;
    }
}

package com.jon_cantero.gastosdecoche.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jon_cantero.gastosdecoche.Activities.AddFuelActivity;
import com.jon_cantero.gastosdecoche.Activities.EditFuelActivity;
import com.jon_cantero.gastosdecoche.Activities.VehicleActivity;
import com.jon_cantero.gastosdecoche.Adapters.FuelAdapter;
import com.jon_cantero.gastosdecoche.Databases.VehiclesDb;
import com.jon_cantero.gastosdecoche.Models.Fuel;
import com.jon_cantero.gastosdecoche.R;

import java.util.ArrayList;
import java.util.List;

public class FuelFragment extends Fragment {

    private static List<Fuel> fuels = new ArrayList<Fuel>();
    private static VehiclesDb datos;
    private static SQLiteDatabase db;
    public static FuelAdapter fuelAdapterList;
    private ListView listView;

    public FuelFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fuel, container, false);


        datos = new VehiclesDb(getContext(),"Datos",null,1);

        fuelAdapterList = new FuelAdapter(getContext(),R.layout.list_fuels_view,fuels);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(fuelAdapterList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EditFuelActivity.class);
                intent.putExtra("fuel",fuels.get(position));
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);

        //Icono flotante
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFuelActivity.class);
                startActivity(intent);
            }
        });
        loadFuel();

        return view;
    }


    // Inflamos el layout de context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Antes de inflarlo, añadimos un título al menú contextual
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(fuels.get(info.position).getFecha());
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    // Manejamos eventos click del menú contextual
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.delete_item:
                deleteFuel(fuels.get(info.position).getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    //region crud
    public static String loadFuel() {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            //Vaciamos la lista
            fuels.clear();

            //Volvemos a cargar la lista
            Cursor fila = db.rawQuery("select * from fuels where vehicles_id=?",new  String[]{String.valueOf(VehicleActivity.currentVehicle.getId())});
            if(fila.moveToFirst()){
                do{
                    fuels.add(new Fuel(fila.getInt(0),fila.getString(1),fila.getFloat(2),fila.getFloat(3),fila.getFloat(4),fila.getFloat(5),fila.getInt(6)));
                }while(fila.moveToNext());
            }
            db.close();
            return "";
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }

    public static String addFuel(Fuel fuel) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            ContentValues registro = new ContentValues();

            registro.put("fecha",fuel.getFecha());
            registro.put("cuantia",fuel.getCuantia());
            registro.put("litros",fuel.getLitros());
            registro.put("kilometros",fuel.getKilometros());
            registro.put("consumo_medio_anterior",fuel.getConsumoMedioAnterior());
            registro.put("vehicles_id",VehicleActivity.currentVehicle.getId());

            if(db.insert("fuels", null, registro) == -1)
            {
                db.close();
                return "Error al añadir el repostaje";
            }else{
                //Cargamos la lista
                loadFuel();
                //Notificamos el cambio a los adapters
                fuelAdapterList.notifyDataSetChanged();
                db.close();
                return "";
            }
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }

    public static String updateFuel(Fuel fuel) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            ContentValues registro = new ContentValues();

            registro.put("fecha",fuel.getFecha());
            registro.put("cuantia",fuel.getCuantia());
            registro.put("litros",fuel.getLitros());
            registro.put("kilometros",fuel.getKilometros());
            registro.put("consumo_medio_anterior",fuel.getConsumoMedioAnterior());

            int cantidadModificados = db.update("fuels",registro,"id=?",new String[]{String.valueOf(fuel.getId())});
            if (cantidadModificados != 1) {
                db.close();
                return "No ha sido posible modificar el combustible";
            } else {
                //Cargamos la lista
                loadFuel();
                //Notificamos el cambio a los adapters
                fuelAdapterList.notifyDataSetChanged();
                db.close();
                return "";
            }
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }

    public static String deleteFuel(int id) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            int cantidadBorrados = db.delete("fuels","id=?",new  String[]{String.valueOf(id)});
            if (cantidadBorrados!=1){
                db.close();
                return "No ha sido posible eliminar el combustible";
            }else{
                //Volvemos a cargar las personas
                loadFuel();
                //Notificamos el cambio a los adapters para que aparezca la persona
                fuelAdapterList.notifyDataSetChanged();
                db.close();
                return "";
            }
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }
    //endregion

}

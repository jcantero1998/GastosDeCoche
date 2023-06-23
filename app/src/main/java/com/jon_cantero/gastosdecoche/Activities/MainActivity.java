package com.jon_cantero.gastosdecoche.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jon_cantero.gastosdecoche.Adapters.VehicleAdapter;
import com.jon_cantero.gastosdecoche.Databases.VehiclesDb;
import com.jon_cantero.gastosdecoche.Models.Vehicle;
import com.jon_cantero.gastosdecoche.R;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<Vehicle> vehiculos = new ArrayList<Vehicle>();
    private static VehiclesDb datos;
    private static SQLiteDatabase db;
    public static VehicleAdapter vehicleAdapterList;
    private ListView listView;
    private AlertDialog dialog;
    AlertDialog.Builder builder;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = new VehiclesDb(this,"Datos",null,1);

        vehicleAdapterList = new VehicleAdapter(this,R.layout.list_vehicles_view,vehiculos);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(MainActivity.vehicleAdapterList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, VehicleActivity.class);
                intent.putExtra("vehicle",vehiculos.get(position));
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);

        //Icono flotante de añadir vehiculos
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddVehicleActivity.class);
                startActivity(intent);
            }
        });

        loadVehicles();

        //Dialog para pasar la ITV
        List<Vehicle> vehiculosApasarITV = vehiclesToPassITV();
        for (Vehicle v: vehiculosApasarITV) {

            builder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_vehicle_to_pass_itv, null);

            final TextView txtTituloDialog = (TextView) mView.findViewById(R.id.txtTituloDialog);
            txtTituloDialog.setText(v.getMarca().toUpperCase()+" "+v.getModelo().toUpperCase());

            final TextView editTextFecha = (TextView) mView.findViewById(R.id.editTextFecha);
            editTextFecha.setText(editTextFecha.getText()+" "+v.getItv());

            Button buttonVolver = (Button) mView.findViewById(R.id.buttonVolver);
            buttonVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            builder.setView(mView);
            dialog = builder.create();
            dialog.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Vehicle> vehiclesToPassITV() {

        List<Vehicle> vehiculosApasarITV = new ArrayList<Vehicle>();

        //Calculamos la fecha actual
        LocalDate date = LocalDate.now();

        //Recorremos los vehiculos
        for (Vehicle v: vehiculos) {

            //Calculamos la fecha del vehiculo
            String[] fecha = v.getItv().split("/");
            LocalDate vehicleDate = LocalDate.of(Integer.parseInt(fecha[2]),Integer.parseInt(fecha[1]),Integer.parseInt(fecha[0]));

            //Calculamos la diferencia de las fechas en dias
            Duration diff = Duration.between(date.atStartOfDay(), vehicleDate.atStartOfDay());
            long diffDays = diff.toDays();

            //Si le queda menos de una semana para pasar la ITV, lo añadimos a la lista
            if (diffDays < 7 && diffDays>=0){
                vehiculosApasarITV.add(v);
            }
        }
        return vehiculosApasarITV;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_item:
                builder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_about_us, null);

                Button buttonVolver = (Button) mView.findViewById(R.id.buttonVolver);
                buttonVolver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                builder.setView(mView);
                dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Inflamos el layout de context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        // Antes de inflarlo, añadimos un título al menú contextual
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(vehiculos.get(info.position).getMarca() +" "+ vehiculos.get(info.position).getModelo());
        inflater.inflate(R.menu.context_menu, menu);
    }

    // Manejamos eventos click del menú contextual
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.delete_item:
                deleteVehicle(vehiculos.get(info.position).getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    //region crud
    public static String loadVehicles() {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            //Vaciamos la lista
            vehiculos.clear();

            //Volvemos a cargar la lista
            Cursor fila = db.rawQuery("select * from vehicles", null);
            if(fila.moveToFirst()){
                do{
                    vehiculos.add(new Vehicle(fila.getInt(0),fila.getString(1),fila.getString(2),fila.getInt(3),fila.getString(4)));
                }while(fila.moveToNext());
            }
            db.close();
            return "";
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }
    public static String addVehicle(Vehicle vehicle) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            ContentValues registro = new ContentValues();

            registro.put("modelo",vehicle.getModelo());
            registro.put("marca",vehicle.getMarca());
            registro.put("tipo",vehicle.getTipo());
            registro.put("itv",vehicle.getItv());

            if(db.insert("vehicles", null, registro) == -1)
            {
                db.close();
                return "Error al añadir el vehiculo";
            }else{
                //Cargamos la lista
                loadVehicles();
                //Notificamos el cambio a los adapters
                vehicleAdapterList.notifyDataSetChanged();
                db.close();
                return "";
            }
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }

    public static String updateVehicle(Vehicle vehicle) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            ContentValues registro = new ContentValues();

            registro.put("modelo",vehicle.getModelo());
            registro.put("marca",vehicle.getMarca());
            registro.put("tipo",vehicle.getTipo());
            registro.put("itv",vehicle.getItv());

            int cantidadModificados = db.update("vehicles",registro,"id=?",new String[]{String.valueOf(VehicleActivity.currentVehicle.getId())});
            if (cantidadModificados != 1) {
                db.close();
                return "No ha sido posible modificar el vehículo";
            } else {
                //Cargamos la lista
                loadVehicles();
                //Notificamos el cambio a los adapters
                vehicleAdapterList.notifyDataSetChanged();
                db.close();
                return "";
            }
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }

    public static String deleteVehicle(int id) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            int cantidadBorrados = db.delete("vehicles","id=?",new  String[]{String.valueOf(id)});
            if (cantidadBorrados!=1){
                db.close();
                return "No ha sido posible eliminar el vehículo";
            }else{
                //Volvemos a cargar las lista
                loadVehicles();
                //Notificamos el cambio a los adapters
                vehicleAdapterList.notifyDataSetChanged();
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
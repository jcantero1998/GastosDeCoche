package com.jon_cantero.gastosdecoche.Activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jon_cantero.gastosdecoche.Adapters.PagerAdapter;
import com.jon_cantero.gastosdecoche.Databases.VehiclesDb;
import com.jon_cantero.gastosdecoche.Models.Vehicle;
import com.jon_cantero.gastosdecoche.R;

public class VehicleActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    public static Vehicle currentVehicle;
    private AlertDialog dialog;
    AlertDialog.Builder builder;
    private static VehiclesDb datos;
    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        if (getIntent().getSerializableExtra("vehicle")!=null){
        currentVehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
        }

        datos = new VehiclesDb(VehicleActivity.this,"Datos",null,1);

        setTabLayout();
        setViewPager();
        setListenerTabLayout(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info_vehicle, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_item:
                builder = new AlertDialog.Builder(VehicleActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_vehicle_info, null);

                final TextView editTextGastoMensual = (TextView) mView.findViewById(R.id.editTextGastoMensual);
                final TextView editTextGastoAnual = (TextView) mView.findViewById(R.id.editTextGastoAnual);
                final TextView editTextPrecioPorKm = (TextView) mView.findViewById(R.id.editTextPrecioPorKm);

                float totalFuel = loadSumFuel();
                float totalExpenses = loadSumExpenses();
                float totalKm = loadSumKm();

                if (totalExpenses != 0){
                    float gastoMensual = (totalFuel + totalExpenses) / 12;
                    editTextGastoMensual.setText(editTextGastoMensual.getText()  +" "+ String.valueOf(gastoMensual)+"€");

                    float gastoAnual = totalFuel + totalExpenses;
                    editTextGastoAnual.setText(editTextGastoAnual.getText()  +" "+ String.valueOf(gastoAnual)+"€");
                }else if (totalKm!=0){
                    float gastoMensual = (totalFuel) / 12;
                    editTextGastoMensual.setText(editTextGastoMensual.getText()  +" "+ String.valueOf(gastoMensual)+"€");

                    float gastoAnual = totalFuel + totalExpenses;
                    editTextGastoAnual.setText(editTextGastoAnual.getText() +" "+ String.valueOf(gastoAnual)+"€");
                }else{
                    editTextGastoMensual.setText("Introduce algún gasto para calcular el gasto mensual");
                    editTextGastoAnual.setText("Introduce algún gasto para calcular el gasto anual");
                }
                if (totalKm!=0) {
                    float pecioPorKm = (totalFuel + totalExpenses) / totalKm;
                    editTextPrecioPorKm.setText(editTextPrecioPorKm.getText() +" "+  String.valueOf(pecioPorKm)+"€");
                }else{
                    editTextPrecioPorKm.setText("Introduce algún repostaje para calcular el precio por km");
                }

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

    public float loadSumFuel() {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        float total;

        if (db != null) {
            Cursor fila = db.rawQuery("SELECT SUM(cuantia) FROM fuels WHERE vehicles_id=?", new String[]{String.valueOf(VehicleActivity.currentVehicle.getId())});
            if (fila.moveToFirst()) {
                total = fila.getInt(0);
                db.close();
                return total;
            }
        }
        total = 0;
        db.close();
        return total;
    }

    public float loadSumExpenses() {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        float total;

        if (db != null) {
            Cursor fila = db.rawQuery("SELECT SUM(cuantia) FROM expenses WHERE vehicles_id=?", new String[]{String.valueOf(VehicleActivity.currentVehicle.getId())});
            if (fila.moveToFirst()) {
                total = fila.getInt(0);
                db.close();
                return total;
            }
        }
        total = 0;
        db.close();
        return total;
    }
    public float loadSumKm() {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        float total;

        if (db != null) {
            Cursor fila = db.rawQuery("SELECT SUM(kilometros) FROM fuels WHERE vehicles_id=?", new String[]{String.valueOf(VehicleActivity.currentVehicle.getId())});
            if (fila.moveToFirst()) {
                total = fila.getInt(0);
                db.close();
                return total;
            }
        }
        total = 0;
        db.close();
        return total;
    }

    //region ToolBar, TabLayout y ViewPager
    private void setTabLayout() {
        // Instancio los objetos tabLayout y ViewPager declarados en el layout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        // Añado al tabLayout las pestañas-tabs
        tabLayout.addTab(tabLayout.newTab().setText("Datos"));
        tabLayout.addTab(tabLayout.newTab().setText("Repostajes"));
        tabLayout.addTab(tabLayout.newTab().setText("Gastos"));
    }

    private void setViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // En el ViewPager tenemos que crear la clase
        // Instanciamos el PAgerAdapter, hay que tener en cuenta que haga el import
        // de nuestra clase, en vez de una del android
        // El número de tabs en vez de pasar un 3 en este caso le pasamos el getTabCount para que sea
        // más dinámico
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        // Asignamos el adaptador al ViewPager
        viewPager.setAdapter(adapter);
        // Configuramos el listener para que esté escuchando cada vez que cambiamos
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setListenerTabLayout(final ViewPager viewPager) {
        // Definimos que hay que hacer cuándo cambiemos de un tab a otro
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Cuando seleccionamos el tab
                // Toast.makeText(MainActivity.this, "Selected: " + tab.getText(),Toast.LENGTH_SHORT).show();
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Cuando el tab que está activo deja de estarlo
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Cuándo seleccionamos el mismo tab que está activo
            }
        });
    }
    //endregion
}

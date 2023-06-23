package com.jon_cantero.gastosdecoche.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jon_cantero.gastosdecoche.Models.Vehicle;
import com.jon_cantero.gastosdecoche.R;

import java.util.Calendar;

public class AddVehicleActivity extends AppCompatActivity {

    private Spinner spinnerTipoDeVehiculo;
    private EditText editTextMarca;
    private EditText editTextModelo;
    private EditText editTextItv;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        spinnerTipoDeVehiculo = (Spinner) findViewById(R.id.spinnerTipoDeVehiculo);
        editTextMarca = (EditText) findViewById(R.id.editTextMarca);
        editTextModelo = (EditText) findViewById(R.id.editTextModelo);
        editTextItv = (EditText) findViewById(R.id.editTextItv);
        editTextItv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AddVehicleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int nYear, int nMonth, int nDay) {
                        editTextItv.setText(nDay + "/" + (nMonth+1)+"/"+nYear);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        String[] tiposDeVehiculos = {"Turismo","Ciclomotor","Motocicleta","Camión","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tiposDeVehiculos);
        spinnerTipoDeVehiculo.setAdapter(adapter);

        findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createVehicle();
            }
        });
    }

    private void createVehicle() {
        if (!editTextMarca.getText().toString().isEmpty() && !editTextModelo.getText().toString().isEmpty() && spinnerTipoDeVehiculo.getSelectedItem() !=null) {
            Vehicle vehicle = new Vehicle(editTextModelo.getText().toString(), editTextMarca.getText().toString(),spinnerTipoDeVehiculo.getSelectedItemPosition(), editTextItv.getText().toString());
            String message = MainActivity.addVehicle(vehicle);
            if (message!=""){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }else{
                finish();
            }
        }else{
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}

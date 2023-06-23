package com.jon_cantero.gastosdecoche.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jon_cantero.gastosdecoche.Fragments.FuelFragment;
import com.jon_cantero.gastosdecoche.Models.Fuel;
import com.jon_cantero.gastosdecoche.R;

import java.util.Calendar;

public class EditFuelActivity extends AppCompatActivity {

    private EditText editTextFecha;
    private EditText editTextCuantia;
    private EditText editTextLitros;
    private EditText editTextKilometros;
    private EditText editTextConsumoMedioAnterior;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fuel);

        editTextFecha = (EditText) findViewById(R.id.editTextFecha);
        editTextCuantia = (EditText) findViewById(R.id.editTextCuantia);
        editTextLitros = (EditText) findViewById(R.id.editTextLitros);
        editTextKilometros = (EditText) findViewById(R.id.editTextKilometros);
        editTextConsumoMedioAnterior = (EditText) findViewById(R.id.editTextConsumoMedioAnterior);

        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(EditFuelActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int nYear, int nMonth, int nDay) {
                        editTextFecha.setText(nDay + "/" + (nMonth+1)+"/"+nYear);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        //Pasamos los datos del vehiculo
        Fuel currentFuel = (Fuel) getIntent().getSerializableExtra("fuel");

        id = currentFuel.getId();
        editTextFecha.setText(currentFuel.getFecha());
        editTextCuantia.setText(Float.valueOf(currentFuel.getCuantia()).toString());
        editTextLitros.setText(Float.valueOf(currentFuel.getLitros()).toString());
        editTextKilometros.setText(Float.valueOf(currentFuel.getKilometros()).toString());
        editTextConsumoMedioAnterior.setText(Float.valueOf(currentFuel.getConsumoMedioAnterior()).toString());

        findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFuel();
            }
        });
    }
    private void updateFuel() {
        float consumoMedioAnterior = 0;
        if (!editTextFecha.getText().toString().isEmpty() && !editTextCuantia.getText().toString().isEmpty() && !editTextLitros.getText().toString().isEmpty() && !editTextKilometros.getText().toString().isEmpty()) {
            if (!editTextConsumoMedioAnterior.getText().toString().isEmpty()){
                consumoMedioAnterior = Float.valueOf(editTextConsumoMedioAnterior.getText().toString());
            }
            Fuel fuel = new Fuel(id, editTextFecha.getText().toString(), Float.valueOf(editTextCuantia.getText().toString()),Float.valueOf(editTextLitros.getText().toString()), Float.valueOf(editTextKilometros.getText().toString()),consumoMedioAnterior);
            String message = FuelFragment.updateFuel(fuel);
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

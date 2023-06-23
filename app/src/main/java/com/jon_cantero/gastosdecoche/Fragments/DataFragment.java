package com.jon_cantero.gastosdecoche.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.jon_cantero.gastosdecoche.Activities.MainActivity;
import com.jon_cantero.gastosdecoche.Activities.VehicleActivity;
import com.jon_cantero.gastosdecoche.Models.Vehicle;
import com.jon_cantero.gastosdecoche.R;

import java.util.Calendar;


public class DataFragment extends Fragment {

    private Spinner spinnerTipoDeVehiculo;
    private EditText editTextMarca;
    private EditText editTextModelo;
    private EditText editTextItv;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    public DataFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        spinnerTipoDeVehiculo = (Spinner) view.findViewById(R.id.spinnerTipoDeVehiculo);
        editTextMarca = (EditText) view.findViewById(R.id.editTextMarca);
        editTextModelo = (EditText) view.findViewById(R.id.editTextModelo);
        editTextItv = (EditText) view.findViewById(R.id.editTextItv);
        editTextItv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int nYear, int nMonth, int nDay) {
                        editTextItv.setText(nDay + "/" + (nMonth+1)+"/"+nYear);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        String[] tiposDeVehiculos = {"Turismo","Ciclomotor","Motocicleta","Camión","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, tiposDeVehiculos);
        spinnerTipoDeVehiculo.setAdapter(adapter);

        //Pasamos los datos del vehiculo
        spinnerTipoDeVehiculo.setSelection(VehicleActivity.currentVehicle.getTipo());
        editTextMarca.setText(VehicleActivity.currentVehicle.getMarca());
        editTextModelo.setText(VehicleActivity.currentVehicle.getModelo());
        editTextItv.setText(VehicleActivity.currentVehicle.getItv());

        view.findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateVehicle();
            }
        });
        return view;
    }
    private void updateVehicle() {
        if (!editTextMarca.getText().toString().isEmpty() && !editTextModelo.getText().toString().isEmpty() && spinnerTipoDeVehiculo.getSelectedItem() !=null) {
            Vehicle vehicle = new Vehicle(editTextModelo.getText().toString(), editTextMarca.getText().toString(),spinnerTipoDeVehiculo.getSelectedItemPosition(), editTextItv.getText().toString());
            String message = MainActivity.updateVehicle(vehicle);
            if (message!=""){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Vehívulo actualizado", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}

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

import com.jon_cantero.gastosdecoche.Fragments.ExpensesFragment;
import com.jon_cantero.gastosdecoche.Models.Expenses;
import com.jon_cantero.gastosdecoche.R;

import java.util.Calendar;

public class EditExppenseActivity extends AppCompatActivity {

    private EditText editTextFecha;
    private EditText editTextCuantia;
    private EditText editTextConcepto;
    private Spinner spinnerCategoria;
    private Spinner spinnerTipoDeGasto;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exppense);


        editTextFecha = (EditText) findViewById(R.id.editTextFecha);
        editTextCuantia = (EditText) findViewById(R.id.editTextCuantia);
        editTextConcepto = (EditText) findViewById(R.id.editTextConcepto);
        spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        spinnerTipoDeGasto = (Spinner) findViewById(R.id.spinnerTipoDeGasto);
        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(EditExppenseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int nYear, int nMonth, int nDay) {
                        editTextFecha.setText(nDay + "/" + (nMonth+1)+"/"+nYear);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        String[] categorias = {"Avería","Seguro","Mantenimiento","Revisión","Peaje","Impuesto Municipal","ITV","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        spinnerCategoria.setAdapter(adapter);

        String[] tiposDeGastos = {"Gasto puntual","Gasto periódico"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tiposDeGastos);
        spinnerTipoDeGasto.setAdapter(adapter);


        //Pasamos los datos del vehiculo
        Expenses currentExpense = (Expenses) getIntent().getSerializableExtra("expense");

        id = currentExpense.getId();
        editTextFecha.setText(currentExpense.getFecha());
        editTextCuantia.setText(Float.valueOf(currentExpense.getCuantia()).toString());
        editTextConcepto.setText(currentExpense.getConcepto());
        spinnerCategoria.setSelection(currentExpense.getCategoria());
        spinnerTipoDeGasto.setSelection(currentExpense.getTipoDeGasto());

        findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateExpense();
            }
        });
    }
    private void updateExpense() {
        if (!editTextFecha.getText().toString().isEmpty() && !editTextCuantia.getText().toString().isEmpty() && !editTextConcepto.getText().toString().isEmpty() && spinnerCategoria.getSelectedItem() !=null && spinnerTipoDeGasto.getSelectedItem() !=null) {
            Expenses expense = new Expenses(id, editTextFecha.getText().toString(), Float.valueOf(editTextCuantia.getText().toString()),editTextConcepto.getText().toString(),spinnerCategoria.getSelectedItemPosition(),spinnerTipoDeGasto.getSelectedItemPosition());
            String message = ExpensesFragment.updateExpense(expense);
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

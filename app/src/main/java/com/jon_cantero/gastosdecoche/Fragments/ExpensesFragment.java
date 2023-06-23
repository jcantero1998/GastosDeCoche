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
import com.jon_cantero.gastosdecoche.Activities.AddExpenseActivity;
import com.jon_cantero.gastosdecoche.Activities.EditExppenseActivity;
import com.jon_cantero.gastosdecoche.Activities.VehicleActivity;
import com.jon_cantero.gastosdecoche.Adapters.ExpenseAdapter;
import com.jon_cantero.gastosdecoche.Databases.VehiclesDb;
import com.jon_cantero.gastosdecoche.Models.Expenses;
import com.jon_cantero.gastosdecoche.R;

import java.util.ArrayList;
import java.util.List;

public class ExpensesFragment extends Fragment {

    private static List<Expenses> expenses = new ArrayList<Expenses>();
    private static VehiclesDb datos;
    private static SQLiteDatabase db;
    public static ExpenseAdapter expenseAdapterList;
    private ListView listView;

    public ExpensesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        datos = new VehiclesDb(getContext(),"Datos",null,1);

        expenseAdapterList = new ExpenseAdapter(getContext(),R.layout.list_expenses_view,expenses);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(expenseAdapterList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EditExppenseActivity.class);
                intent.putExtra("expense",expenses.get(position));
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);

        //Icono flotante
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddExpenseActivity.class);
                startActivity(intent);
            }
        });
        loadExpenses();

        return view;
    }

    // Inflamos el layout de context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Antes de inflarlo, añadimos un título al menú contextual
        AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(expenses.get(info2.position).getFecha());
        getActivity().getMenuInflater().inflate(R.menu.context_menu2, menu);
    }

    // Manejamos eventos click del menú contextual
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.delete_item2:
                deleteExpense(expenses.get(info.position).getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    //region crud
    public static String loadExpenses() {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            //Vaciamos la lista
            expenses.clear();

            //Volvemos a cargar la lista
            Cursor fila = db.rawQuery("select * from expenses where vehicles_id=?",new  String[]{String.valueOf(VehicleActivity.currentVehicle.getId())});
            if(fila.moveToFirst()){
                do{
                    expenses.add(new Expenses(fila.getInt(0),fila.getString(1),fila.getFloat(2),fila.getString(3),fila.getInt(4),fila.getInt(5),fila.getInt(6)));
                }while(fila.moveToNext());
            }
            db.close();
            return "";
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }

    public static String addExpense(Expenses expense) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            ContentValues registro = new ContentValues();

            registro.put("fecha",expense.getFecha());
            registro.put("cuantia",expense.getCuantia());
            registro.put("concepto",expense.getConcepto());
            registro.put("categoria",expense.getCategoria());
            registro.put("tipo_de_gasto",expense.getTipoDeGasto());
            registro.put("vehicles_id",VehicleActivity.currentVehicle.getId());

            if(db.insert("expenses", null, registro) == -1)
            {
                db.close();
                return "Error al añadir el Gasto";
            }else{
                //Cargamos la lista
                loadExpenses();
                //Notificamos el cambio a los adapters
                expenseAdapterList.notifyDataSetChanged();
                db.close();
                return "";
            }
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }

    public static String updateExpense(Expenses expense) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            ContentValues registro = new ContentValues();

            registro.put("fecha",expense.getFecha());
            registro.put("cuantia",expense.getCuantia());
            registro.put("concepto",expense.getConcepto());
            registro.put("categoria",expense.getCategoria());
            registro.put("tipo_de_gasto",expense.getTipoDeGasto());

            int cantidadModificados = db.update("expenses",registro,"id=?",new String[]{String.valueOf(expense.getId())});
            if (cantidadModificados != 1) {
                db.close();
                return "No ha sido posible modificar el gasto";
            } else {
                //Cargamos la lista
                loadExpenses();
                //Notificamos el cambio a los adapters
                expenseAdapterList.notifyDataSetChanged();
                db.close();
                return "";
            }
        } else {
            db.close();
            return "Error al acceder a la base de datos";
        }
    }

    public static String deleteExpense(int id) {
        //Abrimos la base de datos en modo lectura y escritura
        db = datos.getWritableDatabase();

        if(db != null)
        {
            int cantidadBorrados = db.delete("expenses","id=?",new  String[]{String.valueOf(id)});
            if (cantidadBorrados!=1){
                db.close();
                return "No ha sido posible eliminar el gasto";
            }else{
                //Volvemos a cargar las personas
                loadExpenses();
                //Notificamos el cambio a los adapters para que aparezca la persona
                expenseAdapterList.notifyDataSetChanged();
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

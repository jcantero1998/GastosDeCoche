package com.jon_cantero.gastosdecoche.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jon_cantero.gastosdecoche.Fragments.DataFragment;
import com.jon_cantero.gastosdecoche.Fragments.ExpensesFragment;
import com.jon_cantero.gastosdecoche.Fragments.FuelFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public static DataFragment dataFragment = new DataFragment();
    public static ExpensesFragment expensesFragment = new ExpensesFragment();
    public static FuelFragment fuelFragment = new FuelFragment();

    // Extiendo de FragmentStatePagerAdapter
    // y creo el constructor y el resto de métodos obligatorios

    // En el constructor lo personalizamos añadiendo el número de tabs y lo
    // guardamos en una variable
    public PagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }

    // Llega una posición, este evento se lanza cada vez que hacemos click
    // o cambiamos de tab
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return dataFragment;
            case 1:
                return fuelFragment;
            case 2:
                return expensesFragment;
            default:
                return null;
        }
    }

    //Para decir cuántos tabs tenemos
    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
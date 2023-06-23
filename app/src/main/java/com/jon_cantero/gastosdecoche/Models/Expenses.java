package com.jon_cantero.gastosdecoche.Models;

import java.io.Serializable;

public class Expenses implements Serializable {
    private int id;
    private String fecha;
    private float cuantia;
    private String concepto;
    private int categoria;
    private int tipoDeGasto;
    private int vehicles_id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public float getCuantia() {
        return cuantia;
    }
    public void setCuantia(float cuantia) {
        this.cuantia = cuantia;
    }
    public String getConcepto() {
        return concepto;
    }
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
    public int getCategoria() {
        return categoria;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public int getTipoDeGasto() {
        return tipoDeGasto;
    }
    public void setTipoDeGasto(int tipoDeGasto) {
        this.tipoDeGasto = tipoDeGasto;
    }
    public int getVehicles_id() {
        return vehicles_id;
    }
    public void setVehicles_id(int vehicles_id) {
        this.vehicles_id = vehicles_id;
    }

    public Expenses() {
    }
    public Expenses(String fecha, float cuantia, String concepto, int categoria, int tipoDeGasto) {
        this.fecha = fecha;
        this.cuantia = cuantia;
        this.concepto = concepto;
        this.categoria = categoria;
        this.tipoDeGasto = tipoDeGasto;
    }
    public Expenses(String fecha, float cuantia, String concepto, int categoria, int tipoDeGasto, int vehicles_id) {
        this.fecha = fecha;
        this.cuantia = cuantia;
        this.concepto = concepto;
        this.categoria = categoria;
        this.tipoDeGasto = tipoDeGasto;
        this.vehicles_id = vehicles_id;
    }
    public Expenses(int id, String fecha, float cuantia, String concepto, int categoria, int tipoDeGasto) {
        this.id = id;
        this.fecha = fecha;
        this.cuantia = cuantia;
        this.concepto = concepto;
        this.categoria = categoria;
        this.tipoDeGasto = tipoDeGasto;
    }
    public Expenses(int id, String fecha, float cuantia, String concepto, int categoria, int tipoDeGasto,
                    int vehicles_id) {
        this.id = id;
        this.fecha = fecha;
        this.cuantia = cuantia;
        this.concepto = concepto;
        this.categoria = categoria;
        this.tipoDeGasto = tipoDeGasto;
        this.vehicles_id = vehicles_id;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Expenses other = (Expenses) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
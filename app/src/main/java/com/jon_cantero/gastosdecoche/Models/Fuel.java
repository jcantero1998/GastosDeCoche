package com.jon_cantero.gastosdecoche.Models;

import java.io.Serializable;

public class Fuel implements Serializable {
    private int id;
    private String fecha;
    private float cuantia;
    private float litros;
    private float kilometros;
    private float consumoMedioAnterior;
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
    public float getLitros() {
        return litros;
    }
    public void setLitros(float litros) {
        this.litros = litros;
    }
    public float getKilometros() {
        return kilometros;
    }
    public void setKilometros(float kilometros) {
        this.kilometros = kilometros;
    }
    public float getConsumoMedioAnterior() {
        return consumoMedioAnterior;
    }
    public void setConsumoMedioAnterior(float consumoMedioAnterior) {
        this.consumoMedioAnterior = consumoMedioAnterior;
    }
    public int getVehicles_id() {
        return vehicles_id;
    }
    public void setVehicles_id(int vehicles_id) {
        this.vehicles_id = vehicles_id;
    }

    public Fuel() {
    }
    public Fuel(String fecha, float cuantia, float litros, float kilometros, float consumoMedioAnterior,
                int vehicles_id) {
        this.fecha = fecha;
        this.cuantia = cuantia;
        this.litros = litros;
        this.kilometros = kilometros;
        this.consumoMedioAnterior = consumoMedioAnterior;
        this.vehicles_id = vehicles_id;
    }
    public Fuel(String fecha, float cuantia, float litros, float kilometros, float consumoMedioAnterior) {
        this.fecha = fecha;
        this.cuantia = cuantia;
        this.litros = litros;
        this.kilometros = kilometros;
        this.consumoMedioAnterior = consumoMedioAnterior;
    }
    public Fuel(int id, String fecha, float cuantia, float litros, float kilometros, float consumoMedioAnterior) {
        this.id = id;
        this.fecha = fecha;
        this.cuantia = cuantia;
        this.litros = litros;
        this.kilometros = kilometros;
        this.consumoMedioAnterior = consumoMedioAnterior;
    }
    public Fuel(int id, String fecha, float cuantia, float litros, float kilometros, float consumoMedioAnterior,
                int vehicles_id) {
        this.id = id;
        this.fecha = fecha;
        this.cuantia = cuantia;
        this.litros = litros;
        this.kilometros = kilometros;
        this.consumoMedioAnterior = consumoMedioAnterior;
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
        Fuel other = (Fuel) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
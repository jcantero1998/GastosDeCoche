package com.jon_cantero.gastosdecoche.Models;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private int id;
    private String modelo;
    private String marca;
    private int tipo;
    private String itv;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public int getTipo() {
        return tipo;
    }
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    public String getItv() {
        return itv;
    }
    public void setItv(String itv) {
        this.itv = itv;
    }

    public Vehicle() {
    }
    public Vehicle(String modelo, String marca, int tipo, String itv) {
        this.modelo = modelo;
        this.marca = marca;
        this.tipo = tipo;
        this.itv = itv;
    }
    public Vehicle(int id, String modelo, String marca, int tipo, String itv) {
        this.id = id;
        this.modelo = modelo;
        this.marca = marca;
        this.tipo = tipo;
        this.itv = itv;
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
        Vehicle other = (Vehicle) obj;
        if (id != other.id)
            return false;
        return true;
    }

}

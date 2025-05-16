package com.crystal.bazarposmobile.common;

public class Buscador {
    String dato;
    int posicion;

    public Buscador(String dato, int posicion) {
        this.dato = dato;
        this.posicion = posicion;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return "Buscador{" +
                "dato='" + dato + '\'' +
                ", posicion=" + posicion +
                '}';
    }
}

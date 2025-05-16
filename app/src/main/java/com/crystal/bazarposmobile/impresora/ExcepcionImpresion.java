package com.crystal.bazarposmobile.impresora;

public class ExcepcionImpresion extends Exception{

    public ExcepcionImpresion(String mensaje){
        super(mensaje);
    }
    public ExcepcionImpresion(String mensaje, Throwable interna){
        super(mensaje, interna);
    }
}

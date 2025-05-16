package com.crystal.bazarposmobile.impresora;

import android.graphics.Bitmap;

import java.io.IOException;

public interface ManejadorImpresora {
    //Alineación
    public static final int ALINEACION_IZQUIERDA = 0;
    public static final int ALINEACION_DERECHA = 2;
    public static final int ALINEACION_CENTRO = 1;

    //Comandos Código de barras
    public static final byte[] SET_WIDTH_NORMAL = {0x02};
    public static final byte[] SET_WIDTH_ANCHO = {0x03};

    //Comando de la impresora Sunmin
    public static final int SIN_SEPARACION = 1;
    //Comando de la impresora Imin
    public static final int COMANDO_NEGRITA = 6;

    //Comandos de formato texto impresora predeterminada
    public static final int COMANDO_TXT_NORMAL = 0; //TODO texto normal
    public static final int COMANDO_TXT_DOBLE_ALTURA = 1; //TODO texto doble altura
    public static final int COMANDO_TXT_DOBLE_ANCHO = 2;  //TODO texto doble ancho
    public static final int COMANDO_TXT_DOBLE_ANCHO_ALTO = 3; //TODO texto doble ancho y alto
    public static final int COMANDO_FUENTE_PEQUEÑA = 4; //TODO fuente pequeña

    public void inicializar();
    public void alinear(int alineacion) throws ExcepcionImpresion;
    public void saltarLinea() throws ExcepcionImpresion;
    public void imprimirTexto(String texto, float tamano, boolean resaltar, boolean subrayar) throws ExcepcionImpresion;
    public void imprimirColumnas(Object[] textos, int[] anchos, int[] alineaciones, int[] tamanos, boolean resaltar) throws ExcepcionImpresion;
    public void cortarPapel() throws ExcepcionImpresion;
    public void alimentaPapel() throws ExcepcionImpresion;
    public void abrirCajon() throws ExcepcionImpresion;
    public void abrirConexion() throws ExcepcionImpresion;
    public void confirmarComando() throws ExcepcionImpresion;
    public void iniciarImpresion() throws ExcepcionImpresion, IOException;
    public void imprimirSeparador() throws ExcepcionImpresion;
    public void imprimirCodigoBarras(String codigoBarras, byte[] ancho) throws ExcepcionImpresion;
    public void desconectar() throws ExcepcionImpresion;
    public void comandoFormatoTexto(int comando) throws ExcepcionImpresion;
    public boolean isConnected() throws ExcepcionImpresion;
    public void imprimirBitmap(Bitmap bitmap) throws ExcepcionImpresion;
    public void obtenerConeccionGlobal(String mac) throws ExcepcionImpresion;
    public void getConnection() throws ExcepcionImpresion;
    public void imprimirCodigoBarrasSunmi(String texto, int Simbologia, int alto, int ancho, int posicionTexto) throws ExcepcionImpresion;
    public void imprimirColumnasSunmi(String[] textos, int[] anchos, int[] alineaciones, boolean resaltar, float tamano) throws ExcepcionImpresion;
    public void imprimirColumnasImin(String[] textos, int[] anchos, int[] alineaciones, int[] tamanos, boolean resaltar) throws ExcepcionImpresion;
    public void imprimirCodigoBarrasImin(String codigoBarras, byte[] ancho) throws ExcepcionImpresion;
    public void imprimirQR(String texto, int tamano) throws ExcepcionImpresion;
}

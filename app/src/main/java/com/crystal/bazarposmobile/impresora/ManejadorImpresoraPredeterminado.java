package com.crystal.bazarposmobile.impresora;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Bitmap;

import com.crystal.bazarposmobile.common.BluetoothImpresora;
import com.crystal.bazarposmobile.ui.ImpresionActivity;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;

import java.nio.charset.StandardCharsets;

public class ManejadorImpresoraPredeterminado implements ManejadorImpresora{
    private static final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
    private static final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
    private static final byte[] ALIGN_RIGHT = {0x1B, 0x61, 2};
    private static final byte[] LINE_FEED = {0x0A};
    private static final byte[] PAPER_CUT = {0x1D, 0x56, 0x1};
    private static final byte[] PAPER_FEED = {27, 0x4A, (byte) 0xFF};
    private static final byte[] BOLD_ON = {0x1B, 0x45, 1};
    private static final byte[] BOLD_OFF = {0x1B, 0x45, 0};
    private static final byte[] ABRIR_CAJON = {0x1b, 0x70, 0x00, 0x19, (byte)0xFA};
    private static final byte[] FLUSH_COMMAND = {(byte) 0xFF, 0x0C};
    private static final byte[] INIT = {0x1B, 0x40};
    private static final String CHARTSETNAME = "Cp858";

    //Comandos de formato texto
    private static final byte[] COMANDO_TXT_NORMAL = {0x1b, 0x21, 0x00}; //texto normal
    private static final byte[] COMANDO_TXT_DOBLE_ALTURA = {0x1b, 0x21, 0x10}; //texto doble altura
    private static final byte[] COMANDO_TXT_DOBLE_ANCHO= {0x1b, 0x21, 0x20};  //texto doble ancho
    private static final byte[] COMANDO_TXT_DOBLE_ANCHO_ALTO = {0x1b, 0x21, 0x30}; //texto doble ancho y alto
    private static final byte[] COMANDO_FUENTE_PEQUEÑA = {0x1b, 0x4d, 0x01}; //fuente pequeña

    byte[] GS_COMMAND = {0x1D};
    byte[] GET_HEIGHT_COMMAND_CODE = {0x68};
    byte[] SET_HEIGHT = {0x5A};
    byte[] GET_WIDTH_COMMAND_CODE = {0x77};
    byte[] GET_PRINT_CODE_COMMAND = {0x6B};
    byte[] GET_BARCODE_SYSTEM = {0x45};

    private static final byte[] LINE_PUN = new byte [] {
            0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D };

    private static final String MENSAJE_ERROR_IMPRESION = "Error imprimiendo en zebra";
    //TODO establecer
    private String mac;
    private String ip;
    private int puerto;
    private boolean isBluetooth;

    private static BluetoothImpresora instance = null;
    private Connection impresora;
    private BluetoothAdapter mBtAdapter;
    private BluetoothImpresora coneccionGlobal;

    public ManejadorImpresoraPredeterminado(String mac, String ip, int puerto, boolean isBluetooth) {
        this.mac = mac;
        this.ip = ip;
        this.puerto = puerto;
        this.isBluetooth = isBluetooth;
    }

    @Override
    public void inicializar(){
        if(isBluetooth){
            impresora = new BluetoothConnection(mac);
            coneccionGlobal = BluetoothImpresora.getInstance(mac);
            coneccionGlobal.setConnection(impresora);
        }else{
            impresora = new TcpConnection(ip, puerto);
        }
    }

    @Override
    public void alinear(int alineacion) throws ExcepcionImpresion{
        try {
            switch (alineacion){
                case ManejadorImpresora.ALINEACION_DERECHA:
                    impresora.write(ALIGN_RIGHT);
                    break;
                case ManejadorImpresora.ALINEACION_CENTRO:
                    impresora.write(ALIGN_CENTER);
                    break;
                default:
                    impresora.write(ALIGN_LEFT);
                    break;
            }
        }catch (ConnectionException e){
            throw new ExcepcionImpresion(MENSAJE_ERROR_IMPRESION, e);
        }
    }

    @Override
    public void saltarLinea() throws ExcepcionImpresion{
        imprimir(LINE_FEED);
    }

    @Override
    public void imprimirTexto(String texto, float tamano, boolean resaltar, boolean subrayar) throws ExcepcionImpresion{
        try {
            imprimir(resaltar ? BOLD_ON : BOLD_OFF);
            imprimir(texto.getBytes(CHARTSETNAME));
        }catch (Exception e){
            throw new ExcepcionImpresion("Error general imprimiendo", e);
        }
    }

    @Override
    public void imprimirColumnas(Object[] textos, int[] anchos, int[] alineaciones, int[] tamanos, boolean resaltar) throws ExcepcionImpresion{
        StringBuilder formato = new StringBuilder();
        for (int i=0; i < textos.length;i++){
            formato.append("%");
            formato.append(String.valueOf(i+1));
            formato.append("$");
            if (alineaciones[i] == ManejadorImpresora.ALINEACION_IZQUIERDA){
                formato.append("-");
            }
            formato.append(String.valueOf(anchos[i]));
            formato.append("s ");
        }
        imprimirTexto(String.format(formato.toString(), textos), 0,resaltar ,false );
    }

    @Override
    public void cortarPapel() throws ExcepcionImpresion {
        imprimir(PAPER_CUT);
    }

    @Override
    public void alimentaPapel() throws ExcepcionImpresion {
        imprimir(PAPER_FEED);
    }

    @Override
    public void abrirCajon() throws ExcepcionImpresion {
        imprimir(ABRIR_CAJON);
    }

    @Override
    public void abrirConexion() throws ExcepcionImpresion {
        try{
            if(impresora != null){
                impresora.open();
            }
        }catch (ConnectionException e){
            throw new ExcepcionImpresion(MENSAJE_ERROR_IMPRESION, e);
        }
    }

    @Override
    public void confirmarComando() throws ExcepcionImpresion {
        imprimir(FLUSH_COMMAND);
    }

    @Override
    public void iniciarImpresion() throws ExcepcionImpresion {
        imprimir(INIT);
    }

    @Override
    public void imprimirSeparador() throws ExcepcionImpresion {
        imprimir(LINE_PUN);
    }

    @Override
    public void imprimirCodigoBarras(String codigoBarras, byte[] ancho) throws ExcepcionImpresion {
        try {
            byte[] GET_BARCODE_LENGTH = {ImpresionActivity.ByteHelper.commandByteRepresentation(codigoBarras.length())};

            imprimir(GS_COMMAND);
            imprimir(GET_HEIGHT_COMMAND_CODE);
            imprimir(SET_HEIGHT);
            imprimir(GS_COMMAND);
            imprimir(GET_WIDTH_COMMAND_CODE);
            imprimir(ancho);
            imprimir(GS_COMMAND);
            imprimir(GET_PRINT_CODE_COMMAND);
            imprimir(GET_BARCODE_SYSTEM);
            imprimir(GET_BARCODE_LENGTH);
            for (int i = 0; i < codigoBarras.length(); i++) {
                imprimir((codigoBarras.charAt(i) + "").getBytes(CHARTSETNAME));
            }
        }catch (Exception e){
            throw new ExcepcionImpresion("Error general código de barras", e);
        }
    }

    @Override
    public void desconectar() throws ExcepcionImpresion {
        try{
            if(impresora != null){
                impresora.close();
                impresora = null;
                if (isBluetooth) {
                    coneccionGlobal.setConnection(null);
                }
            }
        }catch (ConnectionException e){
            throw new ExcepcionImpresion(MENSAJE_ERROR_IMPRESION, e);
        }
    }

    @Override
    public void comandoFormatoTexto(int comando) throws ExcepcionImpresion {
        switch (comando){
            case ManejadorImpresora.COMANDO_TXT_DOBLE_ALTURA:
                imprimir(COMANDO_TXT_DOBLE_ALTURA);
                break;
            case ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO_ALTO:
                imprimir(COMANDO_TXT_DOBLE_ANCHO_ALTO);
                break;
            case ManejadorImpresora.COMANDO_TXT_DOBLE_ANCHO:
                imprimir(COMANDO_TXT_DOBLE_ANCHO);
                break;
            case ManejadorImpresora.COMANDO_TXT_NORMAL:
                imprimir(COMANDO_TXT_NORMAL);
                break;
            default:
                imprimir(COMANDO_FUENTE_PEQUEÑA);
                break;
        }
    }

    @Override
    public boolean isConnected() throws ExcepcionImpresion {
        if(impresora != null){
            return impresora.isConnected();
        }else{
            return false;
        }
    }

    @Override
    public void imprimirBitmap(Bitmap bitmap) throws ExcepcionImpresion {

    }

    @Override
    public void obtenerConeccionGlobal(String mac) throws ExcepcionImpresion {
        coneccionGlobal = BluetoothImpresora.getInstance(mac);
    }

    @Override
    public void getConnection() throws ExcepcionImpresion {
        impresora = coneccionGlobal.getConnection();
    }

    @Override
    public void imprimirCodigoBarrasSunmi(String texto, int Simbologia, int alto, int ancho, int posicionTexto) throws ExcepcionImpresion {

    }

    @Override
    public void imprimirColumnasSunmi(String[] textos, int[] anchos, int[] alineaciones, boolean resaltar, float tamano) throws ExcepcionImpresion {

    }

    @Override
    public void imprimirColumnasImin(String[] textos, int[] anchos, int[] alineaciones, int[] tamanos, boolean resaltar) throws ExcepcionImpresion {

    }

    @Override
    public void imprimirCodigoBarrasImin(String codigoBarras, byte[] ancho) throws ExcepcionImpresion {

    }

    @Override
    public void imprimirQR(String texto, int tamano) throws ExcepcionImpresion {
        try {
            // tamaño del módulo que esté dentro del rango permitido (1-16)
            int qrTamanoModulo = Math.min(16, Math.max(1, tamano / 10));
            byte[] qrModuleSize = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, (byte) qrTamanoModulo};
            imprimir(qrModuleSize);

            // Configurar el nivel de corrección de errores (0x30 = L, 0x31 = M, 0x32 = Q, 0x33 = H)
            byte[] qrErrorCorrection = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x30};
            imprimir(qrErrorCorrection);

            // Dividir el texto en bloques
            byte[] textoBytes = texto.getBytes(StandardCharsets.ISO_8859_1);
            int longitudTexto = textoBytes.length;
            byte pL = (byte) (longitudTexto + 3);
            byte pH = 0x00;

            byte[] qrStoreData = new byte[8 + longitudTexto];
            qrStoreData[0] = 0x1D;
            qrStoreData[1] = 0x28;
            qrStoreData[2] = 0x6B;
            qrStoreData[3] = pL;
            qrStoreData[4] = pH;
            qrStoreData[5] = 0x31;
            qrStoreData[6] = 0x50;
            qrStoreData[7] = 0x30;
            System.arraycopy(textoBytes, 0, qrStoreData, 8, longitudTexto);

            imprimir(qrStoreData);

            // Imprimir el código QR
            byte[] qrPrint = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30};
            imprimir(qrPrint);

        } catch (Exception e) {
            throw new ExcepcionImpresion("Error general imprimiendo QR", e);
        }
    }

    private void imprimir(byte[] control) throws  ExcepcionImpresion {
        try{
            impresora.write(control);
        }catch (ConnectionException e){
            throw new ExcepcionImpresion(MENSAJE_ERROR_IMPRESION, e);
        }
    }

}

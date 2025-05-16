package com.crystal.bazarposmobile.impresora;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.crystal.bazarposmobile.ui.ImpresionActivity;
import com.imin.printerlib.IminPrintUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ManejadorImpresoraImin implements ManejadorImpresora{
    //Comandos de impresion
    private static final byte[] LINE_FEED = {0x0A};
    byte[] GS_COMMAND = {0x1D};
    byte[] GET_HEIGHT_COMMAND_CODE = {0x68};
    byte[] SET_HEIGHT = {0x5A};
    byte[] GET_WIDTH_COMMAND_CODE = {0x77};
    byte[] GET_PRINT_CODE_COMMAND = {0x6B};
    byte[] GET_BARCODE_SYSTEM = {0x45};

    //TODO establecer
    private Context context;
    private BluetoothDevice device;
    private BluetoothAdapter mBluetoothAdapter;
    private IminPrintUtils impresora;

    //Comandos de impresion
    private static final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};

    public ManejadorImpresoraImin(Context context){
        this.context = context;
    }

    @Override
    public void inicializar() {
        impresora = IminPrintUtils.getInstance(context);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        device = mBluetoothAdapter.getRemoteDevice("00:11:22:33:44:55");
    }

    @Override
    public void alinear(int alineacion) throws ExcepcionImpresion {
        switch (alineacion){
            case ALINEACION_CENTRO:
                impresora.setAlignment(1);
                break;
            case ALINEACION_DERECHA:
                impresora.setAlignment(2);
                break;
            default:
                impresora.setAlignment(0);
                break;
        }
    }

    @Override
    public void saltarLinea() throws ExcepcionImpresion {
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }
        try {
            impresora.printText("\n");
        } catch (Exception e) {
            throw new ExcepcionImpresion("Error general código de barras", e);
        }
    }

    @Override
    public void imprimirTexto(String texto, float tamano, boolean resaltar, boolean subrayar) throws ExcepcionImpresion {
        try {
            if(resaltar){
                impresora.setTextStyle(Typeface.BOLD);
            }else {
                impresora.setTextStyle(Typeface.NORMAL);
            }
            impresora.setTextSize((int) tamano);
            impresora.printText(texto);
        }catch (Exception e){
            throw new ExcepcionImpresion("Error general imprimiendo", e);
        }
    }

    @Override
    public void imprimirColumnas(Object[] textos, int[] anchos, int[] alineaciones, int[] tamanos, boolean resaltar) throws ExcepcionImpresion {

    }

    @Override
    public void cortarPapel() throws ExcepcionImpresion {

    }

    @Override
    public void alimentaPapel() throws ExcepcionImpresion {
        impresora.printAndFeedPaper(120);
    }

    @Override
    public void abrirCajon() throws ExcepcionImpresion {

    }

    @Override
    public void abrirConexion() throws ExcepcionImpresion {

    }

    @Override
    public void confirmarComando() throws ExcepcionImpresion {

    }

    @Override
    public void iniciarImpresion() throws ExcepcionImpresion {
        try {
            impresora.initPrinter(IminPrintUtils.PrintConnectType.BLUETOOTH, device);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirSeparador() throws ExcepcionImpresion {

    }

    @Override
    public void imprimirCodigoBarras(String codigoBarras, byte[] ancho) throws ExcepcionImpresion {
        try {
            if (ancho == ManejadorImpresora.SET_WIDTH_ANCHO) {
                impresora.setBarCodeWidth(3);
            } else if (ancho == ManejadorImpresora.SET_WIDTH_NORMAL) {
                impresora.setBarCodeWidth(2);
            }
            impresora.setBarCodeContentPrintPos(2);
            impresora.setBarCodeContentPrintPos(0);
            impresora.printBarCode(4, codigoBarras, ManejadorImpresora.ALINEACION_CENTRO);
        }catch (Exception e){
            throw new ExcepcionImpresion("Error general código de barras", e);
        }
    }

    @Override
    public void desconectar() throws ExcepcionImpresion {

    }

    @Override
    public void comandoFormatoTexto(int comando) throws ExcepcionImpresion {
        if(comando == ManejadorImpresora.COMANDO_TXT_NORMAL){
            impresora.setTextTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public boolean isConnected() throws ExcepcionImpresion {
        return false;
    }

    @Override
    public void imprimirBitmap(Bitmap bitmap) throws ExcepcionImpresion {
        impresora.printSingleBitmap(bitmap);
    }

    @Override
    public void obtenerConeccionGlobal(String mac) throws ExcepcionImpresion {

    }

    @Override
    public void getConnection() throws ExcepcionImpresion {

    }

    @Override
    public void imprimirCodigoBarrasSunmi(String texto, int Simbologia, int alto, int ancho, int posicionTexto) throws ExcepcionImpresion {

    }

    @Override
    public void imprimirColumnasSunmi(String[] textos, int[] anchos, int[] alineaciones, boolean resaltar, float tamano) throws ExcepcionImpresion {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void imprimirColumnasImin(String[] textos, int[] anchos, int[] alineaciones, int[] tamanos, boolean resaltar) throws ExcepcionImpresion {
        try {
            if(resaltar){
                impresora.setTextStyle(Typeface.BOLD);
            }else {
                impresora.setTextStyle(Typeface.NORMAL);
            }
            impresora.printColumnsText(textos, anchos, alineaciones, tamanos);
        }catch (Exception e){
            throw new ExcepcionImpresion("Error general imprimiendo", e);
        }
    }

    @Override
    public void imprimirCodigoBarrasImin(String codigoBarras, byte[] ancho) throws ExcepcionImpresion {
        try {
            byte[] alignCenter = {0x1B, 0x61, 0x01}; // ESC a 1 (Centro)
            impresora.sendRAWData(alignCenter);
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
                impresora.sendRAWData((codigoBarras.charAt(i) + "").getBytes());
            }
        }catch (Exception e){
            throw new ExcepcionImpresion("Error general código de barras", e);
        }
    }

    @Override
    public void imprimirQR(String texto, int tamano) throws ExcepcionImpresion {
        try {
            byte[] alignCenter = {0x1B, 0x61, 0x01}; // ESC a 1 (Centro)
            impresora.sendRAWData(alignCenter);

            // Tamaño del módulo QR (1-16)
            int qrTamanoModulo = Math.min(16, Math.max(1, tamano / 10));

            // Comando para establecer el tamaño del módulo QR
            byte[] qrModuleSize = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, (byte) qrTamanoModulo};
            impresora.sendRAWData(qrModuleSize);

            // Comando para establecer la corrección de errores (0 - baja, 1 - media, 2 - alta, 3 - mejor)
            byte[] qrErrorCorrection = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x30};
            impresora.sendRAWData(qrErrorCorrection);

            // Texto a imprimir en QR
            byte[] textoBytes = texto.getBytes(StandardCharsets.ISO_8859_1);
            int longitudTexto = textoBytes.length;
            byte pL = (byte) (longitudTexto + 3);
            byte pH = 0x00;

            // Comando para almacenar los datos del QR
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

            impresora.sendRAWData(qrStoreData);

            // Comando para imprimir el código QR almacenado
            byte[] qrPrint = {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30};
            impresora.sendRAWData(qrPrint);

        } catch (Exception e) {
            throw new ExcepcionImpresion("Error general imprimiendo QR", e);
        }
    }


    private void imprimir(byte[] control) throws  ExcepcionImpresion {
        impresora.sendRAWData(control);
    }
}

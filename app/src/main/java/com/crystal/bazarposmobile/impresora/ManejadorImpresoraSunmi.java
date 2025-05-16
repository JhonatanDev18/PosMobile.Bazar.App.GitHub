package com.crystal.bazarposmobile.impresora;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;

import com.crystal.bazarposmobile.utilsSunmi.ESCUtil;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.WoyouConsts;

public class ManejadorImpresoraSunmi implements ManejadorImpresora{
    //Comandos de impresion
    private static final byte[] LINE_FEED = {0x0A};
    private static final byte[] SIN_SEPARACION = {0x1B, 0x33, 20};

    byte[] LINEA_PUN = "--------------------------------------".getBytes();

    public static int NoSunmiPrinter = 0x00000000;
    public static int CheckSunmiPrinter = 0x00000001;
    public static int FoundSunmiPrinter = 0x00000002;
    public static int LostSunmiPrinter = 0x00000003;

    public int sunmiPrinter = CheckSunmiPrinter;
    private SunmiPrinterService impresora;

    //TODO establecer
    private Context context;
    private int tamanoTexto = 20;

    private static ManejadorImpresoraSunmi helper = new ManejadorImpresoraSunmi();

    public ManejadorImpresoraSunmi(Context context) {
        this.context = context;
    }

    public ManejadorImpresoraSunmi() {
    }

    public static ManejadorImpresoraSunmi getInstance() {
        return helper;
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            Log.e("logcat","onConnected");
            impresora = service;
            checkSunmiPrinterService(service);
        }

        @Override
        protected void onDisconnected() {
            Log.e("logcat","onDisconnected");
            impresora = null;
            sunmiPrinter = LostSunmiPrinter;
        }
    };

    private void checkSunmiPrinterService(SunmiPrinterService service){
        boolean ret = false;
        try {
            ret = InnerPrinterManager.getInstance().hasPrinter(service);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
        sunmiPrinter = ret?FoundSunmiPrinter:NoSunmiPrinter;
    }

    private static final String MENSAJE_ERROR_IMPRESION = "Error imprimiendo en zebra";

    @Override
    public void inicializar() {
        try {
            boolean ret =  InnerPrinterManager.getInstance().bindService(context,
                    innerPrinterCallback);
            if(!ret){
                sunmiPrinter = NoSunmiPrinter;
            }
        } catch (InnerPrinterException e) {
            e.printStackTrace();
            Log.e("logcat","e.printStackTrace()");
        }
    }

    @Override
    public void alinear(int alineacion) throws ExcepcionImpresion {
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }
        try {
            switch (alineacion){
                case ManejadorImpresora.ALINEACION_DERECHA:
                    impresora.setAlignment(2, null);
                    break;
                case ManejadorImpresora.ALINEACION_CENTRO:
                    impresora.setAlignment(1, null);
                    break;
                default:
                    impresora.setAlignment(0, null);
                    break;
            }
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    @Override
    public void saltarLinea() throws ExcepcionImpresion {
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }
        try {
            impresora.sendRAWData(LINE_FEED, null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    @Override
    public void imprimirTexto(String texto, float tamano, boolean resaltar, boolean subrayar) throws ExcepcionImpresion {
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }

        try {
            try {
                impresora.setPrinterStyle(WoyouConsts.ENABLE_BOLD, resaltar?
                        WoyouConsts.ENABLE: WoyouConsts.DISABLE);
            } catch (RemoteException e) {
                if (resaltar) {
                    impresora.sendRAWData(ESCUtil.boldOn(), null);
                } else {
                    impresora.sendRAWData(ESCUtil.boldOff(), null);
                }
            }
            try {
                impresora.setPrinterStyle(WoyouConsts.ENABLE_UNDERLINE, subrayar?
                        WoyouConsts.ENABLE: WoyouConsts.DISABLE);
            } catch (RemoteException e) {
                if (subrayar) {
                    impresora.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
                } else {
                    impresora.sendRAWData(ESCUtil.underlineOff(), null);
                }
            }
            impresora.printTextWithFont(texto, null, tamano, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirColumnas(Object[] textos, int[] anchos, int[] alineaciones, int[] tamanos, boolean resaltar) throws ExcepcionImpresion {

    }

    public void cambiarTamañoTextoColumnas(int tamano){
        tamanoTexto = tamano;
    }

    @Override
    public void cortarPapel() throws ExcepcionImpresion {

    }

    @Override
    public void alimentaPapel() throws ExcepcionImpresion {
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }

        try {
            impresora.autoOutPaper(null);
        } catch (RemoteException e) {
            print3Line();
        }
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

    }

    @Override
    public void imprimirSeparador() throws ExcepcionImpresion {
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }
        try {
            impresora.setFontSize(20, null);
            impresora.sendRAWData(LINEA_PUN, null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    @Override
    public void imprimirCodigoBarras(String codigoBarras, byte[] ancho) throws ExcepcionImpresion {
    }

    @Override
    public void desconectar() throws ExcepcionImpresion {

    }

    @Override
    public void comandoFormatoTexto(int comando) throws ExcepcionImpresion {
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }
        try {
            if (comando == ManejadorImpresora.SIN_SEPARACION) {
                impresora.sendRAWData(SIN_SEPARACION, null);
            }
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    @Override
    public boolean isConnected() throws ExcepcionImpresion {
        return false;
    }

    @Override
    public void imprimirBitmap(Bitmap bitmap) throws ExcepcionImpresion {

    }

    @Override
    public void obtenerConeccionGlobal(String mac) throws ExcepcionImpresion {

    }

    @Override
    public void getConnection() throws ExcepcionImpresion {

    }

    @Override
    public void imprimirCodigoBarrasSunmi(String texto, int Simbologia, int alto, int ancho, int posicionTexto) throws ExcepcionImpresion {
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }

        try {
            impresora.printBarCode(texto, Simbologia, alto, ancho, posicionTexto, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirColumnasSunmi(String[] textos, int[] anchos, int[] alineaciones, boolean resaltar, float tamano) throws ExcepcionImpresion {
        try {
            impresora.setPrinterStyle(WoyouConsts.ENABLE_BOLD, resaltar?
                    WoyouConsts.ENABLE: WoyouConsts.DISABLE);
            impresora.setFontSize(tamano, null);
            impresora.printColumnsString(textos, anchos, alineaciones, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
            // Alinear el QR al centro
            impresora.setAlignment(1, null);

            // Imprimir el QR con el tamaño especificado
            impresora.printQRCode(texto, tamano, 0, null);

        } catch (RemoteException e) {
            handleRemoteException(e);
            throw new ExcepcionImpresion("Error imprimiendo el código QR", e);
        }
    }

    private void handleRemoteException(RemoteException e){
        //TODO process when get one exception
    }

    public void print3Line(){
        if(impresora == null){
            //TODO Service disconnection processing
            return;
        }

        try {
            impresora.lineWrap(3, null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }
}

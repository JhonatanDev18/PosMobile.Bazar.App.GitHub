package com.crystal.bazarposmobile.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class LectorCodigosCamaraActivity
        extends
        AppCompatActivity
        implements
        DecoratedBarcodeView.TorchListener {

    //Declaración de los objetos de la interfaz del DialogFragment
    private ImageView btnflash;

    //Declaración de la variables del activity
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean flash_encendido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_codigos_camara);

        findViews();
        events();

        //start capture
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        flash_encendido = SPM.getBoolean(Constantes.LINTERNA);
        if(flash_encendido){
            barcodeScannerView.setTorchOn();
        }else{
            barcodeScannerView.setTorchOff();
        }
    }

    //Asignacion de Referencias
    private void findViews() {
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        btnflash = findViewById(R.id.switch_flashlight);
    }

    //Asignacion de eventos
    private void events() {
        barcodeScannerView.setTorchListener(this);
        if (!usandoFlash()) {
            btnflash.setVisibility(View.GONE);
        } else {
            btnflash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cambiar_flash();
                }
            });
        }
    }

    private boolean usandoFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void cambiar_flash() {
        if (flash_encendido) {
            barcodeScannerView.setTorchOff();
            flash_encendido = false;
        } else {
            barcodeScannerView.setTorchOn();
            flash_encendido = true;
        }
        SPM.setBoolean(Constantes.LINTERNA,flash_encendido);
    }

    @Override
    public void onTorchOn() {
        btnflash.setBackgroundResource(R.drawable.ic_baseline_flash_on_60);
    }

    @Override
    public void onTorchOff() {
        btnflash.setBackgroundResource(R.drawable.ic_baseline_flash_off_60);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}

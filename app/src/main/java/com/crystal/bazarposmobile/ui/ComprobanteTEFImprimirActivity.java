package com.crystal.bazarposmobile.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.ui.fragment.ComprobanteTEFGripFragment;
import com.crystal.bazarposmobile.ui.fragment.ProductoGripFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComprobanteTEFImprimirActivity extends AppCompatActivity implements ComprobanteTEFGripFragment.OnLFIComprobanteTEF {

    //Declaración de los objetos de la interfaz del activity
    ComprobanteTEFGripFragment comprobanteTEFGrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobante_tef_imprimir);
        findViews();
        new ComprbanteAsyncTask().execute();
    }

    private void findViews() {
        //Se inicializa el adapter aqui para evitar el error RecyclerView: No adapter attached; skipping layout
        comprobanteTEFGrip = (ComprobanteTEFGripFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentCTEFIA);
    }

    @Override
    public void OnLFILDocDatafonoEntity(DatafonoEntity mItem, int p) {

        //Pasar a la impresión del comprobante
        Intent i;
        i = Utilidades.activityImprimir(ComprobanteTEFImprimirActivity.this);
        i.putExtra("lablePrint", (Serializable) "CompTEF");
        i.putExtra("respDatafono", mItem);
        i.putExtra("primeraimpresion", false);
        startActivity(i);
    }

    protected class ComprbanteAsyncTask extends AsyncTask<Void,Void, List<DatafonoEntity>> {

        @Override
        protected List<DatafonoEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).datafonoDao().getComprbantes();
        }

        @Override
        protected void onPostExecute(List<DatafonoEntity> datafonoEntityList) {
            if(datafonoEntityList.size() > 0){
                comprobanteTEFGrip.setDatafonoEntity(datafonoEntityList);
            }else{
                msjToast("Comprobantes TEFs no disponibles.");
            }
        }
    }

    private void msjToast(String msj) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
    }
}
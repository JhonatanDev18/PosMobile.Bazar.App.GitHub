package com.crystal.bazarposmobile.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.TEFContinguenciaEntity;
import com.crystal.bazarposmobile.ui.fragment.TEFContFragment;

import java.io.Serializable;
import java.util.List;

public class TEFContingenciaActivity
        extends
            AppCompatActivity
        implements
            View.OnClickListener,
            TEFContFragment.OnListFTEFContIL{

    //Declaración de los objetos de la interfaz del activity
    TEFContFragment tefContFG;
    ImageButton ibimprimir;
    Button btncierre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tefcontinguencia);
        this.setTitle(R.string.titulo_cierre_contingencia);
        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = this.findViewById(R.id.action_bar);
                if (toolbar!= null){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }

        findViews();
        events();
        new TEFContTodosAsyncTask().execute();
    }

    private void findViews() {
        //Se inicializa el adapter aqui para evitar el error RecyclerView: No adapter attached; skipping layout
        tefContFG = (TEFContFragment)
                getSupportFragmentManager().findFragmentById(R.id.fTEFCA);

        ibimprimir = findViewById(R.id.ibImprimirTEFCA);
        ibimprimir.setVisibility(View.GONE);
        btncierre = findViewById(R.id.btnRealizarCierreTEFCA);
        btncierre.setEnabled(false);
    }

    private void events() {
        ibimprimir.setOnClickListener(this);
        btncierre.setOnClickListener(this);
    }

    protected class TEFContTodosAsyncTask extends AsyncTask<Void,Void, List<TEFContinguenciaEntity>> {

        @Override
        protected List<TEFContinguenciaEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).tefContinguenciaDao().getAll();
        }

        @Override
        protected void onPostExecute(List<TEFContinguenciaEntity> tefContList) {
            super.onPostExecute(tefContList);
            if(tefContList.size() > 0){
                ibimprimir.setVisibility(View.VISIBLE);
                tefContFG.setPagosTEFCont(tefContList);
            }
        }
    }

    @Override
    public void onListFTEFContIL(TEFContinguenciaEntity item, Integer position) {

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.ibImprimirTEFCA:
                btncierre.setEnabled(true);
                new TEFContAllPrintAsyncTask().execute();
                break;
            case R.id.btnRealizarCierreTEFCA:
                new TEFContDeleteAsyncTask().execute();
                break;
        }
    }

    protected class TEFContAllPrintAsyncTask extends AsyncTask<Void,Void, List<TEFContinguenciaEntity>> {

        @Override
        protected List<TEFContinguenciaEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).tefContinguenciaDao().getAllPrint();
        }

        @Override
        protected void onPostExecute(List<TEFContinguenciaEntity> tefContList) {
            super.onPostExecute(tefContList);
            if(tefContList.size() > 0){
                pasarPrint(tefContList);
            }
        }
    }

    private void pasarPrint(List<TEFContinguenciaEntity> tefContList) {
        Intent i = Utilidades.activityImprimir(TEFContingenciaActivity.this);
        i.putExtra("lablePrint", (Serializable) "tef_cont_cierre_detallado");
        i.putExtra("tefContList", (Serializable) tefContList);
        startActivity(i);
    }


    private class TEFContDeleteAsyncTask extends AsyncTask<TEFContinguenciaEntity,Void,Void> {

        @Override
        protected Void doInBackground(TEFContinguenciaEntity... TEFContinguenciaEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).tefContinguenciaDao().delete();
            }catch (Exception ex){
                Log.e("LOGCAT",ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            msjToast("Cierre realiado satifactoriamente");
            finish();
        }
    }


    private void msjToast(String msj) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
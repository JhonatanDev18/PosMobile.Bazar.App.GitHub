package com.crystal.bazarposmobile.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.db.entity.SuspencionHeaderEntity;
import com.crystal.bazarposmobile.db.entity.SuspencionLineEntity;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.request.suspension.SuspesionLinea;
import com.crystal.bazarposmobile.retrofit.response.ResponseBase;
import com.crystal.bazarposmobile.retrofit.response.cliente.ResponseCliente;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.retrofit.response.suspensiones.ResponseSuspensiones;
import com.crystal.bazarposmobile.retrofit.response.suspensiones.SuspensionItem;
import com.crystal.bazarposmobile.ui.fragment.SuspensionesGripFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarTransaccionesSuspendidasActivity
        extends
            AppCompatActivity
        implements
            SuspensionesGripFragment.OnLFISuspensionesGripListener {

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME) + " - " + SPM.getString(Constantes.CAJA_CODE);

    //Declaración de los objetos de la interfaz del activity
    private ProgressBar pb;
    SuspensionesGripFragment suspensionesGF;

    //Declaración de la variables del activity
    String tienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_transacciones_suspendidas);
        this.setTitle(R.string.obtener_suspensiones);

        tienda = SPM.getString(Constantes.TIENDA_CODE);

        retrofitInit();
        findViews();
        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorModoAutonomo)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = this.findViewById(R.id.action_bar);
                if (toolbar!= null){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
            cargarSuspencionesModoAutonomo();
        }else{
            cargarSuspenciones();
        }
    }

    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    private void findViews() {
        suspensionesGF = (SuspensionesGripFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentRTSA);

        pb = findViewById(R.id.pbRTSA);
        pb.setVisibility(View.GONE);
    }

    private void cargarSuspencionesModoAutonomo() {
        new SusHeaderAllAsyncTask().execute();
    }

    protected class SusHeaderAllAsyncTask extends AsyncTask<Void,Void, List<SuspencionHeaderEntity>> {

        @Override
        protected List<SuspencionHeaderEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).suspesionDao().getHeaderAll();
        }

        @Override
        protected void onPostExecute(List<SuspencionHeaderEntity> susHeaderEntityList) {
            super.onPostExecute(susHeaderEntityList);
            if(susHeaderEntityList.size()>0){
                List<SuspensionItem> suspensionItemList = new ArrayList<>();
                for(SuspencionHeaderEntity header: susHeaderEntityList){
                    SuspensionItem item = new SuspensionItem(header.getTexto(),SPM.getString(Constantes.CLIENTE_GENERICO),header.getReferencia(),header.getCantidad());
                    suspensionItemList.add(item);
                }
                suspensionesGF.setSuspensiones(suspensionItemList);
            }else{
                mensajeSimpleDialog(getResources().getString(R.string.error),"No existen trasacciones");
            }
        }
    }

    private void cargarSuspenciones() {
        iPB();
        Call<ResponseSuspensiones> call = apiService.doObtenerSuspensiones(usuario,tienda);
        call.enqueue(new Callback<ResponseSuspensiones>() {
            @Override
            public void onResponse(Call<ResponseSuspensiones> call, Response<ResponseSuspensiones> response) {
                if(response.isSuccessful()){
                    if(response.body().isError()){
                        LogFile.adjuntarLog("Error SuspensionAgregar: " + response.body().getMensaje());
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                    }else{
                        for (int i = 0; i < response.body().getSuspensiones().size(); i++) {
                            int count = 0;
                            for(SuspesionLinea line: response.body().getSuspensiones().get(i).getLineas()){
                                count = count + line.getCantidad();
                            }
                            response.body().getSuspensiones().get(i).setCantidad(count);
                        }
                        suspensionesGF.setSuspensiones(response.body().getSuspensiones());
                    }
                }else{
                    LogFile.adjuntarLog("Error SuspensionAgregar: " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                }
                pPB();
            }

            @Override
            public void onFailure(Call<ResponseSuspensiones> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseSuspensiones: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
            }
        });
    }

    @Override
    public void OnLFILSuspensiones(SuspensionItem mItem, int p) {

        //Alerta para confirmar el cierre de medios de pagos
        AlertDialog.Builder builder = new AlertDialog.Builder(RecuperarTransaccionesSuspendidasActivity.this);
        builder.setTitle(getResources().getString(R.string.transaccion_)+" "+mItem.getReferencia())
                .setMessage(getResources().getString(R.string.recuperar_trasaccion_suspendida_)+" "+mItem.getTexto())
                .setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
                                    consumir_suspension_modo_autonomo(mItem);
                                }else{
                                    consumir_suspension(mItem);
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.volver,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void consumir_suspension_modo_autonomo(SuspensionItem mItem) {
        try {
            List<SuspencionLineEntity> susLineEntityList = new SusLineRefAsyncTask().execute(mItem.getReferencia()).get();
            new DelSusLineAsyncTask().execute(mItem.getReferencia()).get();
            new DelSusHeaderAsyncTask().execute(mItem.getReferencia()).get();

            List<SuspesionLinea> suspesionLineaList = new ArrayList<>();
            for(SuspencionLineEntity line:susLineEntityList){
                SuspesionLinea item = new SuspesionLinea(line.getEan(),line.getCantidad(),0);
                suspesionLineaList.add(item);
            }
            List<Producto> productosList = crear_lista_producto(suspesionLineaList);
            Intent i = new Intent();
            i.putExtra("productosList", (Serializable) productosList);
            i.putExtra("cliente", (Serializable) null);
            setResult(Constantes.RESP_SUSPENSIONES,i);
            finish();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected class SusLineRefAsyncTask extends AsyncTask<String,Void, List<SuspencionLineEntity>> {

        @Override
        protected List<SuspencionLineEntity> doInBackground(String... referencia) {
            return BazarPosMovilDB.getBD(getApplication()).suspesionDao().getLines(referencia[0]);
        }
    }

    private class DelSusLineAsyncTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... referencia) {
            BazarPosMovilDB.getBD(getApplication()).suspesionDao().deleteLines(referencia[0]);
            return null;
        }
    }

    private class DelSusHeaderAsyncTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... referencia) {
            BazarPosMovilDB.getBD(getApplication()).suspesionDao().deleteHeader(referencia[0]);
            return null;
        }
    }

    private void consumir_suspension(SuspensionItem mItem) {

        iPB();
        Call<ResponseBase> call = apiService.doConsumirSuspension(usuario,mItem.getReferencia());
        call.enqueue(new Callback<ResponseBase>() {
            @Override
            public void onResponse(Call<ResponseBase> call, Response<ResponseBase> response) {

                if(response.isSuccessful()){
                    if(response.body().getError()){
                        LogFile.adjuntarLog("Error ConsumirSuspension: " + response.body().getMensaje());
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                        pPB();
                    }else{
                        List<Producto> productosList = crear_lista_producto(mItem.getLineas());
                        if(mItem.getCliente().equals(SPM.getString(Constantes.CLIENTE_GENERICO))){
                            Intent i = new Intent();
                            i.putExtra("productosList", (Serializable) productosList);
                            i.putExtra("cliente", (Serializable) null);
                            setResult(Constantes.RESP_SUSPENSIONES,i);
                            finish();
                        }else{
                            consultarcliente(mItem.getCliente(),productosList);
                        }
                    }
                }else{
                    LogFile.adjuntarLog("Error ConsumirSuspension: " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                }
            }

            @Override
            public void onFailure(Call<ResponseBase> call, Throwable t) {
                LogFile.adjuntarLog("Error ConsumirSuspension: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
            }
        });
    }

    private List<Producto> crear_lista_producto(List<SuspesionLinea> lineas) {

        List<Producto> productosList = new ArrayList<>();
        for(SuspesionLinea line: lineas){
            try {
                ProductoEntity pe = new ProductoEanAsyncTask().execute(line.getEan()).get();
                Producto p = new Producto(pe.getEan(),pe.getPrecio(),pe.getNombre(),pe.getTalla(),pe.getColor(),pe.getTipoTarifa(),pe.getTienda(),
                        pe.getPeriodoTarifa(),pe.getIp(), pe.getComposicion(),pe.getPrecioUnitario(),pe.getArticulo(),pe.getCodigoTasaImpuesto(),
                        pe.getArticuloCerrado(),pe.getArticuloGratuito(),pe.getTasaImpuesto(),pe.getPrecioSinImpuesto(),pe.getImpuesto(),
                        pe.getValorTasa(),pe.getFechaTasa(),pe.getPrecioOriginal(),pe.getPeriodoActivo(),pe.getCodigoMarca(),
                        pe.getMarca(),pe.getSerialNumberId(),pe.getVendedorId(),line.getCantidad(),pe.getDescontable(),
                        pe.getTipoPrendaCodigo(),pe.getTipoPrendaNombre(),pe.getGeneroCodigo(),pe.getGeneroNombre(),
                        pe.getCategoriaIvaCodigo(),pe.getCategoriaIvaNombre(),pe.getLine());
                productosList.add(p);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return productosList;
    }


    private void consultarcliente(String cliente,final List<Producto> productosList) {

        iPB();
        Call<ResponseCliente> call = apiService.doCliente(usuario,cliente);
        call.enqueue(new Callback<ResponseCliente>() {
            @Override
            public void onResponse(Call<ResponseCliente> call, Response<ResponseCliente> response) {

                if(response.isSuccessful()){
                    if(response.body().isError()){
                        LogFile.adjuntarLog("Error ResponseCliente: " + response.body().getMensaje());
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                        pPB();
                    }else{
                        Intent i = new Intent();
                        i.putExtra("productosList", (Serializable) productosList);
                        i.putExtra("cliente", (Serializable) response.body().getCliente());
                        setResult(Constantes.RESP_SUSPENSIONES,i);
                        finish();
                    }
                }else{
                    LogFile.adjuntarLog("Error ResponseCliente: " + response.message());
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb) + response.message());
                    pPB();
                }
            }

            @Override
            public void onFailure(Call<ResponseCliente> call, Throwable t) {
                LogFile.adjuntarLog("Error ResponseCliente: " + call + t);
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                pPB();
            }
        });
    }

    private class ProductoEanAsyncTask extends AsyncTask<String,Void, ProductoEntity> {

        @Override
        protected ProductoEntity doInBackground(String... ean) {
            return BazarPosMovilDB.getBD(getApplication()).productoDao().getEAN(ean[0]);
        }
    }

    private void iPB() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);
    }

    private void pPB() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.GONE);
    }

    //Alert Dialog para mostrar mensaje de Error o alerta
    public void mensajeSimpleDialog(String titulo, String msj) {

        int icon = R.drawable.msj_alert_30;
        if(titulo.equals(getResources().getString(R.string.error))){
            icon = R.drawable.msj_error_30;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage(msj)
                .setIcon(icon)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
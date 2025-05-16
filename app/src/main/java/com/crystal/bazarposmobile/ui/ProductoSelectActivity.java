package com.crystal.bazarposmobile.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.ui.fragment.ProductoSelectGripFragment;

import java.io.Serializable;
import java.util.List;

public class ProductoSelectActivity extends AppCompatActivity implements ProductoSelectGripFragment.OnListFragmentInteractionListener{

    ProductoSelectGripFragment productoSelectGF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_select);

        this.setTitle(R.string.titulo_productos_bazar);
        getSupportActionBar().setSubtitle(R.string.select_producto);

        productoSelectGF = (ProductoSelectGripFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentPSA);


        new ProductoTodosAsyncTask().execute();
    }

    @Override
    public void onListFragmentInteraction(ProductoEntity pe, Integer position) {

        Producto p = new Producto(pe.getEan(),pe.getPrecio(),pe.getNombre(),pe.getTalla(),pe.getColor(),pe.getTipoTarifa(),pe.getTienda(),
                pe.getPeriodoTarifa(),pe.getIp(), pe.getComposicion(),pe.getPrecioUnitario(),pe.getArticulo(),pe.getCodigoTasaImpuesto(),
                pe.getArticuloCerrado(),pe.getArticuloGratuito(),pe.getTasaImpuesto(),pe.getPrecioSinImpuesto(),pe.getImpuesto(),
                pe.getValorTasa(),pe.getFechaTasa(),pe.getPrecioOriginal(),pe.getPeriodoActivo(),pe.getCodigoMarca(),
                pe.getMarca(),pe.getSerialNumberId(),pe.getVendedorId(),1,pe.getDescontable(),
                pe.getTipoPrendaCodigo(),pe.getTipoPrendaNombre(),pe.getGeneroCodigo(),pe.getGeneroNombre(),
                pe.getCategoriaIvaCodigo(),pe.getCategoriaIvaNombre(),pe.getLine());

        Intent intent= new Intent();
        intent.putExtra("producto", (Serializable) p);
        setResult(Constantes.RESP_PRODUCTO_SELECT,intent);
        finish();
    }

    private class ProductoTodosAsyncTask extends AsyncTask<Void,Void, List<ProductoEntity>>{

        @Override
        protected List<ProductoEntity> doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).productoDao().getAll();
        }

        @Override
        protected void onPostExecute(List<ProductoEntity> productos) {
            super.onPostExecute(productos);
            productoSelectGF.setProductoSelect(productos);
        }
    }
}

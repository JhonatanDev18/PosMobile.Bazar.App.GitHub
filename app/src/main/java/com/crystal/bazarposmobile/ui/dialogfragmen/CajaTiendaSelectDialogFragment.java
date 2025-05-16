package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Buscador;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.response.caja.Caja;
import com.crystal.bazarposmobile.retrofit.response.caja.ResponseCajas;
import com.crystal.bazarposmobile.ui.adapters.ListaSimpleRecyclerViewAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class CajaTiendaSelectDialogFragment extends DialogFragment {

    //Declaración del Cliente REST
    ApiService apiService;
    String usuario = SPM.getString(Constantes.USER_NAME);

    //Declaración de los objetos de la interfaz del DialogFragment
    View view;
    ProgressBar pb;
    ListView lvcajatienda;
    EditText etBuscadorCT;
    TextInputLayout tliBuscadorCT;

    //Declaración de la variables del DialogFragment
    String code;
    List<Caja> cajasList;
    List<String> cajaTiendaList = new ArrayList<>();
    List<Caja> cajasFiltroList = new ArrayList<>();
    List<String> listAuxBuscador = new ArrayList<>();
    List<String> listAuxClickBuscador = new ArrayList<>();
    List<Buscador> listAuxFiltroBuscador = new ArrayList<>();
    List<Caja> listAuxFiltro = new ArrayList<>();
    List<Caja> listAuxClickFiltroBuscador = new ArrayList<>();
    boolean isCaja = false;

    public CajaTiendaSelectDialogFragment(String Code, List<Caja> cajasList) {
        this.code = Code;
        this.cajasList = cajasList;
    }

    public interface OnInputListener{
        void sendInputItemCajaTiendaSelectDialogFragment(List<Caja> Cajas, String tiendaCaja, String code);
    }
    public OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Validar titulo
        String titulo;
        String subtitulo;
        int icon;
        if(code.equals("Caja")){
            icon = R.mipmap.caja_registradora;
            subtitulo = getResources().getString(R.string.caja);
            titulo = getResources().getString(R.string.cajas);
            isCaja = true;
        }else{
            icon = R.mipmap.tienda;
            titulo = getResources().getString(R.string.tiendas);
            subtitulo = getResources().getString(R.string.tienda);
        }
        String msjbase = getResources().getString(R.string.selecciona_n_dispositivo);
        String msjbaseFormateada = String.format(msjbase, titulo);

        builder.setTitle(titulo);
        builder.setMessage(msjbaseFormateada)
                .setIcon(icon)
                .setNegativeButton(R.string.volver, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_fragment_caja_tienda_select, null);

        //Asignacion de Referencias
        lvcajatienda = view.findViewById(R.id.lvCTSDF);
        pb = view.findViewById(R.id.pbCTSDF);
        pb.setVisibility(View.GONE);
        etBuscadorCT = view.findViewById(R.id.etBuscadorCT);
        tliBuscadorCT = view.findViewById(R.id.tliBuscadorCT);

        msjbase = getResources().getString(R.string.buscador_n_dispositivo);
        msjbaseFormateada = String.format(msjbase, subtitulo);
        tliBuscadorCT.setHint(msjbaseFormateada);

        //Validar si tengo la lista de cajas para no volver a consumir la API de Cajas
        if(cajasList.size() > 0){
            if(isCaja){
                String tienda = SPM.getString(Constantes.TIENDA_CODE);
                //for para mostrar las cajas que pertenecen a la tienda ya selecionada
                for(Caja str : cajasList)
                    if(tienda.equalsIgnoreCase(str.getCodigoTienda())) {
                        cajaTiendaList.add(str.getCodigoCaja());
                        cajasFiltroList.add(str);
                    }
            }else{
                for(Caja str : cajasList){
                    cajaTiendaList.add(str.getCodigoTienda());
                    cajasFiltroList.add(str);
                }
                Set<String> set = new HashSet<>(cajaTiendaList);
                cajaTiendaList.clear();
                cajaTiendaList.addAll(set);
                Collections.sort(cajaTiendaList);
            }

            ListaSimpleRecyclerViewAdapter arrayAdapterCajaTienda = new ListaSimpleRecyclerViewAdapter(requireContext(),cajaTiendaList);
            lvcajatienda.setAdapter(arrayAdapterCajaTienda);
        }else{

            retrofitInit();
            pb.setVisibility(View.VISIBLE);

            //Consultar la API de Cajas
            Call<ResponseCajas> call = apiService.doCajas(usuario);
            call.enqueue(new Callback<ResponseCajas>() {
                @Override
                public void onResponse(Call<ResponseCajas> call, Response<ResponseCajas> response) {
                    if (getContext() != null) {
                        assert response.body() != null;
                        cajasList = response.body().getCajas();
                        //for para mostrar las cajas que pertenecen a la tienda ya selecionada
                        if (isCaja) {
                            String tienda = SPM.getString(Constantes.TIENDA_CODE);
                            for (Caja str : cajasList)
                                if (tienda.equalsIgnoreCase(str.getCodigoTienda())) {
                                    cajaTiendaList.add(str.getCodigoCaja());
                                    cajasFiltroList.add(str);
                                }
                        } else {
                            for (Caja str : cajasList) {
                                cajaTiendaList.add(str.getCodigoTienda());
                                cajasFiltroList.add(str);
                            }
                            Set<String> set = new HashSet<>(cajaTiendaList);
                            cajaTiendaList.clear();
                            cajaTiendaList.addAll(set);
                            Collections.sort(cajaTiendaList);
                        }
                        pb.setVisibility(View.GONE);

                        ListaSimpleRecyclerViewAdapter arrayAdapterCajaTienda = new ListaSimpleRecyclerViewAdapter(getContext(),cajaTiendaList);
                        lvcajatienda.setAdapter(arrayAdapterCajaTienda);
                    }
                }
                @Override
                public void onFailure(Call<ResponseCajas> call, Throwable t) {
                    if(getContext() != null) {
                        Log.e("LOGCAT", "Error ResponseCajas: " + call + t);
                        pb.setVisibility(View.GONE);
                        msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
                    }
                }
            });
        }

        escucharCambios();

        setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    private void escucharCambios() {
        //Evento click sobre el ListView
        lvcajatienda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Guardar variable de sesion de la nueva Caja o Tienda
                if(!listAuxBuscador.isEmpty()){
                    listAuxClickBuscador = listAuxBuscador;
                    listAuxClickFiltroBuscador = listAuxFiltro;
                }else{
                    listAuxClickBuscador = cajaTiendaList;
                    listAuxClickFiltroBuscador = cajasFiltroList;
                }
                if (isCaja) {
                    SPM.setString(Constantes.DIVISA, listAuxClickFiltroBuscador.get(position).getDivisa());
                    SPM.setString(Constantes.MEDIOS_PAGO, listAuxClickFiltroBuscador.get(position).getMediosPagoAutorizados());
                    SPM.setString(Constantes.PAIS_CODE, listAuxClickFiltroBuscador.get(position).getPais());
                    SPM.setString(Constantes.NOMBRE_TIENDA, listAuxClickFiltroBuscador.get(position).getNombreTienda());
                    SPM.setString(Constantes.IDENTIFICADOR_CAJA, listAuxClickFiltroBuscador.get(position).getIdentificadorCaja());
                    SPM.setString(Constantes.PREFIJO_CAJA, listAuxClickFiltroBuscador.get(position).getPrefijoCaja());
                    SPM.setString(Constantes.CAJA_MOVIL, listAuxClickFiltroBuscador.get(position).getCajaMovil());
                } else {
                    SPM.setString(Constantes.TIENDA_CODE, listAuxClickBuscador.get(position));
                    SPM.setString(Constantes.CAJA_CODE, null);
                    SPM.setString(Constantes.MEDIOS_PAGO, null);
                    SPM.setString(Constantes.DIVISA, null);
                    SPM.setString(Constantes.PAIS_CODE, null);
                    SPM.setString(Constantes.NOMBRE_TIENDA, null);
                    SPM.setString(Constantes.IDENTIFICADOR_CAJA, null);
                    SPM.setString(Constantes.PREFIJO_CAJA, null);
                    SPM.setString(Constantes.CAJA_MOVIL, null);
                }

                //Devolver en el hilo la caja o tienda selecionada
                mOnInputListener.sendInputItemCajaTiendaSelectDialogFragment(cajasList, listAuxClickBuscador.get(position), code);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        etBuscadorCT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(cajaTiendaList != null){
                    String search = etBuscadorCT.getText().toString();
                    listAuxFiltroBuscador.clear();
                    listAuxBuscador.clear();

                    if(!search.isEmpty()){
                        for(int i=0; i< cajaTiendaList.size(); i++) {
                            String buscar = cajaTiendaList.get(i);
                            Buscador buscador;
                            if (buscar.contains(search)) {
                                listAuxBuscador.add(buscar);
                                buscador = new Buscador(buscar, i);
                                listAuxFiltroBuscador.add(buscador);
                            }
                        }

                        for (Buscador dato: listAuxFiltroBuscador) {
                            listAuxFiltro.add(cajasFiltroList.get(dato.getPosicion()));
                        }

                        ListaSimpleRecyclerViewAdapter arrayAdapterCajaTienda = new ListaSimpleRecyclerViewAdapter(requireContext(),listAuxBuscador);
                        lvcajatienda.setAdapter(arrayAdapterCajaTienda);
                    }else{
                        ListaSimpleRecyclerViewAdapter arrayAdapterCajaTienda = new ListaSimpleRecyclerViewAdapter(requireContext(),cajaTiendaList);
                        lvcajatienda.setAdapter(arrayAdapterCajaTienda);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //Instanciar el Cliebte REST
    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e("logcat", "onAttach: CajaTiendaSelectDialogFragment: " + e.getMessage() );
        }
    }

    private void msjToast(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }
}

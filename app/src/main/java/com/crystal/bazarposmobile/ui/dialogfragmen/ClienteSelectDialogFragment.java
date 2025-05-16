package com.crystal.bazarposmobile.ui.dialogfragmen;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.retrofit.response.cliente.Cliente;
import com.crystal.bazarposmobile.ui.adapters.ListaSimpleRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SuppressLint("ValidFragment")
public class ClienteSelectDialogFragment extends DialogFragment {

    //Declaración de los objetos de la interfaz del DialogFragment
    private View view;
    private ListView lvclientes;
    private Button btnclientenuevo;

    //Declaración de la variables del DialogFragment
    private List<Cliente> clientes;
    private List<String> listclientes = new ArrayList<>();

    public ClienteSelectDialogFragment(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public interface OnInputListener{
        void sendInputListClienteSelectDialogFragment(Boolean esNuevo, Cliente cliente);
    }

    public ClienteSelectDialogFragment.OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Documento: "+clientes.get(0).getDocumento());
        builder.setMessage(R.string.select_cliente_compra)
                .setIcon(R.mipmap.usuario)
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_cliente_select, null);

        lvclientes = view.findViewById(R.id.lvClientesCSDF);
        btnclientenuevo = view.findViewById(R.id.btnNuevoCSDF);

        for(Cliente c : clientes) {

            String getTipoDocumento = getResources().getString(R.string.cedula_ciudadania);

            switch(c.getTipoDocumento().trim()){
                case "1":
                    getTipoDocumento = getResources().getString(R.string.cedula_ciudadania);
                    SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.cedula_ciudadania_letra));
                    break;
                case "2":
                    getTipoDocumento = getResources().getString(R.string.nit);
                    SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.nit));
                    break;
                case "8":
                    getTipoDocumento = getResources().getString(R.string.pasaporte);
                    SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.pasaporte_letra));
                    break;
                case "11":
                    getTipoDocumento = getResources().getString(R.string.cedula_identidad_cr);
                    SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.cedula_ciudadania_letra));
                    break;
                case "12":
                    getTipoDocumento = getResources().getString(R.string.cedula_juridica);
                    SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.cedula_ciudadania_letra));
                    break;
                case "13":
                    getTipoDocumento = getResources().getString(R.string.dimex);
                    SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.dimex_letra));
                    break;
                case "14":
                    getTipoDocumento = getResources().getString(R.string.nite);
                    SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.nit));
                    break;
                case "15":
                    getTipoDocumento = getResources().getString(R.string.extranjero);
                    SPM.setString(Constantes.TIPO_DOCUMENTO_CLIENTE_DESC_L, getResources().getString(R.string.dimex_letra));
                    break;
            }

            listclientes.add(c.getFirstName()+" "+c.getLastName()+"\n"+getTipoDocumento);
        }

        //Adaptador con la lista de clientes
        ListaSimpleRecyclerViewAdapter adapterClientes = new ListaSimpleRecyclerViewAdapter(requireContext(),listclientes);
        lvclientes.setAdapter(adapterClientes);

        //Seleccion del clientes
        lvclientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnInputListener.sendInputListClienteSelectDialogFragment(false,clientes.get(position));
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        btnclientenuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnInputListener.sendInputListClienteSelectDialogFragment(true,clientes.get(0));
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e("logcat", "onAttach: ClienteSelectDialogFragment: " + e.getMessage() );
        }
    }

}

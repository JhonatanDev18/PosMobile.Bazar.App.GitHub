package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.IConfigurableActivity;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.TipoDatafono;
import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.ui.adapters.DispositivosTefRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TipoTefDialogFragment extends DialogFragment {
    //Declaración de los objetos de la interfaz del DialogFragment
    private View view;
    private List<TipoDatafono> datafonosCaja;
    private RecyclerView recyclerViewDispositivosTef;
    private DispositivosTefRecyclerViewAdapter adapter;
    private ImageView ivCloseTipoTef;
    private IConfigurableActivity configurableActivity;
    private RadioButton rbCredibanco, rbRedeban;

    public TipoTefDialogFragment(IConfigurableActivity configurableActivity) {
        this.configurableActivity = configurableActivity;
    }

    @NonNull
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_tipo_tef, null);

        inicializar();
        eventos();

        rbCredibanco.callOnClick();

        setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    private void inicializar() {
        datafonosCaja = new ArrayList<>();

        rbCredibanco = view.findViewById(R.id.rbCredibanco);
        rbRedeban = view.findViewById(R.id.rbRedeban);
        ivCloseTipoTef = view.findViewById(R.id.ivCloseTipoTef);

        recyclerViewDispositivosTef = view.findViewById(R.id.rvDispositivosTef);
        recyclerViewDispositivosTef.setLayoutManager(new LinearLayoutManager(getContext()));

        datafonos();
    }

    private void datafonos() {
        List<String> descuentoItem = Arrays.asList(SPM.getString(Constantes.DATAFONOS_CREDIBANCO).split(";"));
        for (String str : descuentoItem) {
            if (!str.trim().isEmpty()) {
                List<String> descuentoDetalle = Arrays.asList(str.split("\\|"));
                if (!descuentoDetalle.isEmpty()) {
                    datafonosCaja.add(new TipoDatafono(descuentoDetalle.get(0), descuentoDetalle.get(1).equals("TRUE"), false));
                }
            }
        }

        descuentoItem = Arrays.asList(SPM.getString(Constantes.DATAFONOS_REDEBAN).split(";"));
        for (String str : descuentoItem) {
            if (!str.trim().isEmpty()) {
                List<String> descuentoDetalle = Arrays.asList(str.split("\\|"));
                if (!descuentoDetalle.isEmpty()) {
                    datafonosCaja.add(new TipoDatafono(descuentoDetalle.get(0), descuentoDetalle.get(1).equals("TRUE"), true));
                }
            }
        }
    }

    private void eventos() {
        rbCredibanco.setOnClickListener(this::seleccionarDatafono);
        rbRedeban.setOnClickListener(this::seleccionarDatafono);
        ivCloseTipoTef.setOnClickListener(this::cerrarDialog);
    }

    private void seleccionarDatafono(View view) {
        List<TipoDatafono> datafonosRedeban = new ArrayList<>();
        List<TipoDatafono> datafonosCredibanco = new ArrayList<>();

        for (TipoDatafono datafono : datafonosCaja) {
            if(!datafono.isRedeban()){
                datafonosCredibanco.add(datafono);
            }else{
                datafonosRedeban.add(datafono);
            }
        }

        if(rbCredibanco.isChecked()){
            adapter = new DispositivosTefRecyclerViewAdapter(datafonosCredibanco, configurableActivity);
            recyclerViewDispositivosTef.setAdapter(adapter);
        }

        if(rbRedeban.isChecked()){
            adapter = new DispositivosTefRecyclerViewAdapter(datafonosRedeban, configurableActivity);
            recyclerViewDispositivosTef.setAdapter(adapter);
        }
    }

    private void cerrarDialog(View view) {
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

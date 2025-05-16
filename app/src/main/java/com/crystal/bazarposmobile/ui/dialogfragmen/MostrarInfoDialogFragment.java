package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.common.ClaveValorTef;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.Utilidades;

import java.io.Serializable;
import java.util.List;
import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.ui.adapters.ListaSimpleInfoRecyclerViewAdapter;

public class MostrarInfoDialogFragment extends DialogFragment {
    private View view;
    private List<ClaveValorTef> listaClaveValor;
    private ListView lvMostrarInfo;
    private ImageView ivCloseMostrarInfo, ivIconMostrarInfo;
    private int imagen;
    private String titulo;
    private TextView tvTituloMostrarInfo;
    private int accion;
    private Context contexto;
    private Button btnImprimirInfo;

    public MostrarInfoDialogFragment(List<ClaveValorTef> listaClaveValor) {
        this.listaClaveValor = listaClaveValor;
        imagen = 0;
        titulo = "";
    }

    public MostrarInfoDialogFragment(int imagen, String titulo, List<ClaveValorTef> listaClaveValor,
                                     int accion) {
        this.listaClaveValor = listaClaveValor;
        this.imagen = imagen;
        this.titulo = titulo;
        this.accion = accion;
    }

    @SuppressLint("InflateParams")
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_mostrar_info, null);

        inicializar();
        cargarDatos();
        eventos();

        setCancelable(false);
        builder.setView(view);

        return builder.create();
    }

    private void inicializar() {
        contexto = requireContext();
        ivCloseMostrarInfo = view.findViewById(R.id.ivCloseMostrarInfo);
        ivIconMostrarInfo = view.findViewById(R.id.ivIconMostrarInfo);
        tvTituloMostrarInfo = view.findViewById(R.id.tvTituloMostrarInfo);
        lvMostrarInfo = view.findViewById(R.id.lvMostrarInfo);
        btnImprimirInfo = view.findViewById(R.id.btnImprimirInfo);

        if(accion == Constantes.ACCION_DEFAULT || accion == Constantes.ACCION_TEF_COMUNICACION){
            btnImprimirInfo.setText(getResources().getString(R.string.entiendo));
        }
    }

    private void cargarDatos() {
        if(imagen!=0){
            ivIconMostrarInfo.setImageResource(imagen);
        }

        if(!titulo.isEmpty()){
            tvTituloMostrarInfo.setText(titulo);
        }

        ListaSimpleInfoRecyclerViewAdapter adapter =
                new ListaSimpleInfoRecyclerViewAdapter(contexto,listaClaveValor, accion);
        lvMostrarInfo.setAdapter(adapter);
    }

    private void eventos() {
        ivCloseMostrarInfo.setOnClickListener(this::cerrar);
        btnImprimirInfo.setOnClickListener(this::imprimir);
    }

    private void cerrar(View view){
        dismiss();
    }

    private void imprimir(View view){
        if(accion == Constantes.ACCION_DEFAULT || accion == Constantes.ACCION_TEF_COMUNICACION){
            dismiss();
        }else{
            try {
                Intent i = Utilidades.activityImprimir(contexto);
                i.putExtra("lablePrint", (Serializable) "CompTEF");
                i.putExtra("claveValorTef", true);
                i.putExtra("tituloClaveValor", titulo);
                i.putExtra("listaClaveValor", (Serializable) listaClaveValor);
                requireActivity().startActivity(i);
            }catch (Exception e){
                Utilidades.mjsToast(e.getMessage(), Constantes.TOAST_TYPE_INFO, Toast.LENGTH_LONG,
                        requireContext());
            }
        }
    }
}

package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.ui.ClienteConsultaActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MsjCustomUnaAccionDialogFragment extends DialogFragment {
    private View view;
    private ImageView ivMsjCustom;
    private TextView tvTituloMsjCustom, tvTextoMsjCustom;
    private Button btnEntendidoMsjCustom;
    private int imagen;
    private String titulo, mensaje, mensajeDev;
    private String textoBoton;
    private int accion;
    private ClienteConsultaActivity clienteConsultaActivity;

    public MsjCustomUnaAccionDialogFragment(int imagen, String titulo, String mensaje, String mensajeDev, String textoBoton,
                                            int accion) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.mensajeDev = mensajeDev;
        this.textoBoton = textoBoton;
        this.accion = accion;
    }

    public MsjCustomUnaAccionDialogFragment(String titulo, String mensaje, String mensajeDev, String textoBoton,
                                            int accion) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.mensajeDev = mensajeDev;
        this.textoBoton = textoBoton;
        this.accion = accion;
        imagen = 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_msj_custom_one, null);

        //Asignacion de Referencias
        inicializar();

        //Eventos
        eventos();

        setCancelable(false);
        builder.setView(view);
        //Cree el objeto AlertDialog y devuélvalo
        return builder.create();
    }

    private void inicializar() {
        ivMsjCustom = view.findViewById(R.id.ivMsjCustom);
        tvTituloMsjCustom = view.findViewById(R.id.tvTituloMsjCustom);
        tvTextoMsjCustom = view.findViewById(R.id.tvTextoMsjCustom);
        btnEntendidoMsjCustom = view.findViewById(R.id.btnEntendidoMsjCustom);
        btnEntendidoMsjCustom.setText(textoBoton);

        if(imagen == 0){
            ivMsjCustom.setVisibility(View.GONE);
        }else{
            ivMsjCustom.setImageResource(imagen);
        }

        tvTituloMsjCustom.setText(titulo);
        tvTextoMsjCustom.setText(mensaje);
    }

    private void eventos() {
        btnEntendidoMsjCustom.setOnClickListener(this::entiendo);
        ivMsjCustom.setOnClickListener(this::msjDev);
    }

    private void entiendo(View view){
        switch (accion){
            case Constantes.ACCION_DEFAULT:
                dismiss();
                break;
            case Constantes.ACCION_ACTUALIZAR_TRANSACCION_TEF:
                clienteConsultaActivity.reintentarActualizacionTransaccion();
                break;
        }
    }

    private void msjDev(View view){
        if(!mensajeDev.isEmpty()){
            Utilidades.sweetAlert(getResources().getString(R.string.error), mensajeDev,
                    SweetAlertDialog.ERROR_TYPE, requireContext());
        }
    }

    public void setClienteConsultaActivity(ClienteConsultaActivity clienteConsultaActivity) {
        this.clienteConsultaActivity = clienteConsultaActivity;
    }
}

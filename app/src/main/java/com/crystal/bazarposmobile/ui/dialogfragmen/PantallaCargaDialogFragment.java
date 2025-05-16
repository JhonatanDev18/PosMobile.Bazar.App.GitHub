package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.IConfigurableCarga;

public class PantallaCargaDialogFragment extends DialogFragment {
    private String mensaje;
    private LottieAnimationView lottieAnimationCarga;
    private boolean cancel;
    private TextView tvTextoCarga;
    private ImageView ivCerrarMsjCarga;
    private View view;
    private IConfigurableCarga configurableCarga;

    public PantallaCargaDialogFragment(String mensaje, boolean cancel, IConfigurableCarga IConfigurableCarga) {
        this.mensaje = mensaje;
        this.cancel = cancel;
        this.configurableCarga = IConfigurableCarga;
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_pantalla_carga, null);

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
        tvTextoCarga = view.findViewById(R.id.tvTextoCarga);
        tvTextoCarga.setText(mensaje);
        ivCerrarMsjCarga = view.findViewById(R.id.ivCerrarMsjCarga);

        if(cancel){
            ivCerrarMsjCarga.setVisibility(View.VISIBLE);
        }

        lottieAnimationCarga = view.findViewById(R.id.lottieAnimationCarga);
        lottieAnimationCarga.setAnimation(getResources().getString(R.string.animacion_carga_1));
        lottieAnimationCarga.playAnimation();
    }

    private void eventos(){
        ivCerrarMsjCarga.setOnClickListener(this::cerrar);
    }

    private void cerrar(View view){
        if(configurableCarga != null){
            configurableCarga.cerrarFragmentCarga();
        }else{
            dismiss();
        }
    }
}

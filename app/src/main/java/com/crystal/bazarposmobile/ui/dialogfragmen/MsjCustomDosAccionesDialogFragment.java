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
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.CuerpoRespuestaDatafonoN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.RespuestaCompletaTef;
import com.crystal.bazarposmobile.ui.ClienteConsultaActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MsjCustomDosAccionesDialogFragment extends DialogFragment {
    private View view;
    private ImageView ivMsjCustomTwo, ivCloseCustomTwo;
    private TextView tvTituloMsjCustomTwo, tvTextoMsjCustomTwo;
    private Button btnPositivoTwo, btnNegativoTwo;
    private int imagen;
    private String titulo;
    private String mensaje;
    private String mensajeDev;
    private String textoBotonPositivo, textoBotonNegativo;
    private int accion;
    private boolean permitirCierreSuperior;
    private ClienteConsultaActivity clienteConsulta;
    private CuerpoRespuestaDatafonoN6 cuerpoRespuestaDatafono;
    private RespuestaCompletaTef respuestaCompletaTef;

    public MsjCustomDosAccionesDialogFragment(int imagen, String titulo, String mensaje, String mensajeDev,
                                              String textoBotonPositivo, String textoBotonNegativo, int accion,
                                              boolean permitirCierreSuperior) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.mensajeDev = mensajeDev;
        this.textoBotonPositivo = textoBotonPositivo;
        this.textoBotonNegativo = textoBotonNegativo;
        this.accion = accion;
        this.permitirCierreSuperior = permitirCierreSuperior;
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_msj_custom_two, null);

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
        ivMsjCustomTwo = view.findViewById(R.id.ivMsjCustomTwo);
        tvTituloMsjCustomTwo = view.findViewById(R.id.tvTituloMsjCustomTwo);
        tvTextoMsjCustomTwo = view.findViewById(R.id.tvTextoMsjCustomTwo);
        btnPositivoTwo = view.findViewById(R.id.btnPositivoTwo);
        btnNegativoTwo = view.findViewById(R.id.btnNegativoTwo);
        ivCloseCustomTwo = view.findViewById(R.id.ivCloseCustomTwo);

        if(imagen == 0){
            ivMsjCustomTwo.setVisibility(View.GONE);
        }else{
            ivMsjCustomTwo.setImageResource(imagen);
        }

        if(permitirCierreSuperior){
            ivCloseCustomTwo.setVisibility(View.VISIBLE);
        }

        tvTituloMsjCustomTwo.setText(titulo);
        tvTextoMsjCustomTwo.setText(mensaje);
        btnPositivoTwo.setText(textoBotonPositivo);
        btnNegativoTwo.setText(textoBotonNegativo);
    }

    private void eventos() {
        ivMsjCustomTwo.setOnClickListener(this::msjDev);
        btnPositivoTwo.setOnClickListener(this::accionPositiva);
        btnNegativoTwo.setOnClickListener(this::accionNegativa);
        ivCloseCustomTwo.setOnClickListener(this::cerrar);
    }

    public void cerrar(View view){
        dismiss();
    }

    public void accionPositiva(View view){
        switch (accion){
            case Constantes.ACCION_ANULAR_TEF:
                //Anular tef
                if(respuestaCompletaTef.getHeader().getAnulada()){
                    Utilidades.mjsToast(getResources().getString(R.string.la_transaccion_ya_anulada),
                            Constantes.TOAST_TYPE_INFO, Toast.LENGTH_LONG, requireContext());
                }else{
                    clienteConsulta.anularTransaccionTef(respuestaCompletaTef);
                }
                break;
        }
    }

    public void accionNegativa(View view){
        switch (accion){
            case Constantes.ACCION_ANULAR_TEF:
                //Mostrar info tef
                clienteConsulta.mostrarInfoTef(cuerpoRespuestaDatafono);
                break;
        }
    }

    private void msjDev(View view){
        if(!mensajeDev.isEmpty()){
            Utilidades.sweetAlert(getResources().getString(R.string.error), mensajeDev,
                    SweetAlertDialog.ERROR_TYPE, requireContext());
        }
    }

    public void setClienteConsulta(ClienteConsultaActivity clienteConsulta) {
        this.clienteConsulta = clienteConsulta;
    }

    public void setCuerpoRespuestaDatafono(CuerpoRespuestaDatafonoN6 cuerpoRespuestaDatafono) {
        this.cuerpoRespuestaDatafono = cuerpoRespuestaDatafono;
    }

    public void setRespuestaCompletaTef(RespuestaCompletaTef respuestaCompletaTef) {
        this.respuestaCompletaTef = respuestaCompletaTef;
    }
}

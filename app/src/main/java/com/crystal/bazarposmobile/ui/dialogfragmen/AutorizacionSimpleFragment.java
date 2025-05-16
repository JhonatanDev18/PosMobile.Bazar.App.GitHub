package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.retrofit.request.RequestLogin;
import com.crystal.bazarposmobile.retrofit.response.login.ResponseLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class AutorizacionSimpleFragment extends DialogFragment {
    //Declaración de los objetos de la interfaz del DialogFragment
    View view;
    EditText etCodigo;

    //Declaración de la variables del DialogFragment
    String autoReimpresion;

    public AutorizacionSimpleFragment() {
    }

    public interface OnInputListener{
        void sendInputItemAutorizacionSimpleFrament(Boolean aut);
    }
    public OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.autorizacion);

        builder.setMessage(getResources().getString(R.string.auto_re_imprimir))
                .setIcon(R.mipmap.administrador)
                .setNegativeButton(R.string.atras, (dialog, id) -> {
                    dismiss();
                })
                .setPositiveButton(R.string.autorizar, ((dialog, id) -> {
                }));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_autorizacion_reimpresion, null);

        inicializar();
        eventos();

        setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    private void inicializar() {
        autoReimpresion = SPM.getString(Constantes.AUT_DEVOLUCION);
        etCodigo = view.findViewById(R.id.etCodigoReImpresion);

        etCodigo.requestFocus();
    }

    private void eventos() {
        etCodigo.setOnKeyListener((v, keyCode, event) -> {
            //If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                final AlertDialog d = (AlertDialog)getDialog();
                if(d != null) {
                    Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
                    positiveButton.callOnClick();
                }
                return true;
            }
            return false;
        });

        etCodigo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                final AlertDialog d = (AlertDialog)getDialog();
                if(d != null) {
                    Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
                    positiveButton.callOnClick();
                }
            }
            return false;
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String codigo = etCodigo.getText().toString();

                if(codigo.isEmpty()){
                    Utilidades.mjsToast(getResources().getString(R.string.codigo_requerido),
                            Constantes.TOAST_TYPE_ERROR, Toast.LENGTH_LONG, getContext());
                }else{
                    validarCodigo(codigo);
                }
            });
        }
    }

    private void validarCodigo(String codigo) {
        if(codigo.equals(autoReimpresion)){
            mOnInputListener.sendInputItemAutorizacionSimpleFrament(true);
            Utilidades.mjsToast(getResources().getString(R.string.autorizacion_exitosa), Constantes.TOAST_TYPE_SUCCESS,
                    Toast.LENGTH_LONG, getContext());
            dismiss();
        }else{
            Utilidades.mjsToast(getResources().getString(R.string.autorizacion_invalida),
                    Constantes.TOAST_TYPE_ERROR, Toast.LENGTH_LONG, getContext());
            etCodigo.setText("");
            etCodigo.requestFocus();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e("logcat", "onAttach: AutorizacionSimpleFrament: " + e.getMessage() );
        }
    }
}
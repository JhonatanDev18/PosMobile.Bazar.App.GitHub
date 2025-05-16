package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;

import java.util.Objects;

public class AutorizacionDialogFragment extends DialogFragment {

    //Declaración de los objetos de la interfaz del DialogFragment
    View view;
    EditText etcodigo;
    Button btnautorizar;

    //Declaración de los objetos de la interfaz del activity
    String code;

    public AutorizacionDialogFragment(String code){
        this.code = code;
    }

    public interface OnInputListener{
        void sendInputItemAutorizacion(String code);
    }
    public OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.autorizacion);
        builder.setMessage("Ingrese el código para confirma la autorización")
                .setIcon(R.mipmap.administrador)
                .setNegativeButton(R.string.volver, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dismiss();
                    }
                });
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_autorizacion, null);

        //Asignacion de Referencias
        etcodigo = view.findViewById(R.id.etCodigoADF);
        btnautorizar = view.findViewById(R.id.btnAutorizarAFD);

        //Boton Autorizar
        btnautorizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = etcodigo.getText().toString();
                if(codigo.equals(SPM.getString(Constantes.AUT_DEVOLUCION))){
                    mOnInputListener.sendInputItemAutorizacion(code);
                    msjToast(getResources().getString(R.string.autorizacion_exitosa));
                    dismiss();
                }else{
                    etcodigo.setError(getResources().getString(R.string.autorizacion_invalida));
                }
            }
        });

        //Evento enter sobre password
        etcodigo.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btnautorizar.callOnClick();
                    return true;
                }
                return false;
            }
        });

        etcodigo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnautorizar.callOnClick();
                }
                return handled;
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
            Log.e("logcat", "onAttach: AutorizacionDialogFragment: " + e.getMessage() );
        }
    }

    private void msjToast(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }
}

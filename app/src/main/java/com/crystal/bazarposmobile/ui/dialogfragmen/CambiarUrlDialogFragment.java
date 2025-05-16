package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;

import java.util.Objects;

//Dialog Fragment para la gestion del cambio de la URL Base
public class CambiarUrlDialogFragment extends DialogFragment {

    //Declaración de los objetos de la interfaz del DialogFragment
    private View view;
    private EditText tvurl;

    //Obtener siempre una nueva instancia
    public static CambiarUrlDialogFragment newInstance() {
        return new CambiarUrlDialogFragment();
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.titleUrlDialogFragment);
        builder.setMessage(R.string.msjUrlDialogFragment)
                .setIcon(R.mipmap.url_base)
                .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nuevaUrl = tvurl.getText().toString();
                        //Validar que sea una URL
                        if(URLUtil.isValidUrl(nuevaUrl)){
                            //Guardar variable de sesion de la nueva URL Base de los servicios
                            SPM.setString(Constantes.API_POSSERVICE_URL, nuevaUrl);
                            dialog.dismiss();
                            Intent intent = requireActivity().getIntent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            requireActivity().finish();
                            startActivity(intent);
                        }
                        else{
                            msjToast(getResources().getString(R.string.url_invalida));
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_cambiar_url, null);

        //Asignacion de Referencias
        tvurl = view.findViewById(R.id.etCambiarUrl);
        tvurl.setText(SPM.getString(Constantes.API_POSSERVICE_URL));

        setCancelable(false);
        builder.setView(view);
        //Cree el objeto AlertDialog y devuélvalo
        return builder.create();
    }

    private void msjToast(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }

}

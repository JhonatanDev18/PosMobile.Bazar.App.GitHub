package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;

import java.text.DecimalFormat;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class PagoEfectivoDialogFragment extends DialogFragment {

    //Declaración de los objetos de la interfaz del DialogFragment
    Double total,importe;
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);
    boolean esDevolucion;

    //Declaración de la variables del DialogFragment
    private View view;
    private TextView tvdeuda,tvcambio,tvnetopagar;
    private EditText etimporte;
    private Button btncalcular;

    public PagoEfectivoDialogFragment(Double total ) {
        this.total = Math.abs(total);
        esDevolucion = total < 0;
    }

    public interface OnInputListener{
        void sendInputItemPagoEfectivoDialogFragment(Double importe);
    }
    public PagoEfectivoDialogFragment.OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.efectivo);
        builder.setMessage(getResources().getString(R.string.ingrese_cantidad_efectivo))
                .setIcon(R.mipmap.pago_efectivo)
                .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //Asignacion de Referencias
        view = inflater.inflate(R.layout.dialog_fragment_pago_efectivo, null);
        tvdeuda = view.findViewById(R.id.tvDeudaPEDF);
        tvcambio = view.findViewById(R.id.tvCambioPEDF);
        etimporte = view.findViewById(R.id.etImportePEDF);
        btncalcular = view.findViewById(R.id.btnCalcularPEDF);
        tvnetopagar = view.findViewById(R.id.tvNetoPagarPEDF);

        //Mostrat el total
        tvdeuda.setText(formatea.format(total));
        tvnetopagar.setText(formatea.format(total));

        //Boton de calcular muestra la diferencia entre el total y el importe
        btncalcular.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                if(etimporte.getText().toString().equals("")){
                    int intDeduda = total.intValue();
                    etimporte.setText(Integer.toString(intDeduda));
                }

                //Validar que el importe sea el adecuado
                if(etimporte.getText().length() > 9){
                    importe = 0.0;
                    msjToast(getResources().getString(R.string.importe_invalido));
                }
                else{
                    importe = Double.parseDouble(etimporte.getText().toString());
                    double rest = total - importe;
                    if(rest >= 0){
                        tvdeuda.setText(formatea.format(rest));
                        tvdeuda.setTextColor(Color.RED);
                        tvcambio.setText("0");
                        tvcambio.setTextColor(getResources().getColor(R.color.colorSecudary));
                    }else{
                        Double impMonto = rest*(-1);
                        tvdeuda.setText("0");
                        tvdeuda.setTextColor(getResources().getColor(R.color.colorSecudary));
                        tvcambio.setText(formatea.format(impMonto));
                        tvcambio.setTextColor(Color.RED);
                    }
                }
            }
        });

        //Evento enter sobre el importe
        etimporte.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    etimporte.requestFocus();
                    btncalcular.callOnClick();
                    if(importe > 0)
                        if(esDevolucion){
                            mOnInputListener.sendInputItemPagoEfectivoDialogFragment(importe*-1);
                        }else{
                            mOnInputListener.sendInputItemPagoEfectivoDialogFragment(importe);
                        }
                    Objects.requireNonNull(getDialog()).dismiss();
                    return true;
                }
                return false;
            }
        });

        setCancelable(false);
        builder.setView(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(etimporte.getText().toString().length() == 0 || etimporte.getText().toString().length()> 9){
                        if(etimporte.getText().toString().length() == 0 ){
                            if(esDevolucion){
                                mOnInputListener.sendInputItemPagoEfectivoDialogFragment(total*-1);
                            }else{
                                mOnInputListener.sendInputItemPagoEfectivoDialogFragment(total);
                            }
                            d.dismiss();
                        }else{
                            msjToast(getResources().getString(R.string.importe_invalido));
                        }
                    }else{
                        importe = Double.parseDouble(etimporte.getText().toString());
                        if(esDevolucion){
                            if(importe > 0  && importe <= total){
                                if(esDevolucion){
                                    mOnInputListener.sendInputItemPagoEfectivoDialogFragment(importe*-1);
                                }else{
                                    mOnInputListener.sendInputItemPagoEfectivoDialogFragment(importe);
                                }
                                d.dismiss();
                            }else{
                                msjToast(getResources().getString(R.string.importe_invalido_devolucion));
                            }
                        }else{
                            if(importe > 0 ){
                                if(esDevolucion){
                                    mOnInputListener.sendInputItemPagoEfectivoDialogFragment(importe*-1);
                                }else{
                                    mOnInputListener.sendInputItemPagoEfectivoDialogFragment(importe);
                                }
                                d.dismiss();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e("logcat", "onAttach: PagoEfectivoDialogFragment: " + e.getMessage() );
        }
    }

    private void msjToast(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }
}

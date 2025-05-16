package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;

import java.text.DecimalFormat;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class PagoTipoBonoDialogFragment extends DialogFragment {

    //Declaración de la variables del DialogFragment
    String titulo,tipoPago;
    Double totalCompra,totalDeuda,importe;
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    //Declaración de los objetos de la interfaz del DialogFragment
    private View view;
    private TextView tvtotal,tvdeuda;
    private EditText etimporte;
    private Button btncalcular;

    public PagoTipoBonoDialogFragment(String titulo, String tipoPago, Double totalCompra, Double totalDeuda) {
        this.titulo = titulo;
        this.tipoPago = tipoPago;
        this.totalCompra = totalCompra;
        this.totalDeuda = totalDeuda;
    }

    public interface OnInputListener{
        void sendInputItemPagoTipoBonoDialogFragment(String titulo,String tipoPago, Double importe);
    }
    public PagoTipoBonoDialogFragment.OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo);
        builder.setMessage(getResources().getString(R.string.ingrese_importe))
                .setIcon(R.mipmap.bono_regalo)
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
        view = inflater.inflate(R.layout.dialog_fragment_pago_tipo_bono, null);
        tvtotal = view.findViewById(R.id.tvTotalPTBDF);
        tvdeuda = view.findViewById(R.id.tvDeudaPTBDF);
        etimporte = view.findViewById(R.id.etImportePTBDF);
        btncalcular = view.findViewById(R.id.btnCalcularPTBDF);

        //Mostrat el total y la deuda de la compra
        tvtotal.setText(formatea.format(totalCompra));
        tvdeuda.setText(formatea.format(totalDeuda));

        //Boton Calcular
        btncalcular.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                if(etimporte.getText().toString().equals("")){
                    int intDeduda = totalDeuda.intValue();
                    etimporte.setText(Integer.toString(intDeduda));
                }

                //Validar que el importe sea el adecuado
                if(etimporte.getText().length() > 9){
                    importe = 0.0;
                    msjToast(getResources().getString(R.string.importe_invalido));
                }
                else{
                    double impMonto = Double.parseDouble(etimporte.getText().toString());
                    if((int) impMonto > 0){
                        importe = impMonto;
                        double rest = totalDeuda - (int) impMonto;
                        if(rest >= 0){
                            tvdeuda.setText(formatea.format(rest));
                            tvdeuda.setTextColor(Color.RED);
                        }else{
                            msjToast(getResources().getString(R.string.importe_igual_menor_cero));
                        }
                    }else{
                        msjToast(getResources().getString(R.string.importe_invalido));
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
                    if(importe > 0 && importe <= totalDeuda.intValue())
                        mOnInputListener.sendInputItemPagoTipoBonoDialogFragment(titulo,tipoPago,importe);
                    Objects.requireNonNull(getDialog()).dismiss();
                    return true;
                }
                return false;
            }
        });

        setCancelable(false);
        builder.setView(view);
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
                            mOnInputListener.sendInputItemPagoTipoBonoDialogFragment(titulo,tipoPago,totalDeuda);
                            d.dismiss();
                        }else{
                            msjToast(getResources().getString(R.string.importe_invalido));
                        }
                    }else{
                        importe = Double.parseDouble(etimporte.getText().toString());
                        if(importe > 0 && importe <= totalDeuda){
                            mOnInputListener.sendInputItemPagoTipoBonoDialogFragment(titulo,tipoPago,importe);
                            d.dismiss();
                        }else{
                            msjToast(getResources().getString(R.string.importe_invalido));
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
            Log.e("logcat", "onAttach: PagoTipoBonoDialogFragment: " + e.getMessage() );
        }
    }

    private void msjToast(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }
}

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.retrofit.response.tarjetasbancarias.Tarjeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class PagoTEFContinguenciaDialogFragment extends DialogFragment {

    //Declaración de la variables del DialogFragment
    Double totalDeuda;
    Tarjeta tajetaSelect = null;
    List<Tarjeta> tarjetasBancariasList = new ArrayList<>();
    List<Tarjeta> tarjetasbancariasOrdenadaList;
    List<String> tarjetasTextList = new ArrayList<>();
    String NombreTipoPago;
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);
    boolean esdevolucion;

    //Declaración de los objetos de la interfaz del DialogFragment
    View view;
    Spinner sptajetasbancarias;
    ProgressBar pb;
    EditText etimporte;
    TextView tvdeuda;
    Button btndueda;

    public PagoTEFContinguenciaDialogFragment(String NombreTipoPago, Double totalDeuda, List<Tarjeta> tarjetasbancarias) {
        this.totalDeuda = Math.abs(totalDeuda);
        this.NombreTipoPago = NombreTipoPago;
        this.tarjetasbancariasOrdenadaList = tarjetasbancarias;
        esdevolucion = totalDeuda < 0;
    }

    public interface OnInputListener{
        void sendInputItemPagoTEFContinguenciaDialogFragment(Tarjeta tajeta, Double importe, String nombre);
    }
    public PagoTEFContinguenciaDialogFragment.OnInputListener mOnInputListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(NombreTipoPago);
        builder.setMessage(R.string.seleccione_tipo_tarjeta)
                .setIcon(R.mipmap.tarjeta_de_debito)
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
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Asignacion de Referencias
        view = inflater.inflate(R.layout.dialog_fragment_pago_tef_continguencia, null);
        sptajetasbancarias = view.findViewById(R.id.spinnerPTEFCDF);
        pb = view.findViewById(R.id.pbPTEFCDF);
        etimporte = view.findViewById(R.id.etImportePTEFCDF);
        btndueda = view.findViewById(R.id.btnCalcularPTEFCDF);
        etimporte.setEnabled(false);
        btndueda.setEnabled(false);
        tvdeuda = view.findViewById(R.id.tvDeudaPTEFCDF);
        tvdeuda.setText(formatea.format(totalDeuda));

        List<String> tarjetasorden = new ArrayList<>();
        tarjetasorden.add("MAS");
        tarjetasorden.add("MAC");
        tarjetasorden.add("VIS");
        tarjetasorden.add("VIC");
        tarjetasorden.add("AME");
        tarjetasorden.add("AMC");
        String tarjetascode = "MAS,MAC,VIS,VIC,AME,AMC";

        tarjetasBancariasList.add(new Tarjeta("0",getResources().getString(R.string.seleccione_1_tarjeta),"",""));
        tarjetasTextList.add(getResources().getString(R.string.seleccione_1_tarjeta));

        for(String code : tarjetasorden) {
            for(Tarjeta item : tarjetasbancariasOrdenadaList) {
                if(code.equals(item.getCodigo())){
                    tarjetasBancariasList.add(item);
                    tarjetasTextList.add(item.getNombre());
                    break;
                }
            }
        }
        for(Tarjeta item : tarjetasbancariasOrdenadaList){
            if(!tarjetascode.contains(item.getCodigo())){
                tarjetasBancariasList.add(item);
                tarjetasTextList.add(item.getNombre());
            }
        }
        pb.setVisibility(View.GONE);

        //Adaptador con la lista de los vendedores
        ArrayAdapter<String> arrayAdapterTEFContinguencia = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,tarjetasTextList);
        sptajetasbancarias.setAdapter(arrayAdapterTEFContinguencia);

        //Seleccion de la tarjeta
        sptajetasbancarias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    etimporte.setEnabled(false);
                    btndueda.setEnabled(false);
                    etimporte.setText("");
                    tajetaSelect = null;
                }else{
                    tajetaSelect = tarjetasBancariasList.get(position);
                    etimporte.setEnabled(true);
                    btndueda.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //Evento enter sobre el importe
        etimporte.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    confirmarImporte();
                    return true;
                }
                return false;
            }
        });

        btndueda.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int deudaprint = totalDeuda.intValue();
                etimporte.setText(Integer.toString(deudaprint));
            }
        });

        setCancelable(false);
        builder.setView(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    //Validar que el importe imgresado es el correcto
    private void confirmarImporte() {
        //Validar que el importe imgresado es el correcto
        if(etimporte.getText().toString().length() == 0 ){
            if(tajetaSelect == null){
                msjToast(getResources().getString(R.string.seleccione_1_tarjeta));
            }else{
                if(esdevolucion)
                    totalDeuda = totalDeuda*-1;
                mOnInputListener.sendInputItemPagoTEFContinguenciaDialogFragment(tajetaSelect,totalDeuda,NombreTipoPago);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        }else if(etimporte.getText().toString().length()> 9){
            msjToast(getResources().getString(R.string.importe_invalido));
        }else{
            Double impMonto = Double.parseDouble(etimporte.getText().toString());
            if(impMonto.intValue() > 0){
                double rest = totalDeuda - impMonto;
                if(rest >= 0){
                    if(esdevolucion)
                        impMonto = impMonto * -1;
                    mOnInputListener.sendInputItemPagoTEFContinguenciaDialogFragment(tajetaSelect,impMonto,NombreTipoPago);
                    Objects.requireNonNull(getDialog()).dismiss();
                }else{
                    msjToast(getResources().getString(R.string.importe_igual_menor_cero));
                }
            }else{
                msjToast(getResources().getString(R.string.importe_invalido));
            }
        }
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
                    confirmarImporte();
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

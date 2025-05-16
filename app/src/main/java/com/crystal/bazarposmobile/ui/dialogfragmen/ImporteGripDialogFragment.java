package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.retrofit.response.mercadopago.MercadoPagoImporte;

import java.text.DecimalFormat;

@SuppressLint("ValidFragment")
public class ImporteGripDialogFragment extends DialogFragment {

    //Declaración de la variables del DialogFragment
    private Payment item;
    private int posicion;
    DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    //Declaración de los objetos de la interfaz del DialogFragment
    private View view;
    TextView tvmonto,tvmoneda,tvcodigo,tvtipo;

    public ImporteGripDialogFragment(Payment itemPayment, int position) {
        this.item = itemPayment;
        this.posicion = position;
    }

    public interface OnInputListener{
        void sendInputItemImporteGripDialogFragment(Integer inputposition);
        void sendInputItemImporteTEFGripDialogFragment(DatafonoEntity respDatafono, MercadoPagoImporte mpImporte);
    }
    public OnInputListener mOnInputListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Verifcar si es TEF para no permitir eliminar el importe
        if(item.getIsTEF()){
            builder.setTitle(getResources().getString(R.string.importe)+" "+item.getName());
            builder.setMessage(getResources().getString(R.string.desc_importe))
                    .setIcon(R.mipmap.precio_producto)
                    .setPositiveButton(R.string.re_imprimir, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(item.getDatafonoEntity() != null){
                                mOnInputListener.sendInputItemImporteTEFGripDialogFragment(item.getDatafonoEntity(),null);
                            }else{
                                mOnInputListener.sendInputItemImporteTEFGripDialogFragment(null,item.getRespuestaMercadoPago());
                            }
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        }else if(item.isEliminable()){
            builder.setTitle(getResources().getString(R.string.importe)+" "+item.getName());
            builder.setMessage(getResources().getString(R.string.quieres_eliminar_importe))
                    .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mOnInputListener.sendInputItemImporteGripDialogFragment(posicion);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        }else{
            builder.setTitle(getResources().getString(R.string.importe)+" "+item.getName());
            builder.setMessage(getResources().getString(R.string.desc_importe))
                    .setNegativeButton(R.string.volver, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Asignacion de Referencias
        view = inflater.inflate(R.layout.dialog_fragment_importe_grip, null);
        tvmonto = view.findViewById(R.id.tvMontoIGDF);
        tvmoneda = view.findViewById(R.id.tvDivisaIGDF);
        tvcodigo = view.findViewById(R.id.tvCodigoIGDF);
        tvtipo = view.findViewById(R.id.tvTipoIGDF);

        //Mostrar los datos del importe
        tvcodigo.setText(item.getMethodId());
        tvtipo.setText(item.getName());
        tvmonto.setText(formatea.format(item.getAmount()));
        tvmoneda.setText(item.getCurrencyId());

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e("logcat", "onAttach: ProductoGripDialogFragment: " + e.getMessage() );
        }
    }
}

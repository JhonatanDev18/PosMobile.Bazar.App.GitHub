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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.MediosCaja;
import com.crystal.bazarposmobile.ui.adapters.MedioPagoSelectRecyclerViewAdapter;

import java.util.List;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class MedioPagoSelecDialogFragment extends DialogFragment {

    //Declaración de los objetos de la interfaz del DialogFragment
    private View view;
    private ListView listviewmediospago;

    //Declaración de la variables del DialogFragment
    List<MediosCaja> mediosCajaList;
    String tipo;

    public MedioPagoSelecDialogFragment(String tipo, List<MediosCaja> mediosCajaList) {
        this.mediosCajaList = mediosCajaList;
        this.tipo = tipo;
    }

    public interface OnInputListener{
        void sendInputListMedioPagoSelecDialogFragment(String tipo,MediosCaja item);
    }
    public OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(tipo);
        builder.setMessage(getResources().getString(R.string.select_medio_pago))
                .setIcon(R.mipmap.medio_pago_seleccion)
                .setNegativeButton(R.string.volver, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //Asignacion de Referencias
        view = inflater.inflate(R.layout.dialog_fragment_medio_pago_select_dialog, null);
        listviewmediospago = view.findViewById(R.id.lvMPSDF);

        //Adaptador con la lista de los medios de pagos
        MedioPagoSelectRecyclerViewAdapter adapterMedioPagoSelec = new MedioPagoSelectRecyclerViewAdapter(requireContext(),mediosCajaList);
        listviewmediospago.setAdapter(adapterMedioPagoSelec);

        //Seleccion del medio de pago
        listviewmediospago.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(mediosCajaList.get(position).getIsEnabled()){
                    MediosCaja item = mediosCajaList.get(position);

                    if(Utilidades.isConsumidorFinal()){
                        if(SPM.getString(Constantes.MEDIOS_PAGOS_CREDITOS).contains(item.getCodigo())){
                            Utilidades.mjsToast("Medio de pago no autorizado para CLIENTE GENÉRICO y/o MODO AUTÓNOMO",
                                    Constantes.TOAST_TYPE_INFO, Toast.LENGTH_LONG, getContext());
                        }else{
                            if(Constantes.MP_AUTORIZACION.contains(item.getCodigo())){
                                AutorizacionDialogFragment autoDF = new AutorizacionDialogFragment(Integer.toString(position));
                                autoDF.show(getParentFragmentManager(), "AutorizacionDialogFragment");
                                Objects.requireNonNull(getDialog()).dismiss();
                            }else{
                                mOnInputListener.sendInputListMedioPagoSelecDialogFragment(tipo,item);
                                Objects.requireNonNull(getDialog()).dismiss();
                            }
                        }
                    }else{
                        if(Constantes.MP_AUTORIZACION.contains(item.getCodigo())){
                            AutorizacionDialogFragment autoDF = new AutorizacionDialogFragment(Integer.toString(position));
                            autoDF.show(getParentFragmentManager(), "AutorizacionDialogFragment");
                            Objects.requireNonNull(getDialog()).dismiss();
                        }else{
                            mOnInputListener.sendInputListMedioPagoSelecDialogFragment(tipo,item);
                            Objects.requireNonNull(getDialog()).dismiss();
                        }
                    }
                }else{
                    String msjbase = getResources().getString(R.string.medio_pago_no_usar);
                    String msjbaseFormateada = String.format(msjbase, mediosCajaList.get(position).getNombre());
                    msjToast(msjbaseFormateada);
                }
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
            Log.e("logcat", "onAttach: MedioPagoSelecDialogFragment: " + e.getMessage() );
        }
    }

    private void msjToast(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }

}

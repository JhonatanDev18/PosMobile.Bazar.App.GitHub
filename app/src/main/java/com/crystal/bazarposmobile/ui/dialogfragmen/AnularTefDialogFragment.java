package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.Utilidades;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.RespuestaCompletaTef;
import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.ui.adapters.ListaAnulacionRecyclerViewAdapter;

import java.util.Comparator;
import java.util.List;

public class AnularTefDialogFragment extends DialogFragment {
    private View view;
    private ImageView ivCloseAnulacion;
    private List<RespuestaCompletaTef> respuestaDatafonoList;
    private ListView lvAnulacionSelect;
    private Context contexto;

    public AnularTefDialogFragment(List<RespuestaCompletaTef> respuestaDatafonoList) {
        this.respuestaDatafonoList = respuestaDatafonoList;
    }

    public interface OnInputListener{
        void sendInputItemAnularSelectDialogFragment(RespuestaCompletaTef respuestaDatafono);
    }

    public OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_anular_tef, null);

        inicializar();
        cargarDatos();
        eventos();

        setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    private void cargarDatos() {
        // Definir un comparador personalizado para ordenar en orden descendente
        Comparator<RespuestaCompletaTef> comparador = (o1, o2) -> {
            int numero1 = Integer.parseInt(o1.getRespuestaTef().getRecibo());
            int numero2 = Integer.parseInt(o2.getRespuestaTef().getRecibo());
            return Integer.compare(numero2, numero1);
        };

        respuestaDatafonoList.sort((comparador));

        ListaAnulacionRecyclerViewAdapter adapter =
                new ListaAnulacionRecyclerViewAdapter(requireContext(),respuestaDatafonoList);
        lvAnulacionSelect.setAdapter(adapter);
    }

    private void inicializar() {
        contexto = requireContext();
        ivCloseAnulacion = view.findViewById(R.id.ivCloseAnulacion);
        lvAnulacionSelect = view.findViewById(R.id.lvAnulacionSelect);
    }

    private void eventos() {
        lvAnulacionSelect.setOnItemClickListener((parent, view, position, id) -> {
            try{
                mOnInputListener.sendInputItemAnularSelectDialogFragment(respuestaDatafonoList.get(position));
            }catch (Exception e){
                Utilidades.mjsToast(e.getMessage(),
                        Constantes.TOAST_TYPE_INFO, Toast.LENGTH_LONG, requireContext());
            }
        });

        ivCloseAnulacion.setOnClickListener(this::cerrar);
    }

    private void cerrar(View view){
        dismiss();
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (AnularTefDialogFragment.OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e("logcat", "onAttach: TiendaSelectDialogFragment: " + e.getMessage() );
        }
    }
}

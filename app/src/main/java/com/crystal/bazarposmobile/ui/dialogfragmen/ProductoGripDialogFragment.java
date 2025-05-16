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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;

import java.util.Objects;

@SuppressLint("ValidFragment")
public class ProductoGripDialogFragment extends DialogFragment {

    //Declaración de la variables del DialogFragment
    private Producto item;
    private int posicion;

    //Declaración de los objetos de la interfaz del DialogFragment
    private View view;
    private EditText etcantidad;
    private ImageView btnEliminar,btnadd,btnremove;

    public ProductoGripDialogFragment(Producto itemProducto, int position) {
        this.item = itemProducto;
        this.posicion = position;
    }

    public interface OnInputListener{
        void sendInputItemProductoDialogFragment(Integer cant, Integer inputposition, boolean eliminar);
    }
    public OnInputListener mOnInputListener;

    @SuppressLint("SetTextI18n")
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Obtener los datos del producto
        String nombre = item.getNombre();

        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(nombre);
        builder.setMessage(getResources().getString(R.string.cambios_articulo))
                .setIcon(R.mipmap.precio_producto)
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
        view = inflater.inflate(R.layout.dialog_fragment_producto_grip, null);
        etcantidad = view.findViewById(R.id.etCantidadPGDF);
        btnEliminar = view.findViewById(R.id.ivEliminarPGDF);
        btnadd = view.findViewById(R.id.ivAddPGDF);
        btnremove = view.findViewById(R.id.ivRemovePGDF);

        etcantidad.setText(item.getQuantity().toString());

        //Boton Eliminar, usado para eliminar una linea
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnInputListener.sendInputItemProductoDialogFragment(null,posicion,true);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        //Boton sumar cantidad
        btnadd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(etcantidad.getText().toString()) + 1;
                if(c == 0){
                    c++;
                }
                etcantidad.setText(Integer.toString(c));
            }
        });

        //Boton restar cantidad
        btnremove.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(etcantidad.getText().toString()) - 1;
                if(c == 0){
                    c--;
                }
                etcantidad.setText(Integer.toString(c));
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
                    if(Integer.parseInt(etcantidad.getText().toString()) != 0){
                        mOnInputListener.sendInputItemProductoDialogFragment(Integer.parseInt(etcantidad.getText().toString()),posicion,false);
                        d.dismiss();
                    }else{
                        msjToast("Cantidad inválida.");
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
            Log.e("logcat", "onAttach: ProductoGripDialogFragment: " + e.getMessage() );
        }
    }

    private void msjToast(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }
}
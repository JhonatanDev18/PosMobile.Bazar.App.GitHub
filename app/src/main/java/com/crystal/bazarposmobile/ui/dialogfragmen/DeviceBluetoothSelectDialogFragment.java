package com.crystal.bazarposmobile.ui.dialogfragmen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.crystal.bazarposmobile.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

@SuppressLint("ValidFragment")
public class DeviceBluetoothSelectDialogFragment extends DialogFragment {

    //Declaración de los objetos de la interfaz del DialogFragment
    private View view;
    private ListView lvdevicebluetooth;
    private TextView tvmostrartodos;

    //Declaración de la variables del DialogFragment
    ArrayList<String> bluetoothNameList;
    ArrayList<String> bluetoothMacList;
    BluetoothAdapter mBtAdapter;

    public DeviceBluetoothSelectDialogFragment(){
    }

    public interface OnInputListener{
        void sendInputListSelectDeviceBluetoothDialogFragment(String mac);
    }
    public DeviceBluetoothSelectDialogFragment.OnInputListener mOnInputListener;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use la clase Builder para la construcción conveniente del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dispositivo_bluetooth);
        builder.setMessage(getResources().getString(R.string.select_impresora_blue))
                .setIcon(R.mipmap.blietooth)
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fragment_divice_bluetooth_select, null);
        tvmostrartodos = view.findViewById(R.id.tvMostrarTodosDBSDF);

        lvdevicebluetooth = view.findViewById(R.id.lvDBSDF);
        bluetoothNameList = new ArrayList<String>();
        bluetoothMacList = new ArrayList<String>();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Obtenga un conjunto de dispositivos actualmente emparejados
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Si hay dispositivos emparejados, agregue cada uno al ArrayAdapter
        if (pairedDevices.size() > 0) {
            //Vaciar las listas
            bluetoothNameList.clear();
            bluetoothMacList.clear();

            //Añadir al RadioGroup los dispositivos
            for (BluetoothDevice device : pairedDevices) {
                if (isAPrinter(device)) {
                    bluetoothNameList.add(device.getName() + "\n" + device.getAddress());
                    bluetoothMacList.add(device.getAddress());
                }
            }
        }

        //Adaptador con la lista de descuento
        ArrayAdapter adapterDeviceBluetooth = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                bluetoothNameList
        );
        lvdevicebluetooth.setAdapter(adapterDeviceBluetooth);

        //Seleccion del descuento
        lvdevicebluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mac = bluetoothMacList.get(position);
                mOnInputListener.sendInputListSelectDeviceBluetoothDialogFragment(mac);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        tvmostrartodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenga un conjunto de dispositivos actualmente emparejados
                Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

                // Si hay dispositivos emparejados, agregue cada uno al ArrayAdapter
                if (pairedDevices.size() > 0) {
                    //Vaciar las listas
                    bluetoothNameList.clear();
                    bluetoothMacList.clear();

                    //Añadir al RadioGroup los dispositivos
                    for (BluetoothDevice device : pairedDevices) {
                        bluetoothNameList.add(device.getName() + "\n" + device.getAddress());
                        bluetoothMacList.add(device.getAddress());
                    }
                }
                //Adaptador con la lista de descuento
                ArrayAdapter adapterDeviceBluetooth = new ArrayAdapter(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        bluetoothNameList
                );
                lvdevicebluetooth.setAdapter(adapterDeviceBluetooth);
                tvmostrartodos.setVisibility(View.GONE);
            }
        });

        setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    private static boolean isAPrinter(BluetoothDevice device){
        int priterMask = 0b000001000000011010000000;
        int fullCod = device.getBluetoothClass().hashCode();
        return (fullCod & priterMask) == priterMask;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e("logcat", "onAttach: DeviceBluetoothSelectDialogFragment: " + e.getMessage() );
        }
    }
}
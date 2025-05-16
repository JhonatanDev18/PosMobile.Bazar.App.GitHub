package com.crystal.bazarposmobile.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.MediosCaja;

import java.util.List;

public class MedioPagoSelectRecyclerViewAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context context;
    List<MediosCaja> listMP;
    View view;
    TextView  tvnombre;
    LinearLayout linearLayoutMP;

    public MedioPagoSelectRecyclerViewAdapter(Context context, List<MediosCaja> listMP) {
        this.context = context;
        this.listMP = listMP;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        view = inflater.inflate(R.layout.fragment_medios_pago_list_item, null);
        tvnombre = view.findViewById(R.id.tvMedioPagoSelectAdapter);
        linearLayoutMP = view.findViewById(R.id.linearLayoutMedioPagoSelectAdapter);

        tvnombre.setText(listMP.get(position).getNombre());
        if(listMP.get(position).getIsEnabled()){
            linearLayoutMP.setBackgroundColor(Color.parseColor("#D4FFD4"));
        }else{
            linearLayoutMP.setBackgroundColor(Color.parseColor("#FFC6B6"));
        }

        return view;
    }

    @Override
    public int getCount() {
        return listMP.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

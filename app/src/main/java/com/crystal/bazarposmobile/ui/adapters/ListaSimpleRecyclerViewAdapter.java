package com.crystal.bazarposmobile.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crystal.bazarposmobile.R;

import java.util.List;

public class ListaSimpleRecyclerViewAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context context;
    List<String> listLS;
    View viewLS;
    TextView tvnombreLS;
    LinearLayout linearLayoutLS;

    public ListaSimpleRecyclerViewAdapter(Context context, List<String> listLS) {
        this.context = context;
        this.listLS = listLS;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewLS = inflater.inflate(R.layout.fragment_lista_simple, null);
        tvnombreLS = viewLS.findViewById(R.id.tvLayoutListaSimpleAdapter);
        linearLayoutLS = viewLS.findViewById(R.id.linearLayoutListaSimple);

        tvnombreLS.setText(listLS.get(position));
        return viewLS;
    }


    @Override
    public int getCount() {
        return listLS.size();
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

package com.crystal.bazarposmobile.ui.adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.ui.fragment.ProductoSelectGripFragment;

import java.text.DecimalFormat;
import java.util.List;

public class ProductoSelectGripRecyclerViewAdapter extends RecyclerView.Adapter<ProductoSelectGripRecyclerViewAdapter.ViewHolder> {


    private List<ProductoEntity> mValues;
    private final ProductoSelectGripFragment.OnListFragmentInteractionListener mListener;
    private final DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    public ProductoSelectGripRecyclerViewAdapter(List<ProductoEntity> items, ProductoSelectGripFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_producto_select_grip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);

        String nombre = holder.mItem.getEan() + " - " + holder.mItem.getNombre();
        if (holder.mItem.getTalla() != null) {
            nombre = nombre + " " + holder.mItem.getTalla();
        }
        if (holder.mItem.getColor() != null) {
            nombre = nombre + " " + holder.mItem.getColor();
        }

        String precio = formatea.format(holder.mItem.getPrecio());

        holder.tvNombre.setText(nombre);
        holder.tvPrecio.setText(precio);

        Integer line = holder.mItem.getLine();
        holder.tvLine.setText(line.toString());

        if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#bac9d6"));
        }else{
            holder.linearLayout.setBackgroundColor(Color.parseColor("#d5e6f5"));
        }

        final int p = position;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem,p);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvNombre;
        public final TextView tvPrecio;
        public final TextView tvLine;
        public final ConstraintLayout linearLayout;
        public ProductoEntity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvLine = view.findViewById(R.id.tvLinePSGF);
            tvNombre = view.findViewById(R.id.tvNombrePSGF);
            tvPrecio = view.findViewById(R.id.tvPrecioPSGF);
            linearLayout = view.findViewById(R.id.clPSGF);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNombre.getText() + "'";
        }
    }
}

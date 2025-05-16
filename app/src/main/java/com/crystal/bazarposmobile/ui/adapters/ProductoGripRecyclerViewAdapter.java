package com.crystal.bazarposmobile.ui.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.retrofit.response.eanes.Producto;
import com.crystal.bazarposmobile.ui.fragment.ProductoGripFragment.OnListFragmentInteractionListener;

import java.text.DecimalFormat;
import java.util.List;

public class ProductoGripRecyclerViewAdapter extends RecyclerView.Adapter<ProductoGripRecyclerViewAdapter.ViewHolder> {

    private List<Producto> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    public ProductoGripRecyclerViewAdapter(List<Producto> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_producto_grip, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String nombre = holder.mItem.getNombre();
        if (holder.mItem.getTalla() != null) {
            nombre = nombre + " " + holder.mItem.getTalla();
        }
        if (holder.mItem.getColor() != null) {
            nombre = nombre + " " + holder.mItem.getColor();
        }

        String precio = formatea.format(holder.mItem.getPrecio());
        String total  = formatea.format(Math.abs(holder.mItem.getPrecio())*holder.mItem.getQuantity());

        holder.tvNombre.setText(nombre);
        holder.tvPrecio.setText(precio);
        holder.tvCantidad.setText(holder.mItem.getQuantity().toString());
        holder.tvTotal.setText(total);

        if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#CEECFF"));
        }else{
            holder.linearLayout.setBackgroundColor(Color.parseColor("#B0E1E8"));
        }

        if(holder.mItem.getQuantity() < 0){
            if ((position % 2) == 0) {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#FFC6B6"));
            }else{
                holder.linearLayout.setBackgroundColor(Color.parseColor("#fcb9a7"));
            }
        }

        final int p = position;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
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
        public final TextView tvCantidad;
        public final TextView tvTotal;
        public final ConstraintLayout linearLayout;
        public Producto mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvNombre = view.findViewById(R.id.tvNombreProductoAdapter);
            tvPrecio = view.findViewById(R.id.tvPrecioProductoAdapter);
            tvCantidad = view.findViewById(R.id.tvCantidadProductoAdapter);
            tvTotal = view.findViewById(R.id.tvTotalProductoAdapter);
            linearLayout = view.findViewById(R.id.clProductoGripAdapter);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNombre.getText() + "'";
        }
    }
}

package com.crystal.bazarposmobile.ui.adapters;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.db.entity.TEFContinguenciaEntity;
import com.crystal.bazarposmobile.ui.fragment.TEFContFragment;

import java.text.DecimalFormat;
import java.util.List;

public class TEFContRecyclerViewAdapter extends RecyclerView.Adapter<TEFContRecyclerViewAdapter.ViewHolder> {

    private List<TEFContinguenciaEntity> mValues;
    private final TEFContFragment.OnListFTEFContIL mListener;
    private final DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    public TEFContRecyclerViewAdapter(List<TEFContinguenciaEntity> items, TEFContFragment.OnListFTEFContIL listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public TEFContRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tefcont, parent, false);
        return new TEFContRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TEFContRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String nombre = holder.mItem.getNombre();
        nombre = nombre.replace(" - ", "\n");

        String fecha = holder.mItem.getFecha();
        if(fecha.length() > 10){
            fecha = fecha.substring(0,10);
        }

        String iva = formatea.format(holder.mItem.getIva());
        String monto  = formatea.format(holder.mItem.getMonto());

        holder.tvFecha.setText(fecha);
        holder.tvNombre.setText(nombre);
        holder.tvIva.setText(iva);
        holder.tvMonto.setText(monto);

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
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFTEFContIL(holder.mItem,p);
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
        public final TextView tvFecha;
        public final TextView tvNombre;
        public final TextView tvIva;
        public final TextView tvMonto;
        public final ConstraintLayout linearLayout;
        public TEFContinguenciaEntity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvFecha = view.findViewById(R.id.tvFechaTEFContGripAdapter);
            tvNombre = view.findViewById(R.id.tvNombreTEFContGripAdapter);
            tvIva = view.findViewById(R.id.tvIvaTEFContGripAdapter);
            tvMonto = view.findViewById(R.id.tvMontoTEFContGripAdapter);
            linearLayout = view.findViewById(R.id.clTEFContGripAdapter);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNombre.getText() + "'";
        }
    }
}
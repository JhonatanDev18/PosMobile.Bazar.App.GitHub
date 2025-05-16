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
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.ui.fragment.DocHeaderFragment;

import java.text.DecimalFormat;
import java.util.List;

public class DocHeaderRecyclerViewAdapter extends RecyclerView.Adapter<DocHeaderRecyclerViewAdapter.ViewHolder> {

    private List<DocHeaderEntity> mValues;
    private final DocHeaderFragment.OnListDocHeader mListener;
    private final DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    public DocHeaderRecyclerViewAdapter(List<DocHeaderEntity> items, DocHeaderFragment.OnListDocHeader listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public DocHeaderRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_docheader, parent, false);
        return new DocHeaderRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DocHeaderRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String intenalR = holder.mItem.getInternalReference();
        String fecha = holder.mItem.getDate();
        if(fecha.length() > 10){
            fecha = fecha.substring(0,10);
        }

        holder.tvFecha.setText(fecha);
        holder.tvIR.setText(intenalR);
        String cant = holder.mItem.getCantidad().toString();
        holder.tvCantidad.setText(cant);
        holder.tvTotal.setText(formatea.format(holder.mItem.getTotal()));

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
                    mListener.onListDocHeader(holder.mItem,p);
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
        public final TextView tvIR;
        public final TextView tvCantidad;
        public final TextView tvTotal;
        public final ConstraintLayout linearLayout;
        public DocHeaderEntity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvFecha = view.findViewById(R.id.tvFechaDHGA);
            tvIR = view.findViewById(R.id.tvInternalReferenceDHGA);
            tvCantidad = view.findViewById(R.id.tvCantidadDHGA);
            tvTotal = view.findViewById(R.id.tvTotalDHGA);
            linearLayout = view.findViewById(R.id.clDocHeaderGripAdapter);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvIR.getText() + "'";
        }
    }
}
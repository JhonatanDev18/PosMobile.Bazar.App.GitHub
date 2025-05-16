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
import com.crystal.bazarposmobile.retrofit.request.creardocumento.Payment;
import com.crystal.bazarposmobile.ui.fragment.ImporteGripFragment.OnListFragmentInteractionListener;

import java.text.DecimalFormat;
import java.util.List;


public class ImporteGripRecyclerViewAdapter extends RecyclerView.Adapter<ImporteGripRecyclerViewAdapter.ViewHolder> {

    private final List<Payment> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    public ImporteGripRecyclerViewAdapter(List<Payment> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_importegrip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCodigo.setText(mValues.get(position).getMethodId());
        holder.mNombre.setText(mValues.get(position).getName());
        holder.mMonto.setText(formatea.format(mValues.get(position).getAmount()));

        if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#CEECFF"));
        }else{
            holder.linearLayout.setBackgroundColor(Color.parseColor("#B0E1E8"));
        }

        if(holder.mItem.getAmount() < 0){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FFC6B6"));
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
        public final TextView mCodigo;
        public final TextView mNombre;
        public final TextView mMonto;
        public final ConstraintLayout linearLayout;
        public Payment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCodigo = view.findViewById(R.id.tvCodigoImporteGripAdapter);
            mNombre = view.findViewById(R.id.tvNombreImporteGripAdapter);
            mMonto = view.findViewById(R.id.tvMontoImporteGripAdapter);
            linearLayout = view.findViewById(R.id.clImporteGripAdapter);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }
}

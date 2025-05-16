package com.crystal.bazarposmobile.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.retrofit.response.suspensiones.SuspensionItem;
import com.crystal.bazarposmobile.ui.fragment.SuspensionesGripFragment;

import java.util.List;

public class SuspensionesGripRecyclerViewAdapter extends RecyclerView.Adapter<SuspensionesGripRecyclerViewAdapter.ViewHolder> {

    private List<SuspensionItem> mValues;
    private final SuspensionesGripFragment.OnLFISuspensionesGripListener mListener;

    public SuspensionesGripRecyclerViewAdapter(List<SuspensionItem> items, SuspensionesGripFragment.OnLFISuspensionesGripListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_suspensiones_grip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String text = holder.mItem.getTexto();
        holder.tvText.setText(text);

        String refe = holder.mItem.getReferencia();
        holder.tvReferencia.setText(refe);

        holder.tvCant.setText(holder.mItem.getCantidad().toString());

        if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#CEECFF"));
        }else{
            holder.linearLayout.setBackgroundColor(Color.parseColor("#B0E1E8"));
        }

        final int p = position;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.OnLFILSuspensiones(holder.mItem,p);
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
        public final TextView tvText;
        public final TextView tvCant;
        public final TextView tvReferencia;
        public final ConstraintLayout linearLayout;
        public SuspensionItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvText = view.findViewById(R.id.tvTextClienteSusG);
            tvCant = view.findViewById(R.id.tvTotalQuantitySusG);
            tvReferencia = view.findViewById(R.id.tvInternalReferenceSusG);
            linearLayout = view.findViewById(R.id.clSusG);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvReferencia.getText() + "'";
        }
    }
}

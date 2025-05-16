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
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.ui.fragment.ComprobanteTEFGripFragment;

import java.text.DecimalFormat;
import java.util.List;

public class ComprobanteTEFGripRecyclerViewAdapter extends RecyclerView.Adapter<ComprobanteTEFGripRecyclerViewAdapter.ViewHolder> {

    private List<DatafonoEntity> mValues;
    private final ComprobanteTEFGripFragment.OnLFIComprobanteTEF mListener;
    private final DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    public ComprobanteTEFGripRecyclerViewAdapter(List<DatafonoEntity> items, ComprobanteTEFGripFragment.OnLFIComprobanteTEF listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_comprobante_tef_grip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.tvFranquicia.setText(holder.mItem.getFranquicia());
        holder.tvPrecio.setText(formatea.format(holder.mItem.getMonto()));
        holder.tvIva.setText(formatea.format(holder.mItem.getIva()));
        holder.tvNombre.setText(holder.mItem.getIdCliente());

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
                    mListener.OnLFILDocDatafonoEntity(holder.mItem,p);
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
        public final TextView tvFranquicia;
        public final TextView tvIva;
        public final TextView tvPrecio;

        public final ConstraintLayout linearLayout;
        public DatafonoEntity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tvFranquicia = view.findViewById(R.id.tvFranquiciaCTEFG);
            tvIva = view.findViewById(R.id.tvIvaCTEFG);
            tvPrecio = view.findViewById(R.id.tvPrecioCTEFG);
            tvNombre= view.findViewById(R.id.tvNombreCTEFG);

            linearLayout = view.findViewById(R.id.clCTEFG);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNombre.getText() + "'";
        }
    }
}

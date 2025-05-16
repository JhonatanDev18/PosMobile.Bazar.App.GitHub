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
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.DocumentoDetalle;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.LineDD;
import com.crystal.bazarposmobile.ui.fragment.DocumentoDetalleGripFragment;

import java.text.DecimalFormat;
import java.util.List;

public class DocDetalleGripRecyclerViewAdapter extends RecyclerView.Adapter<DocDetalleGripRecyclerViewAdapter.ViewHolder> {

    private List<DocumentoDetalle> mValues;
    private final DocumentoDetalleGripFragment.OnLFIDocDetallesGripListener mListener;
    private final DecimalFormat formatea = new DecimalFormat(Constantes.FORMATO_DECIMAL);

    public DocDetalleGripRecyclerViewAdapter(List<DocumentoDetalle> items, DocumentoDetalleGripFragment.OnLFIDocDetallesGripListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doc_detalle_grip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String refe = holder.mItem.getHeaderDD().getInternalReference();
        holder.tvReferencia.setText(refe);

        Double cant = Double.parseDouble(holder.mItem.getHeaderDD().getTotalQuantity());
        Integer cantInt = cant.intValue();
        holder.tvCant.setText(cantInt.toString());
        Double total = Double.parseDouble(holder.mItem.getHeaderDD().getTaxIncludedTotalAmount());
        holder.tvPrecio.setText(formatea.format(total));

        List<LineDD> lineDDS = holder.mItem.getLineDDS();
        String eanes = "";
        String cantidades = "";
        String totales = "";
        for(LineDD line:lineDDS){
            cant = Double.parseDouble(line.getQuantity());
            cantInt = cant.intValue();
            total = Double.parseDouble(line.getTaxIncludedUnitPrice());
            String te = line.getItemReference()+" "+line.getLabel();
            if(te.length() > 20){
                te = te.substring(0,20);
                te = te + "...";
            }

            eanes = eanes + te + "\n";
            cantidades = cantidades + cantInt.toString()+"\n";
            totales = totales + formatea.format(total)+"\n";
        }
        holder.tvCantidades.setText(cantidades);
        holder.tvTotales.setText(totales);
        holder.tvEanes.setText(eanes);

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
                    mListener.OnLFILDocDetalles(holder.mItem,p);
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
        public final TextView tvCant;
        public final TextView tvPrecio;
        public final TextView tvReferencia;
        public final TextView tvCantidades;
        public final TextView tvTotales;
        public final TextView tvEanes;

        public final ConstraintLayout linearLayout;
        public DocumentoDetalle mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tvCantidades = view.findViewById(R.id.tvCantidadesDocDetalleG);
            tvTotales = view.findViewById(R.id.tvTotalesDocDetalleG);
            tvEanes = view.findViewById(R.id.tvEanesDocDetalleG);

            tvCant = view.findViewById(R.id.tvQuantityDocDetalleG);
            tvPrecio = view.findViewById(R.id.tvTotalDocDetalleG);
            tvReferencia = view.findViewById(R.id.tvInternalReferenceDocDetalleG);
            linearLayout = view.findViewById(R.id.clDocDetalleG);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvReferencia.getText() + "'";
        }
    }
}

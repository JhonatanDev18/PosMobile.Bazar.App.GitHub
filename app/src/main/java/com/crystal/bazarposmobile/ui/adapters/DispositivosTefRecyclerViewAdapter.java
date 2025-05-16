package com.crystal.bazarposmobile.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.IConfigurableActivity;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.common.TipoDatafono;
import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Utilidades;

import java.util.List;

public class DispositivosTefRecyclerViewAdapter extends RecyclerView.Adapter<DispositivosTefRecyclerViewAdapter.ViewHolder>{
    private final List<TipoDatafono> datafonoList;
    private final IConfigurableActivity activity;

    public DispositivosTefRecyclerViewAdapter(List<TipoDatafono> items, IConfigurableActivity activity) {
        datafonoList = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_tipo_datafono, parent, false);
        return new DispositivosTefRecyclerViewAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTipoDatafono.setText(datafonoList.get(position).getNombreDispositivo());

        if(datafonoList.get(position).isActivo()){
            holder.tvEstadoDispositivo.setText(holder.itemView.getContext().getResources().getString(R.string.dispositivo_activo));
        }else{
            holder.tvEstadoDispositivo.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.errorColor));
            holder.tvEstadoDispositivo.setText(holder.itemView.getContext().getResources().getString(R.string.dispositivo_inactivo));
        }

        holder.rlFondoDispositivosTef.setOnClickListener(view -> {
            if(datafonoList.get(position).isActivo()){
                activity.tipoComunicacionDatafono(datafonoList.get(position));
            }else{
                Utilidades.mjsToast("Dispositivo no disponible", Constantes.TOAST_TYPE_INFO,
                        Toast.LENGTH_LONG, holder.itemView.getContext());
            }
            applyClickAnimation(holder.ivDispositivoTef);
        });

        TipoDatafono tipoDatafono = (TipoDatafono) SPM.getObject(Constantes.OBJECT_TIPO_DATAFONO, TipoDatafono.class);
        if(tipoDatafono != null){
            if(datafonoList.get(position).getNombreDispositivo().equals(tipoDatafono.getNombreDispositivo())){
                holder.ivDispositivoTef.setImageResource(R.drawable.datafono_habilitado);
            }
        }
    }

    private void applyClickAnimation(View view) {
        AlphaAnimation clickAnimation = new AlphaAnimation(1.0f, 0.5f);
        clickAnimation.setDuration(100);
        view.startAnimation(clickAnimation);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvTipoDatafono, tvEstadoDispositivo;
        public final ConstraintLayout rlFondoDispositivosTef;
        public final ImageView ivDispositivoTef;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivDispositivoTef = view.findViewById(R.id.ivDispositivoTef);
            tvTipoDatafono = view.findViewById(R.id.tvTipoDatafono);
            tvEstadoDispositivo = view.findViewById(R.id.tvEstadoDispositivo);
            rlFondoDispositivosTef = view.findViewById(R.id.rlFondoDispositivosTef);
        }
    }

    @Override
    public int getItemCount() {
        return datafonoList.size();
    }
}

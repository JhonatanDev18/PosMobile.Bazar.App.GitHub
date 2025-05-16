package com.crystal.bazarposmobile.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.DocumentoDetalle;
import com.crystal.bazarposmobile.ui.adapters.DocDetalleGripRecyclerViewAdapter;

import java.util.List;

public class DocumentoDetalleGripFragment extends Fragment {

    RecyclerView recyclerView;
    DocDetalleGripRecyclerViewAdapter adapter;

    private int mColumnCount = 1;
    private OnLFIDocDetallesGripListener mListener;

    public DocumentoDetalleGripFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc_detalle_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLFIDocDetallesGripListener) {
            mListener = (OnLFIDocDetallesGripListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setDocDetalles(List<DocumentoDetalle> documentoDetalleListalle){
        this.adapter = new DocDetalleGripRecyclerViewAdapter(documentoDetalleListalle, mListener);
        recyclerView.setAdapter(this.adapter);
    }

    public interface OnLFIDocDetallesGripListener {
        void OnLFILDocDetalles(DocumentoDetalle mItem, int p);
    }
}

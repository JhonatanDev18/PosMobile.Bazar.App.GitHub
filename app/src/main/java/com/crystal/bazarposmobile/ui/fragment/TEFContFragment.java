package com.crystal.bazarposmobile.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.db.entity.TEFContinguenciaEntity;
import com.crystal.bazarposmobile.ui.adapters.TEFContRecyclerViewAdapter;

import java.util.List;

public class TEFContFragment extends Fragment {

    RecyclerView recyclerView;
    TEFContRecyclerViewAdapter adapter;
    private int mColumnCount = 1;
    private OnListFTEFContIL mListener;
    public TEFContFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setPagosTEFCont(List<TEFContinguenciaEntity> listTEFConti){
        this.adapter = new TEFContRecyclerViewAdapter(listTEFConti, mListener);
        recyclerView.setAdapter(this.adapter);
    }

    public static TEFContFragment newInstance(){
        TEFContFragment fragment = new TEFContFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tefcont_list, container, false);

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
        if (context instanceof OnListFTEFContIL) {
            mListener = (OnListFTEFContIL) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFTEFContIL");
        }
    }

    public interface OnListFTEFContIL {
        void onListFTEFContIL(TEFContinguenciaEntity item, Integer position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
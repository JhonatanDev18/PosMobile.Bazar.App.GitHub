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
import com.crystal.bazarposmobile.retrofit.response.suspensiones.SuspensionItem;
import com.crystal.bazarposmobile.ui.adapters.SuspensionesGripRecyclerViewAdapter;

import java.util.List;

public class SuspensionesGripFragment extends Fragment {

    RecyclerView recyclerView;
    SuspensionesGripRecyclerViewAdapter adapter;

    private int mColumnCount = 1;
    private OnLFISuspensionesGripListener mListener;

    public SuspensionesGripFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suspensiones_list, container, false);

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
        if (context instanceof OnLFISuspensionesGripListener) {
            mListener = (OnLFISuspensionesGripListener) context;
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

    public void setSuspensiones(List<SuspensionItem> suspensiones){
        this.adapter = new SuspensionesGripRecyclerViewAdapter(suspensiones, mListener);
        recyclerView.setAdapter(this.adapter);
    }

    public interface OnLFISuspensionesGripListener {
        void OnLFILSuspensiones(SuspensionItem mItem, int p);
    }
}

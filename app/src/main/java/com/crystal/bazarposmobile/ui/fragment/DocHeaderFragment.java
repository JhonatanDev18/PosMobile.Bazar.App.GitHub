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
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.ui.adapters.DocHeaderRecyclerViewAdapter;

import java.util.List;

public class DocHeaderFragment extends Fragment {

    RecyclerView recyclerView;
    DocHeaderRecyclerViewAdapter adapter;
    private int mColumnCount = 1;
    private OnListDocHeader mListener;

    public DocHeaderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setDocHeader(List<DocHeaderEntity> listDocHeader){
        this.adapter = new DocHeaderRecyclerViewAdapter(listDocHeader, mListener);
        recyclerView.setAdapter(this.adapter);
    }

    public static DocHeaderFragment newInstance(){
        DocHeaderFragment fragment = new DocHeaderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_docheader_list, container, false);

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
        if (context instanceof OnListDocHeader) {
            mListener = (OnListDocHeader) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListDocHeader");
        }
    }

    public interface OnListDocHeader {
        void onListDocHeader(DocHeaderEntity item, Integer position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
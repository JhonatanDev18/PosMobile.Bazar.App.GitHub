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
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.ui.adapters.ProductoSelectGripRecyclerViewAdapter;

import java.util.List;

public class ProductoSelectGripFragment extends Fragment {

    RecyclerView recyclerView;
    ProductoSelectGripRecyclerViewAdapter adapter;

    private int mColumnCount = 1;

    private ProductoSelectGripFragment.OnListFragmentInteractionListener mListener;

    public ProductoSelectGripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setProductoSelect(List<ProductoEntity> productos){
        this.adapter = new ProductoSelectGripRecyclerViewAdapter(productos, mListener);
        recyclerView.setAdapter(this.adapter);
    }

    public static ProductoSelectGripFragment newInstance(){
        ProductoSelectGripFragment fragment = new ProductoSelectGripFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_grip_list, container, false);

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
        if (context instanceof ProductoSelectGripFragment.OnListFragmentInteractionListener) {
            mListener = (ProductoSelectGripFragment.OnListFragmentInteractionListener) context;
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ProductoEntity item, Integer position);
    }

}

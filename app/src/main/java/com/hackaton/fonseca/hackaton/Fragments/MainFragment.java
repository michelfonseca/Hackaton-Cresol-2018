package com.hackaton.fonseca.hackaton.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hackaton.fonseca.hackaton.Adapter.GastosItens;
import com.hackaton.fonseca.hackaton.Adapter.ItemAdapter;
import com.hackaton.fonseca.hackaton.HomeActivity;
import com.hackaton.fonseca.hackaton.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }

    ItemAdapter mAdapter = new ItemAdapter(new ArrayList<GastosItens>());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        ArrayList<GastosItens> gastoLista = new ArrayList<>();
        gastoLista.add(new GastosItens(R.mipmap.ic_talher, "Restaurante", "(-) R$50,00"));
        gastoLista.add(new GastosItens(R.mipmap.ic_bolsa, "Supermercado", "(-) R$65,00"));
        gastoLista.add(new GastosItens(R.mipmap.ic_dinheiro, "Depósito", "(+) R$1250,00"));
        gastoLista.add(new GastosItens(R.mipmap.ic_bolsa, "Supermercado", "(-) R$83,15"));

        RecyclerView mRecyclerView = view.findViewById(R.id.listaGastos);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ItemAdapter(gastoLista);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        ((HomeActivity) getActivity())
                .setActionBarSubtitle("Cresol");

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}

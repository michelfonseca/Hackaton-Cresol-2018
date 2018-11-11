package com.hackaton.fonseca.hackaton.Fragments;


import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.hackaton.fonseca.hackaton.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment {

    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        Spinner categoriaGastos = view.findViewById(R.id.categoriaGastos);
        MaterialEditText valorInicial = view.findViewById(R.id.vlrInicial);
        MaterialEditText valorMensal = view.findViewById(R.id.vlrMensal);
        Button sll = view.findViewById(R.id.btnSalvar);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.categoria));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaGastos.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}

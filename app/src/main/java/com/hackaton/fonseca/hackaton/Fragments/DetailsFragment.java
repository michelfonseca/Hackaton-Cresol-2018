package com.hackaton.fonseca.hackaton.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.hackaton.fonseca.hackaton.Function.Function;
import com.hackaton.fonseca.hackaton.HomeActivity;
import com.hackaton.fonseca.hackaton.R;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    WebView webView;
    ProgressBar loader;
    String url = "";


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_details, container, false);

        url = getArguments().getString("url");
        webView = (WebView)vw.findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl(url);

        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#009688"));
        pDialog.setTitleText("Carregando");
        pDialog.setCancelable(false);


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    loader.setVisibility(View.GONE);
                } else {
                    loader.setVisibility(View.VISIBLE);
                }
            }
        });

        // Inflate the layout for this fragment
        return vw;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_site, menu);((HomeActivity) getActivity())
                .setActionBarTitle("Hacks");
        ((HomeActivity) getActivity())
                .setActionBarSubtitle("News");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(Function.isNetworkAvailable(getContext())) {
                        NewsFragment newsFragment = new NewsFragment();
                        FragmentTransaction fragment = getFragmentManager().beginTransaction();
                        fragment.replace(R.id.frameContainer, newsFragment);
                        fragment.commit();
                        return true;
                    } else {
                        MainFragment mainFragment = new MainFragment();
                        FragmentTransaction fragment = getFragmentManager().beginTransaction();
                        fragment.replace(R.id.frameContainer, mainFragment);
                        fragment.commit();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.info_btn:
                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Distribuição")
                        .setContentText("Distribuído por NewsAPI.org")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .show();
                break;
        }

        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        if(Function.isNetworkAvailable(getContext())) {
            NewsFragment newsFragment = new NewsFragment();
            FragmentTransaction fragment = getFragmentManager().beginTransaction();
            fragment.replace(R.id.frameContainer, newsFragment);
            fragment.commit();
            super.onPause();
        } else {
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction fragment = getFragmentManager().beginTransaction();
            fragment.replace(R.id.frameContainer, mainFragment);
            fragment.commit();
            super.onPause();
        }
    }
}
package com.hackaton.fonseca.hackaton.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackaton.fonseca.hackaton.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements Filterable {

    private List<GastosItens> mGastoLista;
    private List<GastosItens> mGastoListaCompleta;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewTitulo;
        public TextView mTextViewDescricao;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imgGasto);
            mTextViewTitulo = itemView.findViewById(R.id.tituloGasto);
            mTextViewDescricao = itemView.findViewById(R.id.descricaoGasto);
        }

    }

    public ItemAdapter(ArrayList<GastosItens> mGastoLista) {
        this.mGastoLista = mGastoLista;
        mGastoListaCompleta = new ArrayList<>(mGastoLista);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itens_gasto, parent, false);
        ItemViewHolder ivh = new ItemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        GastosItens itemAtual = mGastoLista.get(position);

        holder.mImageView.setImageResource(itemAtual.getmImagemGasto());
        holder.mTextViewTitulo.setText(itemAtual.getmTitulo());
        holder.mTextViewDescricao.setText(itemAtual.getmDescricao());
    }

    @Override
    public int getItemCount() {
        return mGastoLista.size();
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<GastosItens> listaFiltrada = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                listaFiltrada.addAll(mGastoListaCompleta);
            } else {
                String filtragem = constraint.toString().toLowerCase().trim();

                for (GastosItens itens : mGastoListaCompleta) {
                    if (itens.getmTitulo().toLowerCase().contains(filtragem)) {
                        listaFiltrada.add(itens);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = listaFiltrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mGastoLista.clear();
            mGastoLista.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

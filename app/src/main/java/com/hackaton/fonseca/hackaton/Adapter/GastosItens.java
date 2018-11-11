package com.hackaton.fonseca.hackaton.Adapter;

public class GastosItens {

    private int mImagemGasto;
    private String mTitulo;
    private String mDescricao;

    public GastosItens(int imagemGasto, String titulo, String descricao) {
        mImagemGasto = imagemGasto;
        mTitulo = titulo;
        mDescricao = descricao;
    }

    public int getmImagemGasto() {
        return mImagemGasto;
    }

    public String getmTitulo() {
        return mTitulo;
    }

    public String getmDescricao() {
        return mDescricao;
    }
}

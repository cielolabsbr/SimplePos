package br.com.cielo.example.base.mvp;

public interface BaseView<P extends BasePresenter> {
    void setPresenter(P presenter);
}

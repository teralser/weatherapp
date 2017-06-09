package com.teralser.weatherapp.mvp.presenter.impl;

public interface IBasePresenter<T> {
    void init(T view);
}

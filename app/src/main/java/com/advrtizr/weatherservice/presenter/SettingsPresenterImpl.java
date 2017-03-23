package com.advrtizr.weatherservice.presenter;

import android.content.Context;

import com.advrtizr.weatherservice.model.SettingsInteractor;
import com.advrtizr.weatherservice.model.SettingsInteractorImpl;
import com.advrtizr.weatherservice.view.SettingsView;

public class SettingsPresenterImpl implements SettingsPresenter {

    private SettingsView view;
    private SettingsInteractor interactor;

    public SettingsPresenterImpl(SettingsView view) {
        this.view = view;
        this.interactor = new SettingsInteractorImpl((Context) view);
    }

    @Override
    public void loadSettings() {
        String settings = interactor.getSettings();
        view.displayState(settings);
    }

    @Override
    public void saveToSettings(String resource) {
        interactor.setSettings(resource);
    }

}

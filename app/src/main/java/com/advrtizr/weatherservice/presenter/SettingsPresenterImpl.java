package com.advrtizr.weatherservice.presenter;

import android.content.Context;

import com.advrtizr.weatherservice.model.SettingsModel;
import com.advrtizr.weatherservice.model.SettingsModelImpl;
import com.advrtizr.weatherservice.view.SettingsView;

public class SettingsPresenterImpl implements SettingsPresenter {

    private SettingsView view;
    private SettingsModel interactor;

    public SettingsPresenterImpl(SettingsView view) {
        this.view = view;
        this.interactor = new SettingsModelImpl((Context) view);
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

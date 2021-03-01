package com.klemar.android.factorytask.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.klemar.android.factorytask.viewModels.NewsListViewModel;

public class NewsListFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Application application;

    public NewsListFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NewsListViewModel.class) {
            return (T) new NewsListViewModel(application);
        }
        return null;
    }
}

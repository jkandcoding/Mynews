package com.klemar.android.factorytask.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.network.NewsListRepository;

import java.util.List;

public class NewsDetailsViewModel extends AndroidViewModel {

    private final NewsListRepository newsListRepository;

    private final MutableLiveData<Boolean> firstClickOnNewsList = new MutableLiveData<>();

    public NewsDetailsViewModel(@NonNull Application application) {
        super(application);
        newsListRepository = new NewsListRepository(application);
        firstClickOnNewsList.setValue(true);
    }

    //get current list from db
    public LiveData<List<NewsModel>> getNewsDetails() {
        return newsListRepository.getArticlesFromDb();
    }

    //get more (older) news
    public void getOlderNewsDetails() {
        newsListRepository.getOlderArticles();
    }

    public MutableLiveData<Boolean> getFirstClickOnNewsList() {
        return firstClickOnNewsList;
    }

    public void setFirstClickOnNewsList(boolean firstClickOnNewsList) {
        this.firstClickOnNewsList.setValue(firstClickOnNewsList);
    }

    // notified if smth went wrong
    public LiveData<Boolean> getErrorOccurred() {
        return newsListRepository.getErrorOccurred();
    }

    public void setErrorOccurred(boolean errorOccurred) {
        newsListRepository.setErrorOccurred(errorOccurred);
    }
}

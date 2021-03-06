package com.klemar.android.factorytask.viewModels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.network.NewsListRepository;

import java.util.List;

public class NewsListViewModel extends AndroidViewModel {

    private final NewsListRepository newsListRepository;



    public NewsListViewModel(@NonNull Application application) {
        super(application);
        newsListRepository = new NewsListRepository(application);
    }

    // get articles from database
    public LiveData<List<NewsModel>> getNewsList() {
        return newsListRepository.getArticlesFromDb();
    }

    // fetch new articles from network if needed
    public void refreshNews() {
        newsListRepository.refreshDatabase();
    }

    // fetch more (older) articles from network
    public void getNewsListOld() {
        newsListRepository.getOlderArticles();
    }

    // notified if smth went wrong
    public LiveData<Boolean> getErrorOccurred() {
        return newsListRepository.getErrorOccurred();
    }

    public void setErrorOccurred(boolean errorOccurred) {
        newsListRepository.setErrorOccurred(errorOccurred);
    }

}

package com.klemar.android.factorytask.viewModels;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.network.NewsListRepository;

import java.sql.Date;
import java.util.List;

public class NewsListViewModel extends AndroidViewModel {

    private NewsListRepository newsListRepository;

    private LiveData<List<NewsModel>> newsList;
   private LiveData<Date> lastTimestamp;
    private String lastTitle;


    public NewsListViewModel(@NonNull Application application) {
        super(application);
        newsListRepository = new NewsListRepository(application);
        newsList = newsListRepository.getArticlesFromDb();
        lastTimestamp = newsListRepository.getLastTimestamp();
        lastTitle = newsListRepository.getLastTitle();
        Log.d("di saam", "viewmodel");
    }

//    public void updateDatabase() {
//        Log.d("kjhjggf", "updateDatabase: " + lastTitle);
//        if (lastTimestamp.toString().equals("")) {
//            newsListFromNetwork = newsListRepository.getArticlesFromNetwork();
//            newsListRepository.insertArticlesIntoDb(newsListFromNetwork);
//        }
//    }

    public LiveData<List<NewsModel>> getNewsList() {
        return newsList;
    }

    public LiveData<Date> getLastTimestamp() {
        return lastTimestamp;
    }

    public String getLastTitle() {
        return lastTitle;
    }
}

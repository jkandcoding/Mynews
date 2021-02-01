package com.klemar.android.factorytask.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.klemar.android.factorytask.model.NewsListResponse;
import com.klemar.android.factorytask.model.NewsModel;

import java.sql.Date;
import java.util.List;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNews(List<NewsModel> newsModel);

    @Query("SELECT id, title, urlToImage, timestamp FROM newstable ORDER BY timestamp DESC")
    LiveData<List<NewsModel>> loadNewsFromDB();

    @Query("SELECT timeStamp FROM newstable ORDER BY id DESC LIMIT 1")
    LiveData<Date> lastTimeStamp();

    @Query("SELECT title FROM newstable ORDER BY id DESC LIMIT 1")
    String lastTitle();


}

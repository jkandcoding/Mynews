package com.klemar.android.factorytask.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.klemar.android.factorytask.model.NewsListResponse;
import com.klemar.android.factorytask.model.NewsModel;

import org.threeten.bp.OffsetDateTime;

import java.sql.Date;
import java.util.List;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(NewsModel newsModel);

    @Query("DELETE FROM newstable")
    void deleteAllNews();

//    @Query("SELECT title, urlToImage, timestamp, publishedAt FROM newstable")
    @Query("SELECT * FROM newstable ORDER BY datetime(publishedAt) DESC")
    LiveData<List<NewsModel>> loadNewsFromDB();

    @Query("SELECT timeStamp FROM newstable ORDER BY datetime(publishedAt) DESC LIMIT 1")
    OffsetDateTime getlastTimeStamp();

     //the oldest article in database
    @Query("SELECT publishedAt FROM newstable ORDER BY datetime(publishedAt) ASC LIMIT 1")
    OffsetDateTime getOldestPublishedAt();

}

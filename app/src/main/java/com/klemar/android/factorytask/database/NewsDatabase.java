package com.klemar.android.factorytask.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.klemar.android.factorytask.model.NewsModel;

@Database(entities = {NewsModel.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class NewsDatabase extends RoomDatabase {

    private static final String LOG_TAG = NewsDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "news";
    private static NewsDatabase sInstance;

    public static NewsDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        NewsDatabase.class, NewsDatabase.DATABASE_NAME)
                        //                  .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract NewsDao newsDao();

}

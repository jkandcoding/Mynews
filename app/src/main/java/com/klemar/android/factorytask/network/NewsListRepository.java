package com.klemar.android.factorytask.network;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.klemar.android.factorytask.database.NewsDao;
import com.klemar.android.factorytask.database.NewsDatabase;
import com.klemar.android.factorytask.model.NewsListResponse;
import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.utils.AppExecutors;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.klemar.android.factorytask.network.ApiClient.API_KEY;


public class NewsListRepository {

   private final static String SOURCE = "bbc-news";
   private static NewsListRepository newsListRepository;

    private LiveData<List<NewsModel>> articlesFromDb;
    private MutableLiveData<NewsListResponse> articlesFromNetwork = new MutableLiveData<>();
    private LiveData<Date> lastTimestamp;
    private String lastTitle;
    private NewsDao newsDao;

//    public static NewsListRepository getInstance() {
//        if (newsListRepository == null) {
//            newsListRepository = new NewsListRepository();
//        }
//        return newsListRepository;
//    }

    public NewsListRepository(Application application) {
        NewsDatabase db = NewsDatabase.getInstance(application);
        newsDao = db.newsDao();
        articlesFromDb = newsDao.loadNewsFromDB();
        lastTimestamp = newsDao.lastTimeStamp();
        lastTitle = newsDao.lastTitle();
    }

    //dohvaca podatke iz room db
    public LiveData<List<NewsModel>> getArticlesFromDb() {
        if (lastTimestamp.equals("")) {
            insertArticlesIntoDb(articlesFromNetwork.getValue().getArticles());
        }
        return articlesFromDb;
    }

    //dohvaca zadnji timestamp iz room db
    public LiveData<Date> getLastTimestamp() {
        return lastTimestamp;
    }

    public String getLastTitle() {
        return lastTitle;
    }

    //dohvaca podatke s networka
    public MutableLiveData<NewsListResponse> getArticlesFromNetwork() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<NewsListResponse> call = apiService.getNews(SOURCE, API_KEY);

        call.enqueue(new Callback<NewsListResponse>() {
            @Override
            public void onResponse(Call<NewsListResponse> call, Response<NewsListResponse> response) {
                if (response.body() != null && response.body().getStatus().equals("ok")) {
                    articlesFromNetwork.setValue(response.body());

//                    List<NewsModel> news = response.body().getArticles();
//                    mDb.newsDao().insertNews(news);
//                    long timest = mDb.newsDao().lastTimeStamp();
//                    String title = mDb.newsDao().lastTitle();
//                    Log.d("jkjkjkj", "zadnji unos: " + timest + "-" + title);
                /*
                timestamp = getLastArticleFromDb.getTimestamp();
                //  insertNewDataToDb(articles.getTimestamp(> timestamp));    todo insert into room db - change type!!!!!
                 */
                //  insertNewDataToDb(articles.getValue().getArticles());
                  //  Log.d("jkjkjkj", String.valueOf(articles.getValue().getArticles()));
                }
            }

//            private void insertNewDataToDb(List<NewsModel> articlesForDb) {
////                Log.d("jkjkjkj", "prije spremanja ");
////                for (NewsModel article : articles) {
////                    final NewsModel newsModel = new NewsModel(article.getSource(), article.getAuthor(), article.getTitle(),
////                            article.getDescription(), article.getUrl(), article.getUrlToImage(), article.getPublishedAt(), article.getContent());
////                  //  mDb.newsDao().insertNews();
////                }
// //               mDb.newsDao().insertNews(articles);
//                long lastTimestamp = mDb.newsDao().lastTimeStamp();
//                Log.d("jkjkjkj", String.valueOf(lastTimestamp));
//
//            }

            @Override
            public void onFailure(Call<NewsListResponse> call, Throwable t) {
                // Log error here since request failed
                //todo insert pop up alert dialog
                Log.e("hjhjh", t.toString());
            }
        });
        return articlesFromNetwork;
    }

    //inserta podatke iz networka u bazu
    public void insertArticlesIntoDb(List<NewsModel> newsForDb) {
        AppExecutors.getInstance().diskIO().execute(() -> newsDao.insertNews(newsForDb));

    }
}

package com.klemar.android.factorytask.network;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.klemar.android.factorytask.database.NewsDao;
import com.klemar.android.factorytask.database.NewsDatabase;
import com.klemar.android.factorytask.model.NewsListResponse;
import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.utils.AppExecutors;

import org.threeten.bp.Duration;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.klemar.android.factorytask.network.ApiClient.API_KEY;


public class NewsListRepository {

    private final static String SOURCE = "bbc-news";
    private LiveData<List<NewsModel>> articlesFromDb = null;
    private NewsListResponse articlesFromNetwork;
    private String oldestPublishedAtForQuery;
    private OffsetDateTime lastTimestamp;
    private final NewsDao newsDao;
    private final MutableLiveData<Boolean> errorOccurred = new MutableLiveData<>();

    public NewsListRepository(Application application) {
        NewsDatabase db = NewsDatabase.getInstance(application);
        newsDao = db.newsDao();
        AppExecutors.getInstance().diskIO().execute(() -> articlesFromDb = newsDao.loadNewsFromDB());
    }

    // get data only from room db
    public LiveData<List<NewsModel>> getArticlesFromDb() {
        //without "sleep" returned list is empty
        SystemClock.sleep(100);
        return articlesFromDb;
    }

    // if db is empty or db data are older than 5 min, fetch new data from network
    // and insert in db
    public void refreshDatabase() {
        CountDownLatch cDl = new CountDownLatch(1);
        AppExecutors.getInstance().diskIO().execute(() -> {
            lastTimestamp = newsDao.getlastTimeStamp();
            cDl.countDown();
        });

        OffsetDateTime nowTime = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);

        try {
            cDl.await();
            if (lastTimestamp == null || Duration.between(lastTimestamp, nowTime).toMinutes() > 4) {
                getArticlesFromNetwork();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // fetch new data from network and insert in db
    public void getArticlesFromNetwork() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //Execute the Network request
        Call<NewsListResponse> call = apiService.getNews(SOURCE, API_KEY);

        //Execute the request in a background thread
        call.enqueue(new Callback<NewsListResponse>() {

            @Override
            public void onResponse(Call<NewsListResponse> call, Response<NewsListResponse> response) {
                if (response.body() != null && response.body().getStatus().equals("ok")) {
                    //Get the values
                    articlesFromNetwork = response.body();

                    // delete * from database and insert new 20 articles
                    AppExecutors.getInstance().diskIO().execute(newsDao::deleteAllNews);
                    insertArticlesIntoDb(articlesFromNetwork);
                } else {

                    //e.g. more requests than api permits
                    errorOccurred.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<NewsListResponse> call, Throwable t) {
                // Log error here since request failed
                errorOccurred.setValue(true);
            }
        });
    }

    // when user scrolls to end of the list, this method gets 20 more articles from network
    public void getOlderArticles() {
        CountDownLatch cdl = new CountDownLatch(1);
        AppExecutors.getInstance().diskIO().execute(() -> {
            oldestPublishedAtForQuery = newsDao.getOldestPublishedAt().toString();
            cdl.countDown();
        });

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<NewsListResponse> call = null;
        try {
            cdl.await();
            call = apiService.getOlderNews(SOURCE, oldestPublishedAtForQuery, API_KEY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        call.enqueue(new Callback<NewsListResponse>() {
            @Override
            public void onResponse(Call<NewsListResponse> call, Response<NewsListResponse> response) {
                if (response.body() != null && response.body().getStatus().equals("ok")) {
                    //Get the values
                    articlesFromNetwork = response.body();
                    insertArticlesIntoDb(articlesFromNetwork);
                } else {
                    //e.g. more requests than api permits
                    errorOccurred.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<NewsListResponse> call, Throwable t) {
                errorOccurred.setValue(true);
            }
        });
    }

    // insert new data to database
    public void insertArticlesIntoDb(NewsListResponse newsForDb) {
        List<NewsModel> newArticles = newsForDb.getArticles();

        for (int i = 0; i < newArticles.size(); i++) {
            String author = newArticles.get(i).getAuthor();
            String title = newArticles.get(i).getTitle();
            String description = newArticles.get(i).getDescription();
            String url = newArticles.get(i).getUrl();
            String urlToImg = newArticles.get(i).getUrlToImage();
            String publishedAt = newArticles.get(i).getPublishedAt();
            String content = newArticles.get(i).getContent();

            NewsModel model = new NewsModel(author, title, description, url, urlToImg, publishedAt, content);
            AppExecutors.getInstance().diskIO().execute(() -> newsDao.insertNews(model));
        }
    }

    public MutableLiveData<Boolean> getErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(boolean errorOccurred) {
        this.errorOccurred.setValue(errorOccurred);
    }


}

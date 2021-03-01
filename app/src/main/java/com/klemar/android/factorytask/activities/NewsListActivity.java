package com.klemar.android.factorytask.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.klemar.android.factorytask.R;
import com.klemar.android.factorytask.adapters.NewsAdapter;
import com.klemar.android.factorytask.factories.NewsListFactory;
import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.utils.Helper;
import com.klemar.android.factorytask.viewModels.NewsListViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity {


    private RecyclerView rv_NewsList;
    private final ArrayList<NewsModel> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private NewsListViewModel viewModel;
    public ProgressBar loaderUp;
    public ProgressBar loaderDown;
    int articlePosition;
    boolean oneAlert = true;
    boolean loanding = true;


    // Click on item in newslist goes to single article in viewPager2
    private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //call getTag() on the view.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            articlePosition = viewHolder.getAbsoluteAdapterPosition();
            startNewsDetailsActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        Toolbar toolbar = findViewById(R.id.toolbar_fragment);
        toolbar.setTitle("Factory news");

        initUI();
        setupViewModel();
        getMoreData();
        getError();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollRecyclerViewToPosition();
    }


    private void scrollRecyclerViewToPosition() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int recyclerviewPosition = preferences.getInt("positionForList", -1);
        Log.d("alert", "recyclerview pos = " + recyclerviewPosition);
        if (recyclerviewPosition != -1) {
            rv_NewsList.scrollToPosition(recyclerviewPosition);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void startNewsDetailsActivity() {
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("articlePosition", articlePosition);
        startActivity(intent);
    }

    private void initUI() {
        loaderUp = findViewById(R.id.pb_news_list_up);
        loaderDown = findViewById(R.id.pb_news_list_down);
        rv_NewsList = findViewById(R.id.rv_news_list);
        newsAdapter = new NewsAdapter(NewsListActivity.this, newsList);
        linearLayoutManager = new LinearLayoutManager(this);
        rv_NewsList.setLayoutManager(linearLayoutManager);
        rv_NewsList.setAdapter(newsAdapter);
        //Create and set OnItemClickListener to the adapter:
        newsAdapter.setItemClickListener(onItemClickListener);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this,
                new NewsListFactory(getApplication()))
                .get(NewsListViewModel.class);
        try {
            viewModel.refreshNews();
            gettingNewsList();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Helper.showAlertDialog(this);
        }
    }

    // getting data from room db
    private void gettingNewsList() {
        viewModel.getNewsList().observe(this, news -> {
            newsList.clear();
            newsList.addAll(news);
            newsAdapter.notifyDataSetChanged();
        });
    }

    // when user scrolls to the beginning of the list -> refresh data from network,
    // when scrolls to the end -> fetch more (older) data from network
    private void getMoreData() {
        rv_NewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int visibleItemCount;
            int totalItemCount;
            int pastVisiblesItems;


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) { //check for scroll down

                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loaderDown.setVisibility(View.VISIBLE);
                        new CountDownTimer(1500, 500) {
                            @Override
                            public void onTick(long l) {
                            }

                            @Override
                            public void onFinish() {
                                loaderDown.setVisibility(View.GONE);
                                viewModel.getNewsListOld();
                            }
                        }.start();
                    }
                } else if (dy < 0) {   //check for scroll up (refresh data)
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (pastVisiblesItems == 0) {
                        loaderUp.setVisibility(View.VISIBLE);
                        new CountDownTimer(1500, 500) {
                            @Override
                            public void onTick(long l) {
                            }

                            @Override
                            public void onFinish() {
                                loaderUp.setVisibility(View.GONE);
                                viewModel.refreshNews();
                            }
                        }.start();
                    }
                }
            }
        });
    }

    private void getError() {
        viewModel.getErrorOccurred().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                if (error) {
                    Helper.showAlertDialog(NewsListActivity.this);
                    viewModel.setErrorOccurred(false);
                }
            }
        });
    }
}
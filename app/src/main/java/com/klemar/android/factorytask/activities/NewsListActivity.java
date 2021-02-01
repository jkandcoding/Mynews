package com.klemar.android.factorytask.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.klemar.android.factorytask.R;
import com.klemar.android.factorytask.adapters.NewsAdapter;
import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.viewModels.NewsListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsListActivity extends AppCompatActivity {


    private RecyclerView rv_NewsList;
    private ArrayList<NewsModel> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private NewsListViewModel viewModel;

    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Factory news");

        initUI();
        setupViewModel();
    }

    private void setupViewModel() {
       viewModel = new ViewModelProvider(this).get(NewsListViewModel.class);
//        viewModel = new ViewModelProvider(this,
//               new ViewModelProvider.AndroidViewModelFactory(getApplication()))
//                .get(NewsListViewModel.class);


        gettingData();
    }

    private void gettingData() {
        viewModel.getNewsList().observe(this, news -> {
            newsList.addAll(news);
            newsAdapter.notifyDataSetChanged();
        });
    }

    private void initUI() {
        rv_NewsList = findViewById(R.id.rv_news_list);
        newsAdapter = new NewsAdapter(NewsListActivity.this, newsList);
        linearLayoutManager = new LinearLayoutManager(this);
        rv_NewsList.setLayoutManager(linearLayoutManager);
        rv_NewsList.setAdapter(newsAdapter);

        // pb_NewsList.findViewById(R.id.pb_news_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  newsListPresenter.onDestroy();
    }
}
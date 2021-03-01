package com.klemar.android.factorytask.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.klemar.android.factorytask.R;
import com.klemar.android.factorytask.adapters.NewsDetailsAdapter;
import com.klemar.android.factorytask.factories.NewsDetailsFactory;
import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.utils.Helper;
import com.klemar.android.factorytask.viewModels.NewsDetailsViewModel;

import java.util.ArrayList;
import java.util.List;


public class NewsDetailsActivity extends FragmentActivity {

    private ViewPager2 viewPagerNewsDetails;
    private NewsDetailsAdapter newsDetailsAdapter;
    private final ArrayList<NewsModel> newsList = new ArrayList<>();
    private NewsDetailsViewModel viewModel;
    private ViewPager2.OnPageChangeCallback callback;
    private int articlePosition;
    int currentPosition;
    private boolean firstClickOnNewsList;
    private String currentTittle;
    private Toolbar toolbar;
    private ProgressBar pb_news_details_right;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        setupViewModel();
        hasIntent();
        setupSharedPref();
        getError();

        pb_news_details_right = findViewById(R.id.pb_news_details_right);

        toolbar = findViewById(R.id.toolbar_fragment);
        toolbar.setNavigationIcon(R.drawable.ic_white_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("positionForList", currentPosition);
            editor.apply();
            NewsDetailsActivity.super.onBackPressed();
        });

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPagerNewsDetails = findViewById(R.id.pager_news_details);
        newsDetailsAdapter = new NewsDetailsAdapter(NewsDetailsActivity.this, newsList);
        viewPagerNewsDetails.setAdapter(newsDetailsAdapter);


        callback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // firstClickOnNewsList variable is true when viewModel instantiate for the
                // first time. After that we set it to false. So only first entry in viewPager2
                // will set position to chosen article, otherwise every orientation change would
                // do the same
                if (firstClickOnNewsList) {

                    viewPagerNewsDetails.setCurrentItem(articlePosition, true);
                    viewModel.setFirstClickOnNewsList(false);
                }

                // get tittle from currently selected page for toolbar
                currentPosition = viewPagerNewsDetails.getCurrentItem();
                currentTittle = newsList.get(currentPosition).getTitle();
                toolbar.setTitle(currentTittle);

                // get older articles when user is at the end of viewPager's list
                if (currentPosition >= (newsList.size() - 1)) {
                    getOlderData();
                }
            }
        };

        viewPagerNewsDetails.registerOnPageChangeCallback(callback);
    }

    // getting articles from Room database
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this,
                new NewsDetailsFactory(getApplication()))
                .get(NewsDetailsViewModel.class);

        gettingArticlesData();
        settingFirstClickOnNewsList();
    }

    // setting firstClickOnNewsList variable and listening for changes
    private void settingFirstClickOnNewsList() {
        viewModel.getFirstClickOnNewsList().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                firstClickOnNewsList = b;
                Log.d("alert", "firstClickOnNewsList is " + firstClickOnNewsList);
            }
        });
    }

    // getting articles from Room database
    private void gettingArticlesData() {
        viewModel.getNewsDetails().observe(this, news -> {
            newsList.clear();
            newsList.addAll(news);
            newsDetailsAdapter.notifyDataSetChanged();
        });
    }

    // more articles when user is in ViewPager2
    private void getOlderData() {
        pb_news_details_right.setVisibility(View.VISIBLE);

        new CountDownTimer(1500, 500) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                pb_news_details_right.setVisibility(View.GONE);
                viewModel.getOlderNewsDetails();
            }
        }.start();
    }

    // position for first article in viewPager2 sent from first activity
    // (after user clicks on newslist)
    private void hasIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("articlePosition")) {
                articlePosition = intent.getIntExtra("articlePosition", -1);
            }
        }
    }

    // for sending current position of articles back to first activity NewsListActivity with
    // toolbar.setNavigationOnClickListener
    private void setupSharedPref() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    // handling viewPager2 back and forth
    @Override
    public void onBackPressed() {
        if (viewPagerNewsDetails.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPagerNewsDetails.setCurrentItem(viewPagerNewsDetails.getCurrentItem() - 1);
        }
    }

    private void getError() {
        viewModel.getErrorOccurred().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                if (error) {
                    Helper.showAlertDialog(NewsDetailsActivity.this);
                    viewModel.setErrorOccurred(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPagerNewsDetails.unregisterOnPageChangeCallback(callback);
    }
}
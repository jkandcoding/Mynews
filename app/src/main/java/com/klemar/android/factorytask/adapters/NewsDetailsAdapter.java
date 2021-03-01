package com.klemar.android.factorytask.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.klemar.android.factorytask.fragments.NewsDetailsFragment;
import com.klemar.android.factorytask.model.NewsModel;

import java.util.List;

public class NewsDetailsAdapter extends FragmentStateAdapter {

    private final List<NewsModel> newsDetailsList;


    public NewsDetailsAdapter(@NonNull FragmentActivity fragmentActivity, List<NewsModel> news) {
        super(fragmentActivity);
        newsDetailsList = news;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        NewsModel article = newsDetailsList.get(position);
        return new NewsDetailsFragment(article);
    }

    @Override
    public int getItemCount() {
        if (newsDetailsList == null) {
            return 0;
        }
        return newsDetailsList.size();
    }

}

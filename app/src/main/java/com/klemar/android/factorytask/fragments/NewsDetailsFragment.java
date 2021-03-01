package com.klemar.android.factorytask.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.klemar.android.factorytask.R;
import com.klemar.android.factorytask.factories.NewsDetailsFactory;
import com.klemar.android.factorytask.model.NewsModel;
import com.klemar.android.factorytask.viewModels.NewsDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class NewsDetailsFragment extends Fragment {

    private NewsModel article;

    private ImageView ivNewsDetails;
    private TextView tvNewsDetailsTittle;
    private TextView tvNewsDetailsContent;

    public NewsDetailsFragment() {
    }

    public NewsDetailsFragment(NewsModel article) {
        this.article = article;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        ivNewsDetails = view.findViewById(R.id.iv_news_details);
        tvNewsDetailsTittle = view.findViewById(R.id.tv_news_details_title);
        tvNewsDetailsContent = view.findViewById(R.id.tv_news_details_content);
        return view;
    }

    /**
     * Called when configuration change, saving article data to Bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Bundle dataBundle = new Bundle();
        dataBundle.putString("imageUrl", article.getUrlToImage());
        dataBundle.putString("title", article.getTitle());
        dataBundle.putString("description", article.getDescription());
        outState.putAll(dataBundle);
        super.onSaveInstanceState(outState);
    }

    /**
     * Setting article's data to ImageView, TextView when View is ready
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // The state was saved by onSaveInstanceState(Bundle outState) method.
        if (this.article == null) {
            String urlToImage = savedInstanceState.getString("imageUrl");
            String title = savedInstanceState.getString("title");
            String description = savedInstanceState.getString("description");
            this.article = new NewsModel(title, description, urlToImage);
        }

        Picasso.get().load(article.getUrlToImage()).into(ivNewsDetails);
        tvNewsDetailsTittle.setText(article.getTitle());
        tvNewsDetailsContent.setText(article.getDescription());
    }


}

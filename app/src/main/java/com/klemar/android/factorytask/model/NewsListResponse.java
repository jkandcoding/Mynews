package com.klemar.android.factorytask.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class NewsListResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("source")
    private String source;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<NewsModel> articles;

    public NewsListResponse(String status, String source, int totalResults, List<NewsModel> articles) {
        this.status = status;
        this.source = source;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsModel> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsModel> articles) {
        this.articles = articles;
    }
}

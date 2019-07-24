package com.example.georgesamuel.seamlabstask.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.georgesamuel.seamlabstask.database.ArticleDB;
import com.example.georgesamuel.seamlabstask.database.ArticleTable;
import com.example.georgesamuel.seamlabstask.model.Article;
import com.example.georgesamuel.seamlabstask.model.NewsResponse;
import com.example.georgesamuel.seamlabstask.remote.ClientAPI;
import com.example.georgesamuel.seamlabstask.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesViewModel extends AndroidViewModel {

    private final String TAG = "ArticlesViewModel";
    private MutableLiveData<List<Article>> articles = new MutableLiveData<>();
    private MutableLiveData<List<ArticleTable>> articlesOff = new MutableLiveData<>();
    private ClientAPI clientAPI;
    private ArticleDB articleDB;

    public ArticlesViewModel(@NonNull Application application) {
        super(application);

        clientAPI = RetrofitClient.getInstance();
    }

    public LiveData<List<Article>> getArticles(Context context, String q, String from, String sortBy, String apiKey){

        clientAPI.getArticles(q, from, sortBy, apiKey).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {

                NewsResponse newsResponse = response.body();
                if(newsResponse.getStatus().equals("ok")){

                    articleDB = Room.databaseBuilder(context, ArticleDB.class, "articledb").allowMainThreadQueries().build();

                    for(Article article : newsResponse.getArticles()){

                        ArticleTable articleTable = new ArticleTable();

                        articleTable.setAuthor(article.getAuthor());
                        articleTable.setContent(article.getContent());
                        articleTable.setDescription(article.getDescription());
                        articleTable.setAuthor(article.getAuthor());
                        articleTable.setPublishedAt(article.getPublishedAt());
                        articleTable.setTitle(article.getTitle());
                        articleTable.setUrl(article.getUrl());
                        articleTable.setUrlToImage(article.getUrlToImage());

                        articleDB.ArticleDao().insert(articleTable);
                    }

                    articles.postValue(newsResponse.getArticles());
                }
                else {
                    Log.d(TAG, "getArticles: onResponse: Status: " + newsResponse.getStatus());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {

                Log.d(TAG, "getArticles: onFailure: " + t.getMessage());

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return articles;
    }

    public LiveData<List<ArticleTable>> getArticlesOffline(Context context){

        articleDB = Room.databaseBuilder(context, ArticleDB.class, "articledb").allowMainThreadQueries().build();

        articlesOff.postValue(articleDB.ArticleDao().getArticles());

        return articlesOff;
    }

    public void deleteArticles(Context context){

        articleDB = Room.databaseBuilder(context, ArticleDB.class, "articledb").allowMainThreadQueries().build();

        articleDB.ArticleDao().delete();
    }
}

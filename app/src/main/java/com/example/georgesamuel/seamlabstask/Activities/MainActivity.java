package com.example.georgesamuel.seamlabstask.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.georgesamuel.seamlabstask.R;
import com.example.georgesamuel.seamlabstask.adapter.ArticlesAdapter;
import com.example.georgesamuel.seamlabstask.database.ArticleTable;
import com.example.georgesamuel.seamlabstask.model.Article;
import com.example.georgesamuel.seamlabstask.swipegesture.RecyclerItemTouchHelper;
import com.example.georgesamuel.seamlabstask.viewModel.ArticlesViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    androidx.appcompat.widget.Toolbar toolbar;
    private ArticlesViewModel viewModel;
    private RecyclerView recyclerView;
    private ArticlesAdapter adapter;
    private List<Article> articleList = new ArrayList<>();
    private boolean intentIsVisible = false;
    private ConnectivityManager connectivityManager;
    private RelativeLayout main;
    private SwipeRefreshLayout refreshLayout;
    private TextView noArticlesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if(!checkNetworkStatus()){
            Snackbar.make(main, "There is problem in internet connection", Snackbar.LENGTH_SHORT).show();

            getArticlesOffline();
        }
        else {

            viewModel.deleteArticles(getApplicationContext());

            getArticlesOnline();
        }
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        main = findViewById(R.id.main);
        refreshLayout = findViewById(R.id.swipeRefresh);
        noArticlesText = findViewById(R.id.noArticles);

        viewModel = ViewModelProviders.of(MainActivity.this).get(ArticlesViewModel.class);
        adapter = new ArticlesAdapter(getApplicationContext(), articleList);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager
                (new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(checkNetworkStatus()){

                    viewModel.deleteArticles(getApplicationContext());

                    getArticlesOnline();
                    refreshLayout.setRefreshing(false);
                }
                else {
                    getArticlesOffline();
                    refreshLayout.setRefreshing(false);
                }

            }
        });

    }

    private void getArticlesOffline(){

        viewModel.getArticlesOffline(getApplicationContext()).observe(MainActivity.this, new Observer<List<ArticleTable>>() {
            @Override
            public void onChanged(List<ArticleTable> articles) {

                if(articles.size() == 0){
                    noArticlesText.setVisibility(View.VISIBLE);
                }
                else {
                    noArticlesText.setVisibility(View.GONE);
                }

                articleList.clear();
                adapter.notifyDataSetChanged();

                for(ArticleTable article : articles){
                    Article article1 = new Article();
                    article1.setAuthor(article.getAuthor());
                    article1.setContent(article.getContent());
                    article1.setDescription(article.getDescription());
                    article1.setPublishedAt(article.getPublishedAt());
                    article1.setTitle(article.getTitle());
                    article1.setUrl(article.getTitle());
                    article1.setUrlToImage(article.getUrlToImage());

                    articleList.add(article1);
                }

                adapter.notifyDataSetChanged();

            }
        });
    }

    private void getArticlesOnline() {

        Long cms = System.currentTimeMillis();
        cms = cms - 2592000000L;
        String date = getDate(cms);

        final String API_KEY = "0697f1195c1f41c389b90370d92eeaec";

        viewModel.getArticles(MainActivity.this, "bitcoin", date, "publishedAt", API_KEY)
                .observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {

                if(articles.size() == 0){
                    noArticlesText.setVisibility(View.VISIBLE);
                }
                else {
                    noArticlesText.setVisibility(View.GONE);
                }

                articleList.clear();
                adapter.notifyDataSetChanged();
                articleList.addAll(articles);
                adapter.notifyDataSetChanged();

            }
        });
    }

    private String getDate(Long cms) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cms);
        return formatter.format(calendar.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.search){

        }

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ArticlesAdapter.ArticlesViewHolder) {

            String url = articleList.get(viewHolder.getAdapterPosition()).getUrl();
            String titleText = articleList.get(viewHolder.getAdapterPosition()).getTitle();

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, titleText);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

        }
    }

    private boolean checkNetworkStatus()
    {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

package com.example.georgesamuel.seamlabstask.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.georgesamuel.seamlabstask.R;

public class ArticleActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView title;
    private TextView description;
    private ImageView image;
    private TextView owner;
    private TextView date;

    private String titleText, descriptionText, ownerText, dateText, imagePath, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);


        titleText = getIntent().getStringExtra("title");
        descriptionText = getIntent().getStringExtra("description");
        imagePath = getIntent().getStringExtra("imagePath");
        ownerText = getIntent().getStringExtra("owner");
        dateText = getIntent().getStringExtra("date");
        url = getIntent().getStringExtra("url");

        init();

       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Article");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        image = findViewById(R.id.image);
        owner = findViewById(R.id.owner);
        date = findViewById(R.id.date);

        title.setText(titleText);
        description.setText(descriptionText);
        owner.setText(ownerText);
        date.setText(dateText);

        Glide.with(ArticleActivity.this).load(imagePath).into(image);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, titleText);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }

        return true;
    }
}

package com.example.georgesamuel.seamlabstask.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.georgesamuel.seamlabstask.model.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert
    void insert(ArticleTable article);

    @Query("select * from ArticleTable")
    List<ArticleTable> getArticles();

    @Query("DELETE FROM ArticleTable")
    void delete();

}

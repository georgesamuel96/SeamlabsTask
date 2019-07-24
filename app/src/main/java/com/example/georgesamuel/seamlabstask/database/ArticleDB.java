package com.example.georgesamuel.seamlabstask.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.georgesamuel.seamlabstask.model.Article;

@Database(entities = {ArticleTable.class}, version = 1)
public abstract class ArticleDB extends RoomDatabase {

    public abstract ArticleDao ArticleDao();
}

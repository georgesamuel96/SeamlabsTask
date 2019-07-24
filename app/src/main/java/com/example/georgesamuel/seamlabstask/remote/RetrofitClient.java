package com.example.georgesamuel.seamlabstask.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static ClientAPI clientInstance = null;
    private static final String BASE_URL = "https://newsapi.org";

    private RetrofitClient(){

    }

    public static ClientAPI getInstance(){

        if(clientInstance == null){

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            clientInstance = retrofit.create(ClientAPI.class);
        }

        return clientInstance;
    }
}

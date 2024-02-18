package com.example.ecoera;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    List<Article> articleList=new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.news_recycler_view);
        progressIndicator=findViewById(R.id.progress_bar);
        btn1=findViewById(R.id.btn_1);
        btn2=findViewById(R.id.btn_2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        setupRecyclerView();
        getNews();
    }
    void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }
    void changeInProgress(boolean show){
        if(show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }
    void getNews(){
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("53b73df48a6f4bf29daaf0594fdf7303");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(()->{
                            changeInProgress(false);
                            articleList=response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();
                        });

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("GOT FAILURE", Objects.requireNonNull(throwable.getMessage()));
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {

    }
}
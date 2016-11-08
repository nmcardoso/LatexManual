package com.github.nmcardoso.latexmanual;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity
        implements PaginatorFragment.OnPageChangedListener {
    private static final int ITEMS_PER_PAGE = 20;
    private RecyclerView rvFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (hasFavorites()) {
            setupList();
            setupPaginator();
        } else {
            // Showing empty warning
            ScrollView svFavorites = (ScrollView) findViewById(R.id.sv_favorites);
            TextView txtEmpty = (TextView) findViewById(R.id.txt_empty);

            svFavorites.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNextPressed(int currentPage) {
        // New instance of database helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Data set of next page
        List<Favorite> data = dbHelper.getFavorites(ITEMS_PER_PAGE,
                currentPage * ITEMS_PER_PAGE);

        // Getting adapter and swapping data
        FavoriteAdapter adapter = (FavoriteAdapter) rvFavorites.getAdapter();
        adapter.swap(data);
    }

    @Override
    public void onPreviousPressed(int currentPage) {
        // New instance of database helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Data set of previous page
        List<Favorite> data = dbHelper.getFavorites(ITEMS_PER_PAGE,
                (currentPage - 1) * ITEMS_PER_PAGE);

        // Getting adapter and swapping data
        FavoriteAdapter adapter = (FavoriteAdapter) rvFavorites.getAdapter();
        adapter.swap(data);
    }

    private void setupList() {
        rvFavorites = (RecyclerView) findViewById(R.id.rv_favorites);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Favorite> data = dbHelper.getFavorites(ITEMS_PER_PAGE);
        FavoriteAdapter adapter = new FavoriteAdapter(this, data);
        rvFavorites.setAdapter(adapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupPaginator() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int favCount = dbHelper.getFavoritesCount();

        PaginatorFragment pag = PaginatorFragment.newInstance(ITEMS_PER_PAGE, favCount);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_paginator, pag)
                .commit();
    }

    private boolean hasFavorites() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        return dbHelper.getFavoritesCount() > 0;
    }
}

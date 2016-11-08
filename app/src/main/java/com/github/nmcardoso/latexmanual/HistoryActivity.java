package com.github.nmcardoso.latexmanual;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity
        implements PaginatorFragment.OnPageChangedListener {
    private static final int ITEMS_PER_PAGE = 20;
    private RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (hasHistory()) {
            setupList();
            setupPaginator();
        } else {
            // show 'empty history' message
            ScrollView svHistory = (ScrollView) findViewById(R.id.sv_history);
            TextView txtEmpty = (TextView) findViewById(R.id.txt_empty);

            svHistory.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNextPressed(int currentPage) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        List<History> data = dbHelper.getHistory(ITEMS_PER_PAGE,
                currentPage * ITEMS_PER_PAGE);

        HistoryAdapter adapter = (HistoryAdapter) rvHistory.getAdapter();
        adapter.swap(data);
    }

    @Override
    public void onPreviousPressed(int currentPage) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        List<History> data = dbHelper.getHistory(ITEMS_PER_PAGE,
                (currentPage - 1) * ITEMS_PER_PAGE);

        HistoryAdapter adapter = (HistoryAdapter) rvHistory.getAdapter();
        adapter.swap(data);
    }

    private void setupList() {
        // preparing data
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<History> data = dbHelper.getHistory(ITEMS_PER_PAGE);
        HistoryAdapter adapter = new HistoryAdapter(this, data);

        // putting in view
        rvHistory = (RecyclerView) findViewById(R.id.rv_history);
        rvHistory.setAdapter(adapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupPaginator() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int count = dbHelper.getHistoryCount();

        PaginatorFragment pag = PaginatorFragment.newInstance(ITEMS_PER_PAGE, count);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_paginator, pag)
                .commit();
    }

    private boolean hasHistory() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        return dbHelper.getHistoryCount() > 0;
    }
}

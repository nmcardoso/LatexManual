package com.github.nmcardoso.latexmanual;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HistoryFragment extends Fragment
        implements PaginatorFragment.OnPageChangedListener{
    private static final int RESULTS_PER_PAGE = 20;
    private DatabaseHelper dbHelper;
    private HistoryAdapter historyAdapter;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        dbHelper = new DatabaseHelper(getActivity());

        RecyclerView rvHistoric = (RecyclerView) view.findViewById(R.id.rv_history);
        List<History> historyList = dbHelper.getHistory(RESULTS_PER_PAGE);
        historyAdapter = new HistoryAdapter(getActivity(), historyList);
        rvHistoric.setAdapter(historyAdapter);
        rvHistoric.setLayoutManager(new LinearLayoutManager(getActivity()));

        int histCount = dbHelper.getHistoryCount();
        PaginatorFragment paginatorFragment = PaginatorFragment.newInstance(RESULTS_PER_PAGE,
                histCount);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frg_paginator, paginatorFragment).commit();

        return view;
    }

    @Override
    public void onNextPressed(int currentPage) {
        List<History> historyList = dbHelper.getHistory(RESULTS_PER_PAGE,
                currentPage * RESULTS_PER_PAGE);
        historyAdapter.swap(historyList);
    }

    @Override
    public void onPreviousPressed(int currentPage) {
        List<History> historyList = dbHelper.getHistory(RESULTS_PER_PAGE,
                (currentPage - 1) * RESULTS_PER_PAGE);
        historyAdapter.swap(historyList);
    }
}

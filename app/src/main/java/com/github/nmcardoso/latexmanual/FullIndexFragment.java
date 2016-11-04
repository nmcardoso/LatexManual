package com.github.nmcardoso.latexmanual;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.List;

public class FullIndexFragment extends Fragment
        implements PaginatorFragment.OnPageChangedListener {
    private static final int ITEMS_PER_PAGE = 20;
    private DatabaseHelper dbHelper;
    private FullIndexAdapter adapter;
    private ScrollView svFullIndex;

    public FullIndexFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_index, container, false);

        RecyclerView rvFullIndex = (RecyclerView) view.findViewById(R.id.rv_full_index);
        dbHelper = new DatabaseHelper(getContext());
        List<Documentation> data = dbHelper.getAllDocumentations(20, 0);
        adapter = new FullIndexAdapter(getContext(), data);
        rvFullIndex.setAdapter(adapter);
        rvFullIndex.setLayoutManager(new LinearLayoutManager(getContext()));

        svFullIndex = (ScrollView) view.findViewById(R.id.sv_full_index);
        svFullIndex.setSmoothScrollingEnabled(true);

        int docCount = dbHelper.getDocumentationCount();

        PaginatorFragment pagFragment = PaginatorFragment.newInstance(ITEMS_PER_PAGE, docCount);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.frame_paginator, pagFragment)
                .commit();

        return view;
    }

    @Override
    public void onNextPressed(int currentPage) {
        List<Documentation> docList = dbHelper.getAllDocumentations(ITEMS_PER_PAGE,
                currentPage * ITEMS_PER_PAGE);
        adapter.swap(docList);
        svFullIndex.smoothScrollTo(0, 0);
    }

    @Override
    public void onPreviousPressed(int currentPage) {
        List<Documentation> docList = dbHelper.getAllDocumentations(ITEMS_PER_PAGE,
                (currentPage - 1) * ITEMS_PER_PAGE);
        adapter.swap(docList);
        svFullIndex.smoothScrollTo(0, 0);
    }
}

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

public class FavoriteFragment extends Fragment
        implements PaginatorFragment.OnPageChangedListener {
    private static final int RESULTS_PER_PAGE = 20;
    private DatabaseHelper dbHelper;
    private FavoriteAdapter favoriteAdapter;
    private List<Favorite> favoritesList;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        dbHelper = new DatabaseHelper(getActivity());

        RecyclerView rvFavorites = (RecyclerView) view.findViewById(R.id.rv_favorites);
        favoritesList = dbHelper.getFavorites(RESULTS_PER_PAGE);
        favoriteAdapter = new FavoriteAdapter(getActivity(), favoritesList);
        rvFavorites.setAdapter(favoriteAdapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));

        int favCount = dbHelper.getFavoritesCount();
        PaginatorFragment paginatorFragment = PaginatorFragment.newInstance(RESULTS_PER_PAGE,
                favCount);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frg_paginator, paginatorFragment).commit();

        return view;
    }

    @Override
    public void onNextPressed(int currentPage) {
        favoritesList = dbHelper.getFavorites(RESULTS_PER_PAGE,
                currentPage * RESULTS_PER_PAGE);
        favoriteAdapter.swap(favoritesList);
    }

    @Override
    public void onPreviousPressed(int currentPage) {
        favoritesList = dbHelper.getFavorites(RESULTS_PER_PAGE,
                (currentPage - 1) * RESULTS_PER_PAGE);
        favoriteAdapter.swap(favoritesList);
    }
}

package com.github.nmcardoso.latexmanual;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

        RecyclerView rvFavorites = (RecyclerView) view.findViewById(R.id.rv_favorites);
        List<Favorite> favoritesList = dbHelper.getFavorites(15);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(getActivity(), favoritesList);
        rvFavorites.setAdapter(favoriteAdapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}

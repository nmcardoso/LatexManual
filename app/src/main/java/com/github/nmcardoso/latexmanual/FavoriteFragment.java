package com.github.nmcardoso.latexmanual;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class FavoriteFragment extends Fragment {
    private static final int RESULTS_PER_PAGE = 20;
    private int currentPage = 1;
    private int totalPages;
    private DatabaseHelper dbHelper;
    private FavoriteAdapter favoriteAdapter;
    private TextView txtFavNav;
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

        LinearLayout favoritesNav = (LinearLayout) view.findViewById(R.id.favorites_nav);
        txtFavNav = (TextView) view.findViewById(R.id.txt_fav_nav);
        ImageButton ibNext = (ImageButton) view.findViewById(R.id.ib_fav_next);
        ImageButton ibPrev = (ImageButton) view.findViewById(R.id.ib_fav_prev);

        int favCount = dbHelper.getFavoritesCount();
        totalPages = (int) Math.ceil((float) favCount / RESULTS_PER_PAGE);

        if (favCount == 0) {
            ScrollView svHistory = (ScrollView) view.findViewById(R.id.sv_favorites);
            TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);
            svHistory.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else if (favCount > RESULTS_PER_PAGE) {
            favoritesNav.setVisibility(View.VISIBLE);
            txtFavNav.setText(String.format(getResources().getString(R.string.i_of_n),
                    1, totalPages));

            ibPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPage > 1) {
                        currentPage--;
                        favoritesList = dbHelper.getFavorites(RESULTS_PER_PAGE,
                                (currentPage - 1) * RESULTS_PER_PAGE);
                        favoriteAdapter.swap(favoritesList);
                        txtFavNav.setText(String.format(getResources().getString(R.string.i_of_n),
                                currentPage, totalPages));
                    }
                }
            });

            ibNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPage < totalPages) {
                        favoritesList = dbHelper.getFavorites(RESULTS_PER_PAGE,
                                currentPage * RESULTS_PER_PAGE);
                        favoriteAdapter.swap(favoritesList);
                        currentPage++;
                        txtFavNav.setText(String.format(getResources().getString(R.string.i_of_n),
                                currentPage, totalPages));
                    }
                }
            });
        }

        return view;
    }
}

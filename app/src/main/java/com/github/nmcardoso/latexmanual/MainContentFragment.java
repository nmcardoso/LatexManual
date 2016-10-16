package com.github.nmcardoso.latexmanual;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class MainContentFragment extends Fragment {


    public MainContentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_main_content, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

        RecyclerView mostViewedRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_most_viewed);
        List<Historic> mvList = dbHelper.getMostViewed(5);
        CardListAdapter mvAdapter = new CardListAdapter(getActivity(), mvList, CardListAdapter.MOST_VIEWED);
        mostViewedRecyclerView.setAdapter(mvAdapter);
        mostViewedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView histRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recent_historic);
        List<Historic> histList = dbHelper.getHistoric(5);
        CardListAdapter histAdapter = new CardListAdapter(getActivity(), histList, CardListAdapter.HISTORIC);
        histRecyclerView.setAdapter(histAdapter);
        histRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView favRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recent_faves);
        List<Favorite> favList = dbHelper.getFavorites(5);
        CardListAdapter favAdapter = new CardListAdapter(getActivity(), favList, CardListAdapter.FAVORITE);
        favRecyclerView.setAdapter(favAdapter);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

}

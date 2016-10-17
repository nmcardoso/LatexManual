package com.github.nmcardoso.latexmanual;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainContentFragment extends Fragment {


    public MainContentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main_content, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

        List<Card> cardsList = new ArrayList<>();
        cardsList.add(
                new Card.CardBuilder(getString(R.string.most_viewed), dbHelper.getMostViewed(5))
                .flag(CardListAdapter.MOST_VIEWED)
                .headerBackground(R.color.orange1)
                .headerIcon(R.drawable.ic_plus)
                .build()
        );
        cardsList.add(
                new Card.CardBuilder(getString(R.string.history), dbHelper.getHistoric(5))
                .flag(CardListAdapter.HISTORIC)
                .headerBackground(R.color.purple1)
                .headerIcon(R.drawable.ic_menu_history)
                .build()
        );
        cardsList.add(
                new Card.CardBuilder(getString(R.string.favorites), dbHelper.getFavorites(5))
                .flag(CardListAdapter.FAVORITE)
                .headerBackground(R.color.blue1)
                .headerIcon(R.drawable.ic_star)
                .build()
        );
        cardsList.add(
                new Card.CardBuilder(getString(R.string.statistics),
                        String.format(getString(R.string.stats_content),
                                dbHelper.getDocumentationCount(),
                                dbHelper.getHistoricCount(),
                                dbHelper.getUniqueHistoryCount(),
                                dbHelper.getFavoritesCount()))
                .headerBackground(R.color.cardview_dark_background)
                .headerIcon(R.drawable.ic_trending_up)
                .build()
        );

        CardRecyclerAdapter adapter = new CardRecyclerAdapter(getActivity(), cardsList);
        RecyclerView rvCards = (RecyclerView) rootView.findViewById(R.id.rv_cards);
        rvCards.setAdapter(adapter);
        rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

}

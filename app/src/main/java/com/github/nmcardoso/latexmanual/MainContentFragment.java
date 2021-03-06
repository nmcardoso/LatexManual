package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainContentFragment extends Fragment {
    CallbackInterface mCallback;
    RecyclerView rvCards;
    SwipeRefreshLayout srLayout;

    public interface CallbackInterface {
        void swapFragment(Fragment fragment);
    }

    public MainContentFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CallbackInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main_content, container, false);

        rvCards = (RecyclerView) rootView.findViewById(R.id.rv_cards);
        setupCardsView(setupCardsList());

        srLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.sr_layout);
        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateCards(setupCardsList());
                srLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void updateCards(List<Card> newData) {
        CardRecyclerAdapter adapter = (CardRecyclerAdapter) rvCards.getAdapter();
        adapter.swap(newData);
    }

    private void setupCardsView(List<Card> data) {
        CardRecyclerAdapter adapter = new CardRecyclerAdapter(getActivity(), data);
        rvCards.setAdapter(adapter);
        rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private List<Card> setupCardsList() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        List<Card> cardsList = new ArrayList<>();

        /*
         * Most Viewed Card
         */
        List<Pair<Documentation, Integer>> mostViewedList = dbHelper.getMostViewed(5);
        if (!mostViewedList.isEmpty()) {
            cardsList.add(
                    new Card.CardBuilder(getString(R.string.most_viewed), mostViewedList)
                            .listItemType(CardListAdapter.MOST_VIEWED)
                            .headerBackground(R.color.orange1)
                            .headerIcon(R.drawable.ic_plus)
                            .viewMore(false)
                            .build()
            );
        }

        /*
         * Recently Viewed Card
         */
        List<History> historyList = dbHelper.getUniqueHistory(5);
        if (!historyList.isEmpty()) {
            cardsList.add(
                    new Card.CardBuilder(getString(R.string.recently_viewed), historyList)
                            .listItemType(CardListAdapter.HISTORY)
                            .headerBackground(R.color.purple1)
                            .headerIcon(R.drawable.ic_menu_history)
                            .viewMore(historyList.size() == 5)
                            .clickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(view.getContext(),
                                            HistoryActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .build()
            );
        }

        /*
         * Favorite Card
         */
        List<Favorite> favoriteList = dbHelper.getFavorites(5);
        if (!favoriteList.isEmpty()) {
            cardsList.add(
                    new Card.CardBuilder(getString(R.string.favorites), favoriteList)
                            .listItemType(CardListAdapter.FAVORITE)
                            .headerBackground(R.color.blue1)
                            .headerIcon(R.drawable.ic_star)
                            .viewMore(favoriteList.size() == 5)
                            .clickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(view.getContext(),
                                            FavoriteActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .build()
            );
        }

        /*
         * Stats Card
         */
        List<Pair<String, Integer>> statsList = new ArrayList<>();
        statsList.add(new Pair<String, Integer>(
                getString(R.string.docs_in_database), dbHelper.getDocumentationCount()));
        statsList.add(new Pair<String, Integer>(
                getString(R.string.doc_views), dbHelper.getHistoryCount()));
        statsList.add(new Pair<String, Integer>(
                getString(R.string.unique_doc_views), dbHelper.getUniqueHistoryCount()));
        statsList.add(new Pair<String, Integer>(
                getString(R.string.favorites), dbHelper.getFavoritesCount()));
        cardsList.add(
                new Card.CardBuilder(getString(R.string.statistics), statsList)
                        .listItemType(CardListAdapter.STATISTICS)
                        .headerBackground(R.color.cardview_dark_background)
                        .headerIcon(R.drawable.ic_trending_up)
                        .viewMore(false)
                        .build()
        );

        /*
         * Random doc Card
         */
        final Documentation randomDoc = dbHelper.getRandomDocumentation();

        String docData = randomDoc.getData().trim();
        String docStr = randomDoc.getTitle() + "\n"
                + docData.substring(0, docData.length() > 150 ? 150 : docData.length()) + "...";
        SpannableString spanStr = new SpannableString(docStr);
        spanStr.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),
                0, randomDoc.getTitle().length(), 0);
        spanStr.setSpan(new RelativeSizeSpan(1.3f), 0, randomDoc.getTitle().length(), 0);

        cardsList.add(
                new Card.CardBuilder(getString(R.string.random_doc), spanStr)
                        .headerBackground(R.color.orange1)
                        .headerIcon(R.drawable.ic_shuffle)
                        .viewMore(true)
                        .clickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(view.getContext(),
                                        DocViewerActivity.class);
                                intent.putExtra(DatabaseHelper.DOCUMENTATIONS_FILE_NAME,
                                        randomDoc.getFileName());
                                startActivity(intent);
                            }
                        })
                        .build()
        );

        return cardsList;
    }
}

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


public class HistoryFragment extends Fragment {
    private static final int RESULTS_PER_PAGE = 20;
    private int currentPage = 1;
    private int totalPages;
    private DatabaseHelper dbHelper;
    private HistoryAdapter historyAdapter;
    private TextView txtHistNav;

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

        LinearLayout historyNav = (LinearLayout) view.findViewById(R.id.history_nav);
        txtHistNav = (TextView) view.findViewById(R.id.txt_hist_nav);
        ImageButton ibPrev = (ImageButton) view.findViewById(R.id.ib_hist_prev);
        ImageButton ibNext = (ImageButton) view.findViewById(R.id.ib_hist_next);

        int historyCount = dbHelper.getHistoryCount();
        totalPages = (int) Math.ceil((float) historyCount / RESULTS_PER_PAGE);

        if (historyCount == 0) {
            ScrollView svHistory = (ScrollView) view.findViewById(R.id.sv_history);
            TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);
            svHistory.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else if (historyCount > RESULTS_PER_PAGE) {
            historyNav.setVisibility(View.VISIBLE);
            txtHistNav.setText(String.format(getResources().getString(R.string.i_of_n),
                    1, totalPages));

            ibPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPage > 1) {
                        currentPage--;
                        List<History> historyList = dbHelper.getHistory(RESULTS_PER_PAGE,
                                (currentPage - 1) * RESULTS_PER_PAGE);
                        historyAdapter.swap(historyList);
                        txtHistNav.setText(String.format(getResources().getString(R.string.i_of_n),
                                currentPage, totalPages));
                    }
                }
            });

            ibNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPage < totalPages) {
                        List<History> historyList = dbHelper.getHistory(RESULTS_PER_PAGE,
                                currentPage * RESULTS_PER_PAGE);
                        historyAdapter.swap(historyList);
                        currentPage++;
                        txtHistNav.setText(String.format(getResources().getString(R.string.i_of_n),
                                currentPage, totalPages));
                    }
                }
            });
        }

        return view;
    }
}

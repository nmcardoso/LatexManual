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
import android.widget.TextView;

import java.util.List;


public class HistoricFragment extends Fragment {

    private static final int RESULTS_PER_PAGE = 20;
    private int currentPage = 1;
    private int totalPages;
    private DatabaseHelper dbHelper;
    private HistoricAdapter historicAdapter;
    private TextView txtHistNav;


    public HistoricFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historic, container, false);

        dbHelper = new DatabaseHelper(getActivity());

        RecyclerView rvHistoric = (RecyclerView) view.findViewById(R.id.rv_historic);
        List<Historic> historicList = dbHelper.getHistoric(RESULTS_PER_PAGE);
        historicAdapter = new HistoricAdapter(getActivity(), historicList);
        rvHistoric.setAdapter(historicAdapter);
        rvHistoric.setLayoutManager(new LinearLayoutManager(getActivity()));

        LinearLayout historyNav = (LinearLayout) view.findViewById(R.id.history_nav);
        txtHistNav = (TextView) view.findViewById(R.id.txt_hist_nav);
        ImageButton ibPrev = (ImageButton) view.findViewById(R.id.ib_hist_prev);
        ImageButton ibNext = (ImageButton) view.findViewById(R.id.ib_hist_next);

        int historyCount = dbHelper.getHistoricCount();
        totalPages = (int) Math.ceil((float) historyCount / RESULTS_PER_PAGE);

        if (historyCount > RESULTS_PER_PAGE) {
            historyNav.setVisibility(View.VISIBLE);
            txtHistNav.setText(String.format(getResources().getString(R.string.i_of_n),
                    1, totalPages));

            ibPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPage > 1) {
                        currentPage--;
                        List<Historic> historicList = dbHelper.getHistoric(RESULTS_PER_PAGE,
                                (currentPage - 1) * RESULTS_PER_PAGE);
                        historicAdapter.swap(historicList);
                        txtHistNav.setText(String.format(getResources().getString(R.string.i_of_n),
                                currentPage, totalPages));
                    }
                }
            });

            ibNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPage < totalPages) {
                        List<Historic> historicList = dbHelper.getHistoric(RESULTS_PER_PAGE,
                                currentPage * RESULTS_PER_PAGE);
                        historicAdapter.swap(historicList);
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

package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaginatorFragment extends Fragment {
    private static final String ARG_ITEMS_PER_PAGE = "items_per_page";
    private static final String ARG_TOTAL_OF_ITEMS = "total_of_items";

    private int itemsPerPage;
    private int totalOfItems;
    private int currentPage = 1;
    private int totalPages;

    private OnPageChangedListener mListener;

    public interface OnPageChangedListener {
        void onNextPressed(int currentPage);
        void onPreviousPressed(int currentPage);
    }

    public PaginatorFragment() {
        // Required empty public constructor
    }

    public static PaginatorFragment newInstance(int itemsPerPage, int totalOfItems) {
        PaginatorFragment fragment = new PaginatorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEMS_PER_PAGE, itemsPerPage);
        args.putInt(ARG_TOTAL_OF_ITEMS, totalOfItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemsPerPage = getArguments().getInt(ARG_ITEMS_PER_PAGE);
            totalOfItems = getArguments().getInt(ARG_TOTAL_OF_ITEMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_paginator, container, false);
        configureView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnPageChangedListener) {
            mListener = (OnPageChangedListener) getParentFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPageChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updatePaginator(int itemsPerPage, int totalOfItems) {
        this.itemsPerPage = itemsPerPage;
        this.totalOfItems = totalOfItems;
        updateView();
    }

    private void updateView() {
        View view = getView();
        if (view != null) {
            LinearLayout llRoot = (LinearLayout) view.findViewById(R.id.ll_root);
            TextView txtCurrPage = (TextView) view.findViewById(R.id.txt_curr_page);

            totalPages = (int) Math.ceil((double) totalOfItems / itemsPerPage);

            if (totalPages <= 1) {
                llRoot.setVisibility(View.GONE);
            } else {
                llRoot.setVisibility(View.VISIBLE);
                txtCurrPage.setText(String.format(getResources().getString(R.string.i_of_n),
                        1, totalPages));
            }
        }
    }

    private void configureView(View view) {
        final TextView txtCurrPage = (TextView) view.findViewById(R.id.txt_curr_page);
        LinearLayout root = (LinearLayout) view.findViewById(R.id.ll_root);
        ImageButton ibNext = (ImageButton) view.findViewById(R.id.ib_next);
        ImageButton ibPrev = (ImageButton) view.findViewById(R.id.ib_prev);

        totalPages = (int) Math.ceil((double) totalOfItems / itemsPerPage);

        if (totalPages <= 1) {
            root.setVisibility(View.GONE);
        } else {
            root.setVisibility(View.VISIBLE);
            txtCurrPage.setText(String.format(getResources().getString(R.string.i_of_n),
                    1, totalPages));

            ibPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPage > 1 && mListener != null) {
                        currentPage--;
                        mListener.onPreviousPressed(currentPage);
                        txtCurrPage.setText(String.format(getResources().getString(R.string.i_of_n),
                                currentPage, totalPages));
                    }
                }
            });

            ibNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPage < totalPages && mListener != null) {
                        mListener.onNextPressed(currentPage);
                        currentPage++;
                        txtCurrPage.setText(String.format(getResources().getString(R.string.i_of_n),
                                currentPage, totalPages));
                    }
                }
            });
        }
    }
}

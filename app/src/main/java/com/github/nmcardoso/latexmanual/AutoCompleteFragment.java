package com.github.nmcardoso.latexmanual;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AutoCompleteFragment extends Fragment {
    AutoCompleteAdapter adapter;
    DatabaseHelper dbHelper;
    List<Documentation> docList;

    public AutoCompleteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_complete, container, false);

        RecyclerView rvAutoComplete = (RecyclerView) view.findViewById(R.id.rv_auto_complete);
        dbHelper = new DatabaseHelper(getActivity());
        docList = dbHelper.search("", 15);
        adapter = new AutoCompleteAdapter(getActivity(), docList, "");
        rvAutoComplete.setAdapter(adapter);
        rvAutoComplete.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void updateListView(String query) {
        if (docList != null && adapter != null) {
            docList = dbHelper.search(query, 15);
            adapter.swap(docList, query);
        }
    }
}
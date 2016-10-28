package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.content.SharedPreferences;
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
    boolean commandSearch;

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

        dbHelper = new DatabaseHelper(getActivity());

        SharedPreferences prefs = getActivity().getSharedPreferences(
                getString(R.string.PREF_SETTINGS), Context.MODE_PRIVATE);
        String filter = prefs.getString(getString(R.string.KEY_SEARCH_FILTER),
                getString(R.string.VALUE_SHOW_ALL));
        commandSearch = filter.equals(getString(R.string.VALUE_COMMANDS_ONLY));

        setupAutoCompleteView(view);

        return view;
    }

    private void setupAutoCompleteView(View view) {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                getString(R.string.PREF_SETTINGS), Context.MODE_PRIVATE);
        Integer limit = prefs.getInt(getString(R.string.KEY_SEARCH_RESULTS),
                Integer.valueOf(getString(R.string.VALUE_SEARCH_RESULTS)));
        docList = dbHelper.search("", limit);
        adapter = new AutoCompleteAdapter(getActivity(), docList, "");

        RecyclerView rvAutoComplete = (RecyclerView) view.findViewById(R.id.rv_auto_complete);
        rvAutoComplete.setAdapter(adapter);
        rvAutoComplete.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void updateListView(String query) {
        if (commandSearch && !query.startsWith("\\")) {
            query = "\\" + query;
        }

        if (docList != null && adapter != null) {
            docList = dbHelper.search(query, 15);
            adapter.swap(docList, query);
        }
    }
}
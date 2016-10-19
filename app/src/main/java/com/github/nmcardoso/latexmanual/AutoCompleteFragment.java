package com.github.nmcardoso.latexmanual;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AutoCompleteFragment extends Fragment {
    SimpleCursorAdapter adapter;
    DatabaseHelper dbHelper;
    Cursor cursor;

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
        ListView listView = (ListView) view.findViewById(R.id.listViewAutoComplete);

        dbHelper = new DatabaseHelper(getActivity());
        cursor = dbHelper.search("");
        adapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[] {"title"},
                new int[] {android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cursor.moveToPosition(i);
                Intent intent = new Intent(getActivity(), DocViewerActivity.class);
                String fileNameColumn = DatabaseHelper.DOCUMENTATIONS_FILE_NAME;
                intent.putExtra(fileNameColumn,
                        cursor.getString(cursor.getColumnIndex(fileNameColumn)));
                startActivity(intent);
            }
        });

        return view;
    }

    public void updateListView(String query) {
        if (cursor != null && adapter != null) {
            cursor = dbHelper.search(query);
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
        }
    }
}
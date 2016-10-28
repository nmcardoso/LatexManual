package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private static final String FONT_SMALL = "small";
    private static final String FONT_DEFAULT = "default";
    private static final String FONT_LARGE = "large";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup UI
        setupClearFavoritesButton();
        setupClearHistoryButton();
        setupFontSizeSpinner();
        setupSearchFilterSpinner();
        setupSearchResultsSpinner();
    }

    private void setupClearHistoryButton() {
        Button btnClearHistory = (Button) findViewById(R.id.btn_clear_history);
        btnClearHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();

                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            DatabaseHelper databaseHelper = new DatabaseHelper(context);
                            int count = databaseHelper.deleteAllHistory();
                            Toast.makeText(context, getString(R.string.history_cleaned),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getString(R.string.continue_q))
                        .setMessage(getString(R.string.this_action_cant_be_undone_q))
                        .setPositiveButton(getString(R.string.yes), listener)
                        .setNegativeButton(getString(R.string.no), listener)
                        .show();
            }
        });
    }

    private void setupClearFavoritesButton() {
        Button btnClearFavorites = (Button) findViewById(R.id.btn_clear_favorites);
        btnClearFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();

                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            DatabaseHelper databaseHelper = new DatabaseHelper(context);
                            int count = databaseHelper.deleteAllFavorites();
                            CharSequence message = count > 0 ? getResources().getQuantityString(
                                    R.plurals.n_favorites_deleted, count, count) :
                                    getString(R.string.no_fav_to_be_deleted);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getString(R.string.continue_q))
                        .setMessage(getString(R.string.this_action_cant_be_undone_q))
                        .setPositiveButton(getString(R.string.yes), listener)
                        .setNegativeButton(getString(R.string.no), listener)
                        .show();
            }
        });
    }

    private void setupFontSizeSpinner() {
        Spinner spnFontSize = (Spinner) findViewById(R.id.spn_font_size);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFontSize.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_SETTINGS),
                MODE_PRIVATE);
        String fontSize = prefs.getString(getString(R.string.KEY_FONT_SIZE), FONT_DEFAULT);

        if (fontSize.equals(FONT_DEFAULT)) {
            spnFontSize.setSelection(1);
        } else if(fontSize.equals(FONT_LARGE)) {
            spnFontSize.setSelection(2);
        } else if(fontSize.equals(FONT_SMALL)) {
            spnFontSize.setSelection(0);
        }

        spnFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_SETTINGS),
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                String newSize;
                if (i == 0) {
                    newSize = FONT_SMALL;
                } else if (i == 1) {
                    newSize = FONT_DEFAULT;
                } else {
                    newSize = FONT_LARGE;
                }

                editor.putString(getString(R.string.KEY_FONT_SIZE), newSize);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setupSearchFilterSpinner() {
        Spinner spnFilter = (Spinner) findViewById(R.id.spn_search_filter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_filters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilter.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_SETTINGS),
                MODE_PRIVATE);
        String filter = prefs.getString(getString(R.string.KEY_SEARCH_FILTER),
                getString(R.string.VALUE_SHOW_ALL));

        if (filter.equals(getString(R.string.VALUE_SHOW_ALL))) {
            spnFilter.setSelection(0);
        } else if(filter.equals(getString(R.string.VALUE_COMMANDS_ONLY))) {
            spnFilter.setSelection(1);
        }

        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_SETTINGS),
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                String newFilter;
                if (i == 0) {
                    newFilter = getString(R.string.VALUE_SHOW_ALL);
                } else {
                    newFilter = getString(R.string.VALUE_COMMANDS_ONLY);
                }

                editor.putString(getString(R.string.KEY_SEARCH_FILTER), newFilter);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setupSearchResultsSpinner() {
        Spinner spnResults = (Spinner) findViewById(R.id.spn_search_results);

        Integer[] numArr = new Integer[] {10, 15, 20, 25};

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, numArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnResults.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_SETTINGS),
                MODE_PRIVATE);
        Integer num = prefs.getInt(getString(R.string.KEY_SEARCH_RESULTS), 15);
        int pos = adapter.getPosition(num);
        spnResults.setSelection(pos);

        spnResults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_SETTINGS),
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                Integer newValue = (Integer) adapterView.getItemAtPosition(i);
                editor.putInt(getString(R.string.KEY_SEARCH_RESULTS), newValue);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}

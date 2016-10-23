package com.github.nmcardoso.latexmanual;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button btnClearHistory = (Button) findViewById(R.id.btn_clear_history);
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
}

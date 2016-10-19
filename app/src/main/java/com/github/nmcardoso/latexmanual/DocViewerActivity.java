package com.github.nmcardoso.latexmanual;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class DocViewerActivity extends AppCompatActivity {

    private boolean isFavorite;
    private int docId;
    private WebView wvDocumentation;
    private MenuItem favButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_viewer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.docViewerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String fileName = getIntent().getStringExtra(DatabaseHelper.DOCUMENTATIONS_FILE_NAME);

        dbHelper = new DatabaseHelper(this);

        wvDocumentation = (WebView) findViewById(R.id.wv_documentation);
        WebSettings wvSettings = wvDocumentation.getSettings();
        wvSettings.setDefaultTextEncodingName("latin-1");
        wvDocumentation.loadUrl("file:///android_asset/" + fileName);

        wvDocumentation.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String[] pathArray = view.getUrl().split("/");
                String fileName = pathArray[pathArray.length - 1];
                docId = dbHelper.getDocumentationId(fileName);

                updateFavButton();

                dbHelper.insertHistory(docId);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doc_viewer, menu);

        favButton = menu.findItem(R.id.action_favorite);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_favorite:
                if (isFavorite) {
                    dbHelper.deleteFavorite(docId);
                    Toast.makeText(getBaseContext(), R.string.fav_removed,
                            Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.insertFavorite(docId);
                    Toast.makeText(getBaseContext(), R.string.fav_added,
                            Toast.LENGTH_SHORT).show();
                }
                updateFavButton();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (wvDocumentation.canGoBack()) {
            wvDocumentation.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void updateFavButton() {
        isFavorite = dbHelper.isFavorite(docId);

        if (isFavorite) {
            favButton.setIcon(getResources().getDrawable(R.drawable.ic_star));
        } else {
            favButton.setIcon(getResources().getDrawable(R.drawable.ic_star_outline));
        }
    }
}

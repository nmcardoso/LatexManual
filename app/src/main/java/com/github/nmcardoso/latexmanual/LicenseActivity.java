package com.github.nmcardoso.latexmanual;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        setupWebView();
    }

    private void setupWebView() {
        WebView wvLicence = (WebView) findViewById(R.id.wv_license);
        wvLicence.loadUrl("file:///android_asset/_license.html");
        wvLicence.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(view.getContext(), "Impossible to open this link",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}

package com.github.nmcardoso.latexmanual;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        configureUI();
    }

    private void configureUI() {
        // Version number
        TextView txtVersion = (TextView) findViewById(R.id.txt_version);
        txtVersion.setText(String.format(getString(R.string.app_version),
                getString(R.string.app_release)));

        // License listener
        Button btnLicense = (Button) findViewById(R.id.btn_license);
        btnLicense.setOnClickListener(localActivityClickListener(LicenseActivity.class));

        // 'Report a bug' listener
        Button btnBug = (Button) findViewById(R.id.btn_bug);
        btnBug.setOnClickListener(externalContentClickListener(getString(R.string.gh_new_issue_url)));

        // Github repo listener
        Button btnGithub = (Button) findViewById(R.id.btn_github);
        btnGithub.setOnClickListener(externalContentClickListener(getString(R.string.gh_repo_url)));
    }

    private View.OnClickListener externalContentClickListener(final String url) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                try {
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(view.getContext(), "Impossible to open this link",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private View.OnClickListener localActivityClickListener(final Class activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), activity);
                view.getContext().startActivity(intent);
            }
        };
    }
}

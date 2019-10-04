package com.publit.io;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Class to show Publit.io API's calling.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * Performe Initialization.
     */
    private void init() {

        //Find views.
        TextView mTextViewFiles = findViewById(R.id.files_tv);
        TextView mTextViewPlayers = findViewById(R.id.folders_tv);
        TextView mTextViewVersions = findViewById(R.id.version_tv);

        //Initialize Click Listners.
        mTextViewFiles.setOnClickListener(this);
        mTextViewPlayers.setOnClickListener(this);
        mTextViewVersions.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.files_tv:
                startActivity(new Intent(MainActivity.this, FilesActivity.class));
                break;

            case R.id.folders_tv:
                startActivity(new Intent(MainActivity.this, FoldersActivity.class));
                break;

            case R.id.version_tv:
                startActivity(new Intent(MainActivity.this, VersionsActivity.class));
                break;
        }

    }
}

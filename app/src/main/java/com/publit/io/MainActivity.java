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

    private TextView mTextViewFiles;

    private TextView mTextViewPlayers;

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
        mTextViewFiles = findViewById(R.id.files_tv);
        mTextViewPlayers = findViewById(R.id.players_tv);

        //Initialize Click Listners.
        mTextViewFiles.setOnClickListener(this);
        mTextViewPlayers.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.files_tv:
                startActivity(new Intent(MainActivity.this, FilesActivity.class));
                break;

            case R.id.players_tv:
                break;
        }

    }
}

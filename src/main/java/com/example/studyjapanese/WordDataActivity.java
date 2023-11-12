package com.example.studyjapanese;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class WordDataActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private String mWordData;
    private String mMeanData;
    private String mCategoryData;
    private String mExampleData;
    private int mID;

    public static boolean openMusic;
    private String newSongKey;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_data);

        setupSharedPreferences();
        setting();

        TextView tv_word = (TextView) findViewById(R.id.word_data);
        TextView tv_mean = (TextView) findViewById(R.id.mean_data);
        TextView tv_example = (TextView) findViewById(R.id.example_data);
        Intent intent=getIntent();
        mID=intent.getIntExtra("dataID",0);
        mWordData = intent.getStringExtra("wordData");
        mMeanData = intent.getStringExtra("meanData");
        mExampleData= intent.getStringExtra("exampleData");
        mCategoryData=intent.getStringExtra("categoryData");

        tv_word.setText(mWordData);
        tv_mean.setText(mMeanData);
        tv_example.setText(mExampleData);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_data, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.update) {
            Intent updateIntent = new Intent(WordDataActivity.this, UpdateWordActivity.class);
            updateIntent.putExtra("dataID",mID);
            updateIntent.putExtra("wordData", mWordData);
            updateIntent.putExtra("meanData", mMeanData);
            updateIntent.putExtra("categoryData",mCategoryData);
            updateIntent.putExtra("exampleData", mExampleData);
            startActivity(updateIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setAudio(sharedPreferences.getBoolean(getString(R.string.pref_set_audio_key),
                getResources().getBoolean(R.bool.pref_set_audio)));
        setSong(sharedPreferences.getString(getString(R.string.pref_set_song_key),
                getString(R.string.pref_song_blms_value)));
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    // Updates the screen if the shared preferences change.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_set_audio_key))) {
            setAudio(sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_set_audio)));
        }else if(key.equals(getString(R.string.pref_set_song_key))) {
            setSong(sharedPreferences.getString(getString(R.string.pref_set_song_key),
                    getString(R.string.pref_song_blms_value)));
        }
    }

    public void setSong(String newSongKey) {
        this.newSongKey = newSongKey;
    }
    public void setAudio(boolean openMusic) {
        this.openMusic = openMusic;
    }
    //setting music
    public void setting() {
        openMusic = !openMusic;
        if (!openMusic) {
            if(newSongKey.equals(getString(R.string.pref_song_lst_value))){
                music.play(this, R.raw.lst);
            }else if(newSongKey.equals(getString(R.string.pref_song_mjt_value))){
                music.play(this, R.raw.mjt);
            }else {
                music.play(this, R.raw.blms);
            }
            Toast.makeText(WordDataActivity.this,"Sound On",Toast.LENGTH_SHORT).show();
        } else {
            music.stop(this);
            Toast.makeText(WordDataActivity.this,"Sound Off",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(newSongKey.equals(getString(R.string.pref_song_lst_value))){
            music.play(this, R.raw.lst);
        }else if(newSongKey.equals(getString(R.string.pref_song_mjt_value))){
            music.play(this, R.raw.mjt);
        }else {
            music.play(this, R.raw.blms);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        music.stop(this);
    }

    @Override
    public void finish() {
        music.stop(this);
        super.finish();
    }
}

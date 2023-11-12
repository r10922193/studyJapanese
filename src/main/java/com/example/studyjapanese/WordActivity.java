package com.example.studyjapanese;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.studyjapanese.adapter.CategoryAdapter;
import com.example.studyjapanese.adapter.WordAdapter;
import com.example.studyjapanese.data.WordContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WordActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = WordActivity.class.getSimpleName();
    private static final int WORD_LOADER_ID = 0;
    RecyclerView mRecyclerView;
    private WordAdapter mAdapter;
    private String mCategory;
    private static String sortOrder;

    public static boolean openMusic;
    private String newSongKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSharedPreferences();
        setting();

        setContentView(R.layout.activity_word);
        mRecyclerView = (RecyclerView) findViewById(R.id.word_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new WordAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        Intent intent=getIntent();
        mCategory = intent.getStringExtra("Category");
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                int id = (int) viewHolder.itemView.getTag();
                String stringId = Integer.toString(id);
                Uri uri = WordContract.WordEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                getContentResolver().delete(uri, null, null);
                getSupportLoaderManager().restartLoader(WORD_LOADER_ID, null, WordActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton quiz = findViewById(R.id.quiz);
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] chineseMeaning = fillQuestions(mCategory);
                String[] japaneseWord = fillAnswers(mCategory);
                Intent quizIntent = new Intent(WordActivity.this, QuizActivity.class);
                //quizIntent.putExtra("quizCategory", mCategory);
                quizIntent.putExtra("questions", chineseMeaning);
                quizIntent.putExtra("answers", japaneseWord);
                quizIntent.putExtra("totalQuestions", japaneseWord.length);
                startActivity(quizIntent);
            }
        });

        getSupportLoaderManager().initLoader(WORD_LOADER_ID, null, this);
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
        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(WORD_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mWordData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mWordData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mWordData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                String selection = WordContract.WordEntry.COLUMN_CATEGORY+"=?";
                String[] selectionArg ={mCategory};
                try {
                    return getContentResolver().query(WordContract.WordEntry.CONTENT_URI,
                            null,
                            selection,
                            selectionArg,
                            sortOrder);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mWordData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_word) {
            Intent addWordIntent = new Intent(WordActivity.this, AddWordActivity.class);
            startActivity(addWordIntent);
            return true;
        }
        if (id == R.id.setting) {
            Intent settingIntent = new Intent(WordActivity.this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        if (id == R.id.sorting) {
            sortOrder = sortWord();
            getSupportLoaderManager().restartLoader(WORD_LOADER_ID, null, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String sortWord(){
        /*String selection = WordContract.WordEntry.COLUMN_CATEGORY+"=?";
        String[] selectionArg ={mCategory};
        getContentResolver().query(WordContract.WordEntry.CONTENT_URI,
                null,
                selection,
                selectionArg,
                WordContract.WordEntry.COLUMN_WORD+" desc");*/
        return WordContract.WordEntry._ID + " desc";
    }

    private String[] fillQuestions(String category){
        Cursor cursor = null;
        List<String> questions = new ArrayList<>();
        String selection = WordContract.WordEntry.COLUMN_CATEGORY+"=?";
        String[] selectionArg ={mCategory};
        try {
            cursor = getContentResolver().query(WordContract.WordEntry.CONTENT_URI,
                    null,
                    selection,
                    selectionArg,
                    null);
            while(cursor.moveToNext()) {
                questions.add(cursor.getString(cursor.getColumnIndexOrThrow(WordContract.WordEntry.COLUMN_MEAN)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }
        return questions.toArray(new String[0]);
    }

    private String[] fillAnswers(String category){
        Cursor cursor = null;
        List<String> answers = new ArrayList<>();
        String selection = WordContract.WordEntry.COLUMN_CATEGORY+"=?";
        String[] selectionArg ={mCategory};
        try {
            cursor = getContentResolver().query(WordContract.WordEntry.CONTENT_URI,
                    null,
                    selection,
                    selectionArg,
                    null);
            while(cursor.moveToNext()) {
                answers.add(cursor.getString(cursor.getColumnIndexOrThrow(WordContract.WordEntry.COLUMN_WORD)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }
        return answers.toArray(new String[0]);
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
            Toast.makeText(WordActivity.this,"Sound On",Toast.LENGTH_SHORT).show();
        } else {
            music.stop(this);
            Toast.makeText(WordActivity.this,"Sound Off",Toast.LENGTH_SHORT).show();
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

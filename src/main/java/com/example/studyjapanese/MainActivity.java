package com.example.studyjapanese;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
//import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.studyjapanese.adapter.CategoryAdapter;
import com.example.studyjapanese.data.WordContract;

import static android.app.AlarmManager.INTERVAL_DAY;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] chineseMenu = {"用餐","購物","學習","工作","交通"};
    private String[] englishMenu = {"Meal", "Shopping", "Study", "Job", "Traffic"};

    private final int PERMISSION_WRITE_STORAGE = 0;

    public static boolean openMusic;
    private String newSongKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        recyclerView = (RecyclerView) findViewById(R.id.category_recyclerview);
        //get permission for record_audio and play music
        getPermission(this, Manifest.permission.RECORD_AUDIO, 0);

        setupSharedPreferences();
        setting();

        Notify();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CategoryAdapter(this,chineseMenu, englishMenu);
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.setting) {
            Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //show notification
    public void Notify() {
        Intent intent = new Intent();
        intent.setClass(this, AlarmReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pending);
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
            Toast.makeText(MainActivity.this,"Sound On",Toast.LENGTH_SHORT).show();
        } else {
            music.stop(this);
            Toast.makeText(MainActivity.this,"Sound Off",Toast.LENGTH_SHORT).show();
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
    /**
     * 取得權限
     */
    public static void getPermission(Activity activity, String permission, int permissionCode) {

        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*get permission success*/
                    music.play(this, R.raw.blms);//執行播放音樂的方法
                } else {
                    /*get permission fail*/
                }
                break;
            default:
                break;
        }
    }

}

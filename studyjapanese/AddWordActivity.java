package com.example.studyjapanese;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studyjapanese.data.WordContract;

public class AddWordActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        final ArrayAdapter<CharSequence> categoryList= ArrayAdapter.createFromResource(AddWordActivity.this, R.array.Category, android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner=(Spinner) findViewById(R.id.spinnerCategory);
        spinner.setAdapter(categoryList);
    }

    public void onClickAddWord(View view) {
        // Not yet implemented
        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
        // If the EditText input is empty -> don't create an entry
        String input_Word = ((EditText) findViewById(R.id.editTextJapaneseWord)).getText().toString();
        String input_Meaning = ((EditText) findViewById(R.id.editTextChineseMeaning)).getText().toString();
        String input_Example = ((EditText) findViewById(R.id.editExample)).getText().toString();
        final Spinner spinner=(Spinner) findViewById(R.id.spinnerCategory);
        String mCategory=spinner.getSelectedItem().toString();
        if (input_Word.length() == 0 | input_Meaning.length() == 0 | input_Example.length() == 0) {
            return;
        }

        // Insert new  data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mCategory into the ContentValues
        contentValues.put(WordContract.WordEntry.COLUMN_WORD, input_Word);
        contentValues.put(WordContract.WordEntry.COLUMN_MEAN, input_Meaning);
        contentValues.put(WordContract.WordEntry.COLUMN_CATEGORY,mCategory);
        contentValues.put(WordContract.WordEntry.COLUMN_EXAMPLE, input_Example);
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(WordContract.WordEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }

        // Finish activity (this returns back to MainActivity)
        finish();

    }

}

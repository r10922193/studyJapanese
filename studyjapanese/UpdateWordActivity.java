package com.example.studyjapanese;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.studyjapanese.data.WordContract;

public class UpdateWordActivity extends AppCompatActivity {
    private int mID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_word_data);
        final ArrayAdapter<CharSequence> categoryList= ArrayAdapter.createFromResource(UpdateWordActivity.this, R.array.Category, android.R.layout.simple_spinner_dropdown_item);
        EditText etUpdateWord =(EditText) findViewById(R.id.updateJapaneseWord);
        EditText etUpdateMean = (EditText) findViewById(R.id.updateChineseMeaning);
        EditText etUpdateExample = (EditText) findViewById(R.id.updateExample);
        final Spinner spinner=(Spinner) findViewById(R.id.updateSpinnerCategory);
        spinner.setAdapter(categoryList);
        Intent intent=getIntent();
        mID=intent.getIntExtra("dataID",0);
        String mWordData = intent.getStringExtra("wordData");
        String mMeanData = intent.getStringExtra("meanData");
        String mExampleData= intent.getStringExtra("exampleData");
        String mCategoryData=intent.getStringExtra("categoryData");
        etUpdateWord.setText(mWordData);
        etUpdateMean.setText(mMeanData);
        spinner.setSelection(getCategoryIndex(mCategoryData));
        etUpdateExample.setText(mExampleData);
    }

    public void onClickUpdateWord(View view){
        EditText etUpdateWord =(EditText) findViewById(R.id.updateJapaneseWord);
        EditText etUpdateMean = (EditText) findViewById(R.id.updateChineseMeaning);
        EditText etUpdateExample = (EditText) findViewById(R.id.updateExample);
        final Spinner spinner=(Spinner) findViewById(R.id.updateSpinnerCategory);
        String update_Word = etUpdateWord.getText().toString();
        String update_Meaning = etUpdateMean.getText().toString();
        String update_Example = etUpdateExample.getText().toString();
        String mCategory = spinner.getSelectedItem().toString();
        String dataID = Integer.toString(mID);
        String selection = WordContract.WordEntry._ID+"=?";
        String[] selectionArgs ={dataID};

        ContentValues contentValues = new ContentValues();
        contentValues.put(WordContract.WordEntry.COLUMN_WORD, update_Word);
        contentValues.put(WordContract.WordEntry.COLUMN_MEAN, update_Meaning);
        contentValues.put(WordContract.WordEntry.COLUMN_CATEGORY,mCategory);
        contentValues.put(WordContract.WordEntry.COLUMN_EXAMPLE, update_Example);
        getContentResolver().update(WordContract.WordEntry.CONTENT_URI,contentValues,selection,selectionArgs);
        finish();
    }

    public int getCategoryIndex(String mCategory){
        String[] Category = {"Meal", "Shopping", "Study", "Job", "Traffic"};
        int CategoryIndex =0;
        for (int i=0; i<Category.length; i++){
            if (mCategory.equals(Category[i])){
                CategoryIndex=i;
                break;
            }
        }
        return CategoryIndex;
    }
}

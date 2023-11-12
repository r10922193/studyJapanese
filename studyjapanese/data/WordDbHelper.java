package com.example.studyjapanese.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDbHelper extends SQLiteOpenHelper {
    // The name of the database
    private static final String DATABASE_NAME = "wordsDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    WordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + WordContract.WordEntry.TABLE_NAME + " (" +
                WordContract.WordEntry._ID  + " INTEGER PRIMARY KEY, " +
                WordContract.WordEntry.COLUMN_WORD + " TEXT NOT NULL, " +
                WordContract.WordEntry.COLUMN_MEAN + " TEXT NOT NULL, " +
                WordContract.WordEntry.COLUMN_EXAMPLE + " TEXT NOT NULL, " +
                WordContract.WordEntry.COLUMN_CATEGORY + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
        initialData(db);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WordContract.WordEntry.TABLE_NAME);
        onCreate(db);
    }

    private void initialData(SQLiteDatabase db){
        String[] word = {"チップ", "しはらう", "スープ ", "シェフ ", "コーヒー",
                "きゃく", "エスカレーター", "てんいん", "かいもの",
                "せいと", "ノート", "まなぶ", "せんせい",
                "かいぎ", "マネージャー", "とりひきさき", "ビジネスマン",
                "しんご", "ホーム", "のりかえる", "えき"};
        String[] mean ={"小費", "付帳", "湯", "主廚", "咖啡",
                "顧客", "電扶梯", "店員", "購物",
                "學生", "筆記本", "學習", "老師",
                "會議","經理","客戶","商人",
                "信號", "月台", "轉乘", "車站"};
        String[] category ={"Meal", "Meal", "Meal", "Meal", "Meal",
                "Shopping", "Shopping", "Shopping", "Shopping",
                "Study", "Study", "Study", "Study",
                "Job", "Job", "Job", "Job",
                "Traffic", "Traffic", "Traffic", "Traffic"};
        String[] example ={"チップはいっさいお断り。／一律不收小费。", "食事代は支払った。／飯錢付了。", "濃いスープ。／濃湯；稠湯。", "フランス料理のシェフになるのは昔からの夢だった。／成为一名法國料理的主廚是我一直以來的夢想。", "コーヒーを沸かす／煮咖啡。",
                "客を大切にする。／好好接待顧客。", "エスカレーターにのる。／坐自動扶梯。", "あの店員は親切。／那位店員很熱情。", "彼女は買い物が好きだ／她喜歡買東西。",
                "小学校の生徒。／小學生。", "ノートをとる。／記筆記。", "先輩に学ぶ。/向前辈學習。", "先生になる／當老師。",
                "会議を開く。／舉行會議。", "ハンバーガー店のマネージャー。／漢堡店的經理。", "その会社は関西に取引先が多い／那所公司在關西有很多客戶。", "ビジネスマンの身だしなみ。／商人的儀容儀表。",
                "しんごうを守まもらなければ、危あぶない。闖紅燈是很危險的。", "三番ホーム。／三號月台。", "別の船に乗り換える。／改坐别的船。", "終着駅。／终點站。"
        };
        for (int i= 0; i<word.length; i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(WordContract.WordEntry.COLUMN_WORD, word[i]);
            contentValues.put(WordContract.WordEntry.COLUMN_MEAN, mean[i]);
            contentValues.put(WordContract.WordEntry.COLUMN_CATEGORY,category[i]);
            contentValues.put(WordContract.WordEntry.COLUMN_EXAMPLE, example[i]);
            db.insert(WordContract.WordEntry.TABLE_NAME,null, contentValues);
        }

    }

}

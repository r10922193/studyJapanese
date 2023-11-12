package com.example.studyjapanese.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class WordContract {
    public static final String AUTHORITY = "com.example.studyjapanese";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_WORDS = "words";

    /* WordEntry is an inner class that defines the contents of the task table */
    public static final class WordEntry implements BaseColumns {

        // WordEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORDS).build();


        // Task table and column names
        public static final String TABLE_NAME = "word";

        // Since WordEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_MEAN= "mean";
        public static final String COLUMN_EXAMPLE="example";
        public static final String COLUMN_CATEGORY = "category";

    }
}

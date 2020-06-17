package com.ulmtd.quizapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "quiz.db";

    public static final String HIGHEST_SCORE_TBL = "highest_score";
    public static final String HIGHEST_SCORE_ID = "ID";
    public static final String HIGHEST_SCORE_VALUE = "VALUE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + HIGHEST_SCORE_TBL + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, VALUE INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HIGHEST_SCORE_TBL);
        onCreate(sqLiteDatabase);
    }

    public boolean saveHighestScore_create(int score) {
        // check if score > scoreInDb then

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HIGHEST_SCORE_VALUE, score);
        long result = db.insert(HIGHEST_SCORE_TBL, null, contentValues);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean saveHighestScore(int score) {
        // check if score > scoreInDb then

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HIGHEST_SCORE_VALUE, score);
        db.update(HIGHEST_SCORE_TBL, contentValues, "ID = ?", new String[] {id});

        return true;
    }

    public Cursor getHighestScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + HIGHEST_SCORE_TBL, null);
        return res;
    }

}

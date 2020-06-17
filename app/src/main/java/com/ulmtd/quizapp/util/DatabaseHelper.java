package com.ulmtd.quizapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "quiz2.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists highest_score(ID int, VALUE int);");
        sqLiteDatabase.execSQL("create table if not exists question_index(ID int, VALUE int);");
        sqLiteDatabase.execSQL("create table if not exists current_score(ID int, VALUE int);");

        sqLiteDatabase.execSQL("insert into highest_score values('1234', '0');");
        sqLiteDatabase.execSQL("insert into question_index values('1234', '0');");
        sqLiteDatabase.execSQL("insert into current_score values('1234', '0');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS highest_score");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS question_index");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS current_score");

        onCreate(sqLiteDatabase);
    }

    public void saveHighestScore(int score) {
        int currScore = getHighestScore();
        if(currScore < score) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("VALUE", score);

            db.update("highest_score", cv, "ID=1234", null);
        }
    }

    public int getHighestScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from highest_score where ID=1234", null);
        res.moveToFirst();
        return res.getInt(1);
    }

    public void setCurrentQuestionIndex(int index) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("VALUE", index);

        db.update("question_index", cv, "ID=1234", null);
    }

    public int getCurrentQuestionIndex() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from question_index where ID=1234", null);
        res.moveToFirst();
        return res.getInt(1);
    }

    public void setCurrentScore(int currentScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("VALUE", currentScore);

        db.update("current_score", cv, "ID=1234", null);
    }

    public int getCurrentScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from current_score where ID=1234", null);
        res.moveToFirst();
        return res.getInt(1);
    }
}

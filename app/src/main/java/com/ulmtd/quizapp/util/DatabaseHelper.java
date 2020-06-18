package com.ulmtd.quizapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ulmtd.quizapp.model.Score;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "quiz2.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists scores(ID int, VALUE int);");
        sqLiteDatabase.execSQL("create table if not exists question_index(ID int, VALUE int);");
        sqLiteDatabase.execSQL("create table if not exists current_score(ID int, VALUE int);");

        sqLiteDatabase.execSQL("insert into scores values('1234', '0');");

        //dummy inserts
        sqLiteDatabase.execSQL("insert into scores values('1234', '200');");
        sqLiteDatabase.execSQL("insert into scores values('1234', '400');");
        sqLiteDatabase.execSQL("insert into scores values('1234', '30');");
        sqLiteDatabase.execSQL("insert into scores values('1234', '2');");

        sqLiteDatabase.execSQL("insert into question_index values('1234', '0');");
        sqLiteDatabase.execSQL("insert into current_score values('1234', '0');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS scores");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS question_index");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS current_score");

        onCreate(sqLiteDatabase);
    }

    public void saveScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("VALUE", score);
        cv.put("ID",1234);

        db.insert("scores",null,cv);
    }

    public int getHighestScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID, VALUE from scores", null);
        res.moveToFirst();
        return res.getInt(1);
    }

    public ArrayList<Score> getTopScores(Integer n){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {(n).toString()};
        Cursor res = db.rawQuery("select ID, VALUE from scores order by Value limit ?", args);
        res.moveToFirst();

        ArrayList<Score> scores = new ArrayList<>();
        for(int i=0; i< res.getCount(); ++i){
            scores.add(new Score(res.getInt(1)));
        }

        return scores;

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

package com.ulmtd.quizapp.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences preferences;

    public Preferences(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestScore(int score) {
        int currentScore = score;
        int lastScore = preferences.getInt("highest_score", 0);

        if(currentScore > lastScore) {
            preferences.edit().putInt("highest_score", currentScore).apply();
        }
    }

    public int getHighestScore() {
        return preferences.getInt("highest_score", 0);
    }

    public void setCurrentQuestionIndex(int index) {
        preferences.edit().putInt("question_index", index).apply();
    }

    public int getCurrentQuestionIndex() {
        return preferences.getInt("question_index", 0);
    }

    public void setCurrentScore(int currentScore) {
        preferences.edit().putInt("current_score", currentScore).apply();
    }

    public int getCurrentScore() {
        return preferences.getInt("current_score", 0);
    }
}

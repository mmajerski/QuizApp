package com.ulmtd.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ulmtd.quizapp.data.AnswerListAsyncResponse;
import com.ulmtd.quizapp.data.QuestionBank;
import com.ulmtd.quizapp.model.Question;
import com.ulmtd.quizapp.model.Score;
import com.ulmtd.quizapp.util.DatabaseHelper;
import com.ulmtd.quizapp.util.Preferences;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SCOREBOARD_MAX_LENGTH = 5;
    private long mLastClickTime = 0;

    private TextView questionTextView;
    private  TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView highestScoreTextView;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;
    private ArrayList<Score> history;

    private int scoreCounter = 0;
    private Score score;
    private TextView scoreTextView;
    private ScoreboardFragment scoreboard = null;

    private Preferences prefs;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.score_text);

        prefs = new Preferences(MainActivity.this);

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionCounterTextView = findViewById(R.id.counter_text);
        questionTextView = findViewById(R.id.question_textview);

        highestScoreTextView = findViewById(R.id.highest_score);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        // sqlite
        myDb = new DatabaseHelper(this);

        // get previous state
//        currentQuestionIndex = prefs.getCurrentQuestionIndex();
//        scoreCounter = prefs.getCurrentScore();
//        score.setScore(scoreCounter);

        // sqlite
        currentQuestionIndex = myDb.getCurrentQuestionIndex();
        Log.d("INDEX", "onCreate: " + currentQuestionIndex);
        scoreCounter = myDb.getCurrentScore();
        score = new Score(scoreCounter);

        scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

        // sqlite
        int highestScore = myDb.getHighestScore();
        highestScoreTextView.setText(MessageFormat.format("Highest Score: {0}", highestScore));

//        highestScoreTextView.setText(MessageFormat.format("Highest Score: {0}", String.valueOf(prefs.getHighestScore())));

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
                questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionArrayList.size()));
            }
        });

        history = myDb.getTopScores(SCOREBOARD_MAX_LENGTH);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.prev_button:
                if(currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.next_button:
                goToNextQuestion();
                break;
            case R.id.true_button:
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                checkAnswer(true);
                break;
            case R.id.false_button:
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                checkAnswer(false);
                break;
        }
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        if(userChooseCorrect == answerIsTrue) {
            fadeView();
            addPoints();
        } else {
            shakeAnimation();
            deductPoints();
        }
    }

    private void addPoints() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        setScoreboard();
        if(scoreboard!= null){
            scoreboard.setCurrentScore(new Score(scoreCounter));
            scoreboard.setScoreHistory(history);
            scoreboard.refreshScoreboard();
        }


    }

    private void deductPoints() {
        scoreCounter -= 100;
        if(scoreCounter > 0) {
            score.setScore(scoreCounter);
            scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

            if(scoreboard!= null){
                scoreboard.setCurrentScore(new Score(scoreCounter));
                scoreboard.setScoreHistory(history);
                scoreboard.refreshScoreboard();
            }
        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
            scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        }
    }

    private void setScoreboard(){
        //asd
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getQuestion();
        questionTextView.setText(question);
        questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionList.size()));
    }

    private void fadeView() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goToNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goToNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void goToNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }

    @Override
    protected void onPause() {
//        prefs.saveHighestScore(score.getScore());
        myDb.saveScore(score.getScore());
//        prefs.setCurrentQuestionIndex(currentQuestionIndex);
        myDb.setCurrentQuestionIndex(currentQuestionIndex);
//        prefs.setCurrentScore(scoreCounter);
        myDb.setCurrentScore(scoreCounter);
        super.onPause();
    }


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof ScoreboardFragment){
            this.scoreboard = (ScoreboardFragment) fragment;
        }
    }
}

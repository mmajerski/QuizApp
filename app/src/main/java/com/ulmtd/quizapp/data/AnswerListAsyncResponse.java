package com.ulmtd.quizapp.data;

import com.ulmtd.quizapp.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}

package com.ulmtd.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ulmtd.quizapp.model.Score;

import java.util.ArrayList;

public class ScoreboardFragment extends Fragment {
    private static final String TAG = "ScoreboardFragment";

    private ListView scoreboard_list = null;
    private ArrayList<Score> history = null;
    private Score current_score = null;
    private ArrayAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scoreboard_fragment,container,false);
        Log.d(TAG,"onCreateView called");

        ArrayList<String> data = new ArrayList<>();
        data.add("asd");
        data.add("zxc");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,data);

        scoreboard_list = view.findViewById(R.id.scoreboard_list);
        scoreboard_list.setAdapter(adapter);

        return view;

    }

    public void setScoreHistory(ArrayList<Score> history){
        this.history = history;
    }

    public void setCurrentScore(Score score){
        this.current_score = score;
    }

    public void refreshScoreboard(){
//        if (scoreboard_list == null){
//            this.scoreboard_list = getView().findViewById(R.id.scoreboard_list);
//        }
//        ArrayList<String> scoreboard_texts = new ArrayList<>();
//
//        int player_idx = getCurrentScorePosition();
//
//        int position = 1;
//        for(int i =0; i< history.size();++i){
//            if (i == player_idx){
//                scoreboard_texts.add(String.format("%d:  >>%d<<", position,current_score.getScore()));
//                ++position;
//            }
//            scoreboard_texts.add(String.format("%d:  >>%d<<", position,history.get(i).getScore()));
//            ++position;
//        }
//
//        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,scoreboard_texts);
//        scoreboard_list.setAdapter(adapter);
//        scoreboard_list.setClickable(false);
    }

    private int getCurrentScorePosition(){
        if( current_score == null || history == null){
            return 0;
        }

        for(int i =0; i< history.size();++i){
            if(history.get(i).getScore() < current_score.getScore()){
                return i+1;
            }
        }

        return history.size() + 1;
    }






}

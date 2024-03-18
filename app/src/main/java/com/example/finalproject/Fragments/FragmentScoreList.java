package com.example.finalproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Activity.ActivityScores;
import com.example.finalproject.Adapter.ScoreAdapter;
import com.example.finalproject.Models.Score;
import com.example.finalproject.Database.Prefs;
import com.example.finalproject.R;

import java.util.List;

public class FragmentScoreList extends Fragment {

    private RecyclerView rvScores;
    private ScoreAdapter adapter;
    private TextView noScoresTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_scores, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvScores = view.findViewById(R.id.rvScores);
        noScoresTv = view.findViewById(R.id.noScoresTv);
        rvScores.setLayoutManager(new LinearLayoutManager(requireContext()));


        List<Score> scores = Prefs.getInstance(requireContext()).getScores();
        if(scores.isEmpty()) {
            noScoresTv.setVisibility(View.VISIBLE);
        }
        ActivityScores activity = (ActivityScores) getActivity();
        System.out.println(scores.size());
        adapter = new ScoreAdapter(scores, activity);
        rvScores.setAdapter(adapter);
    }
}

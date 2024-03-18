package com.example.finalproject.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Logic.ScoreClickListener;
import com.example.finalproject.Models.Score;
import com.example.finalproject.R;

import java.util.Calendar;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private static final Calendar c = Calendar.getInstance();


    private final List<Score> scoreList;

    private final ScoreClickListener scoreClickListener;

    public ScoreAdapter(List<Score> scoreList, ScoreClickListener scoreClickListener) {
        this.scoreList = scoreList;
        this.scoreClickListener = scoreClickListener;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ScoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scoreList.get(position);
        holder.bind(score);
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }


    class ScoreViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTv, dateTv, scoreTv;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameItemTv);
            dateTv = itemView.findViewById(R.id.dateItemTv);
            scoreTv = itemView.findViewById(R.id.scoreItemTv);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Score score) {
            c.setTimeInMillis(score.getDate());
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            String date = day + "/" + month + "/" + year;
            this.nameTv.setText("Name: " + score.getName());
            this.dateTv.setText("Date: " + date);
            this.scoreTv.setText("Score: " + score.getScore());
            itemView.setOnClickListener(v -> scoreClickListener.onScoreClicked(score));
        }
    }
}

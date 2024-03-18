package com.example.finalproject.Models;

import android.content.Context;
import android.widget.ImageView;

import com.example.finalproject.R;

public class Magic extends GameItem {
    public Magic(Context context) {
        super(new ImageView(context));
        getView().setImageResource(R.drawable.magic);
    }
}

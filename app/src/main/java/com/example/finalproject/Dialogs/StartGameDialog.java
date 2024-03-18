package com.example.finalproject.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.finalproject.Activity.ActivityMain;
import com.example.finalproject.Constants;
import com.example.finalproject.R;

public class StartGameDialog {
    public static void show(
            String mode,
            LayoutInflater inflater,
            Context context) {
        View v = inflater.inflate(R.layout.name_layout, null, false);
        EditText nameEt = v.findViewById(R.id.nameEt);
        new AlertDialog.Builder(context)
                .setView(v)
                .setTitle("Harry potter 2D Experience")
                .setPositiveButton("Start", (dialogInterface, i) -> {
                    String name = nameEt.getText().toString();
                    if (name.isEmpty()) {
                        Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent gameIntent = new Intent(context, ActivityMain.class);

                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.NAME_ARG, name);
                    bundle.putString(Constants.MODE_ARG, mode);
                    gameIntent.putExtras(bundle);
                    context.startActivity(gameIntent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

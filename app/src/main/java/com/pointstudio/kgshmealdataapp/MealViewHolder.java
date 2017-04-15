package com.pointstudio.kgshmealdataapp;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MealViewHolder
{
    public TextView morning; // 아침 점심 저녁
    public TextView data1; // 밥
    public TextView data2; // 국
    public TextView data3; // 반찬들
    public TextView data4; //
    public TextView data5; //
    public TextView data6; //

    public FrameLayout choiceLayout;

    public ImageButton good;
    public ImageButton bad;

    public int time;

    public SeekBar choiceBar;

    MealViewHolder(TextView toMorning)
    {
        morning = toMorning;
        morning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (choiceLayout.getVisibility() == View.GONE) {
                    choiceLayout.setVisibility(View.VISIBLE);

                    ChoiceProcess choice = new ChoiceProcess(choiceBar, time);
                    choice.start();
                } else
                    choiceLayout.setVisibility(View.GONE);
            }
        });
    }

    void SetButton(ImageButton g, ImageButton b) {
        good = g;
        bad = b;
        // ----
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceProcess choice = new ChoiceProcess(true, time, choiceBar);
                choice.start();
            }
        });

        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceProcess choice = new ChoiceProcess(false, time, choiceBar);
                choice.start();
            }
        });
    }

    void SetChoiceBar(SeekBar choice) {
        choiceBar = choice;
        choiceBar.setThumb(null);
        choiceBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        choice.setMax(0);
        choiceBar.setProgressDrawable(MealMainActivity.instance.getResources().getDrawable(R.drawable.unchoice_progressbar));
    }
}

package com.pointstudio.kgshmealdataapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MealAdapter extends BaseAdapter
{
    private Context mContext = null;

    private ArrayList<MealListData> mListData;

    public MealAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        this.mListData = MealMainActivity.instance.mListData;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MealViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.meal_item, null);

            holder = new MealViewHolder((TextView) convertView.findViewById(R.id.textTime));

            holder.data1 = (TextView) convertView.findViewById(R.id.mealItem1);
            holder.data2 = (TextView) convertView.findViewById(R.id.mealItem2);
            holder.data3 = (TextView) convertView.findViewById(R.id.mealItem3);
            holder.data4 = (TextView) convertView.findViewById(R.id.mealItem4);
            holder.data5 = (TextView) convertView.findViewById(R.id.mealItem5);
            holder.data6 = (TextView) convertView.findViewById(R.id.mealItem6);
            holder.choiceLayout = (FrameLayout) convertView.findViewById(R.id.CHOICE_LAYOUT);

            holder.SetButton((ImageButton) convertView.findViewById(R.id.GOOD), (ImageButton) convertView.findViewById(R.id.BAD));

            holder.SetChoiceBar((SeekBar) convertView.findViewById(R.id.ChoiceBar));

            convertView.setTag(holder);
        } else {
            holder = (MealViewHolder) convertView.getTag();
        }

        MealListData mData = mListData.get(position);
        holder.morning.setText(mData.morning);
        holder.data1.setText(mData.data1);
        holder.data2.setText(mData.data2);
        holder.data3.setText(mData.data3);
        holder.data4.setText(mData.data4);
        holder.data5.setText(mData.data5);
        holder.data6.setText(mData.data6);

        holder.time = mData.time;


        ChoiceProcess choiceProcess = new ChoiceProcess(holder.choiceBar, holder.time);
        choiceProcess.start();

        return convertView;
    }
}

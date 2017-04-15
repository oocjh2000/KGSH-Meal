package com.pointstudio.kgshmealdataapp;

/**
 * Created by junsu on 2016-10-02.
 */

public class MealListData
{
    public String morning;
    public String data1;
    public String data2;
    public String data3;
    public String data4;
    public String data5;
    public String data6;

    public MealListData() {
    }

    public int time;

    // (아침,점심,저녁), 밥, 국, (김치 등등등), 우유
    public MealListData(String date, String item1, String item2, String item3, String item4, String item5, String item6) {
        morning = date;
        data1 = item1;
        data2 = item2;
        data3 = item3;
        data4 = item4;
        data5 = item5;
        data6 = item6;
    }
}

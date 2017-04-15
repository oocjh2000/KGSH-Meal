package com.pointstudio.kgshmealdataapp;

import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChoiceProcess extends Thread
{
    private boolean _good;
    private int     _time;
    private SeekBar _choice;

    boolean onlyChoice = false;

    public Handler ChoiceHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    Toast.makeText(MealMainActivity.instance.getContext(), "오늘 식단에  투표해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
                case 1: {
                    Toast.makeText(MealMainActivity.instance.getContext(), "오늘 이 식단에는 이미 투표하셨습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2: {
                    Toast.makeText(MealMainActivity.instance.getContext(), "해당 날짜에만 투표해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    };

    public ChoiceProcess(boolean good, int time, SeekBar choice) {
        _time = time;
        _good = good;
        _choice = choice;
        onlyChoice = false;
    }

    public ChoiceProcess(SeekBar choice, int time) {
        _choice = choice;
        onlyChoice = true;
        _time = time;
    }

    @Override
    public void run() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ThreeMeals", _time);

        if (!onlyChoice) {
            // 좋아요를 눌렀을 때
            // 좋아요 : 4
            // 싫어요 : 5
            if (MealMainActivity.instance.originalToday.compareTo(MealMainActivity.instance.todayZeroTen) == 0) {
                if (_good) {
                    // 오늘 투표를 했는가?
                    boolean todayChoice = MealMainActivity.instance.choiceData.getBoolean(MealMainActivity.instance.todayZeroTen + _time, false);
                    if (!todayChoice) {
                        MealMainActivity.instance.choiceDataEditor.putBoolean(MealMainActivity.instance.todayZeroTen + _time, true);
                        map.put("Choice", 4);
                        ChoiceHandler.sendMessage(Message.obtain(ChoiceHandler, 0));
                    } else {
                        map.put("Choice", 6);
                        ChoiceHandler.sendMessage(Message.obtain(ChoiceHandler, 1));
                    }
                } else {
                    // 오늘 투표를 했는가?
                    boolean todayChoice = MealMainActivity.instance.choiceData.getBoolean(MealMainActivity.instance.todayZeroTen + _time, false);
                    if (!todayChoice) {
                        MealMainActivity.instance.choiceDataEditor.putBoolean(MealMainActivity.instance.todayZeroTen + _time, true);
                        map.put("Choice", 5);
                        ChoiceHandler.sendMessage(Message.obtain(ChoiceHandler, 0));
                    } else {
                        map.put("Choice", 6);
                        ChoiceHandler.sendMessage(Message.obtain(ChoiceHandler, 1));
                    }
                }
            } else {
                ChoiceHandler.sendMessage(Message.obtain(ChoiceHandler, 2));
            }
        } else {
            map.put("Choice", 6);
        }

        String TempTodayMM = "";
        if (MealMainActivity.instance.todayMM < 10)
            TempTodayMM = "0";

        String TempTodayDD = "";
        if (MealMainActivity.instance.todayDD < 10)
            TempTodayDD = "0";

        String todayString = MealMainActivity.instance.todayYY + TempTodayMM + MealMainActivity.instance.todayMM + TempTodayDD + MealMainActivity.instance.todayDD;

        map.put("Date", todayString);

        MealMainActivity.instance.choiceDataEditor.commit();

        MealMainActivity.instance.aq.ajax("http://junsueg5737.dothome.co.kr/MealDatas/MakeData.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {
                    String GoodCount = "0";
                    String BadCount = "0";
                    JSONArray data = object.getJSONArray("MealChoice");

                    for (int i = 0; i < data.length(); i++) {
                        GoodCount = data.getJSONObject(i).getString("Good");
                        BadCount = data.getJSONObject(i).getString("Bad");
                    }

                    int nGoodCount = Integer.parseInt(GoodCount);
                    int nBadCount = Integer.parseInt(BadCount);

                    _choice.setMax(0);
                    _choice.setProgress(0);

                    _choice.setMax(nGoodCount + nBadCount);
                    _choice.setProgress(nGoodCount);


                    if (_choice.getMax() > 0)
                        _choice.setProgressDrawable(MealMainActivity.instance.getResources().getDrawable(R.drawable.choice_progressbar));
                    else
                        _choice.setProgressDrawable(MealMainActivity.instance.getResources().getDrawable(R.drawable.unchoice_progressbar));

                } catch (JSONException e) {

                }
            }
        });
    }
}
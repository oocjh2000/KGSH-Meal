package com.pointstudio.kgshmealdataapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

public class MealMainActivity extends Activity {

    // 학교 급식 홈페이지 주소
    public static String URL_SCHOOL_MEAL = "http://www.game.hs.kr/~game/2013/inner.php?sMenu=E4100";
    // AQuery Lib
    public AQuery aq = new AQuery(this);
    public URL url;

    public String originalToday;

    public String today;
    public String todayZeroTen;

    public Source source;
    public ProgressDialog progressDialog;

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;

    public ListView mealList;
    public MealAdapter mealAdapter;
    public ArrayList<MealListData> mListData;

    int todayYY, todayMM, todayDD, todayOfWeek, todayOfMonth;

    private VersionProcess versionProcess;

    public SharedPreferences choiceData;
    public SharedPreferences.Editor choiceDataEditor;

    public String TenValue = "0";
    public String deviceVersion;

    final String[] Weeks = {"일", "월", "화", "수", "목", "금", "토"};

    public static MealMainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_main);

        instance = this;

        mListData = new ArrayList<MealListData>();

        mealAdapter = new MealAdapter(this);
        mealList = (ListView) findViewById(R.id.mealListView);
        mealList.setAdapter(mealAdapter);

        versionProcess = new VersionProcess();

        // 투표 데이터
        choiceData = getSharedPreferences("ChoiceData", 0);

        choiceDataEditor = choiceData.edit();
        // 데이터가 저장되는지 확인
        // choiceDataEditor.clear();

        // 오늘 날자 표시
        CalToday();

        // ----------
        todayZeroTen = String.valueOf(todayMM) + "월";
        todayZeroTen += " ";
        todayZeroTen += String.valueOf(todayDD) + "일";
        todayZeroTen += "(" + Weeks[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1] + ")";

        // ----------

        todayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        aq.id(R.id.todayDate).text(today);

        // 투표시 현재 날자를 비교함
        originalToday = todayZeroTen;

        MoveDays();

        // 인터넷 연결상태 여부
        if (IsInternetConnect())
        {
            Toast.makeText(MealMainActivity.this, "인터넷과 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
            aq.id(R.id.NoMeal).text("네트워크를 확인해 주세요").visible();
            aq.id(R.id.NoMeal).getTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try
                    {
                        if(IsInternetConnect())
                        {
                            Toast.makeText(MealMainActivity.this, "인터넷과 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            aq.id(R.id.NoMeal).text("네트워크를 확인해 주세요").visible();
                        }
                        else
                            MealProcess();
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            // 스레드 실행
            try {
                aq.id(R.id.NoMeal).invisible();

                MealProcess();
                versionProcess.start();
                mealAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("ERROR", e + "");
            }
        }
    }

    public Context getContext()
    {
        return getApplicationContext();
    }

    public void CalToday()
    {
        // 오늘 날자 표시
        GregorianCalendar todayCalender = new GregorianCalendar();
        todayMM = todayCalender.get(todayCalender.MONTH) + 1;
        todayDD = todayCalender.get(todayCalender.DAY_OF_MONTH);
        todayYY = todayCalender.get(todayCalender.YEAR);

        todayCalender.set(todayCalender.get(todayCalender.YEAR), todayCalender.get(todayCalender.MONTH) + 1, todayDD);
        todayOfMonth = todayCalender.getActualMaximum(Calendar.DAY_OF_MONTH);

        today = String.valueOf(todayMM) + "월";
        today += " ";

        if (todayDD < 10)
            today += TenValue;

        today += String.valueOf(todayDD) + "일";
        today += "(" + Weeks[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1] + ")";
    }

    // -------------------------------------------> MealData를 불러오는 스레드
    private void MealProcess() throws IOException
    {
        new Thread() {
            @Override
            public void run() {
                if (!IsInternetConnect())
                {
                    Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                    Progress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog = ProgressDialog.show(MealMainActivity.this, "", "게시판 정보를 가져오는중 입니다.");
                        }
                    }, 0);

                    try {
                        url = new URL(URL_SCHOOL_MEAL);
                        InputStream html = url.openStream();
                        source = new Source(new InputStreamReader(html, "utf-8")); // 소스를 UTF-8로 인코딩한다.
                        source.fullSequentialParse(); // 순차적으로 구문 분석
                    } catch (Exception e) {
                        Log.d("ERROR", e + "");
                    }

                    // ----------- 작업
                    // 첫번째 테이블을 가져옴
                    Element MEAL_TABLE = (Element) source.getAllElements(HTMLElementName.TABLE).get(0);
                    Element MEAL_TBODY = (Element) MEAL_TABLE.getAllElements(HTMLElementName.TBODY).get(0);

                    int mealTime = 0;
                    boolean onlyDinner = false;

                    for (int tr = 0; tr < MEAL_TBODY.getAllElements(HTMLElementName.TR).size(); tr++) {
                        Element MEAL_TR = (Element) MEAL_TBODY.getAllElements(HTMLElementName.TR).get(tr);
                        Element MEAL_STRON = (Element) MEAL_TR.getAllElements(HTMLElementName.STRONG).get(0);
                        // 현재 날자와 불러온 날자가 같을 때

                        if (MEAL_STRON.getTextExtractor().toString().equals(today.toString())) {

                            for (int td = 1; td < MEAL_TR.getAllElements(HTMLElementName.TD).size(); td++) {
                                Element MEAL_TD = (Element) MEAL_TR.getAllElements(HTMLElementName.TD).get(td);
                                String FINAL_MEAL = MEAL_TD.getTextExtractor().toString();
                                String FINAL_MEALS[] = FINAL_MEAL.split(" 밥| ");
                                ;

                                MealListData listData = new MealListData();

                                switch (mealTime) {
                                    case 0:
                                        listData.morning = "아침";
                                        break;
                                    case 1:
                                        listData.morning = "점심";
                                        break;
                                    case 2:
                                        listData.morning = "저녁";
                                        break;
                                }
                                listData.time = mealTime;

                                boolean IsMeal = false;
                                int mealDataIdx = 0;


                                for (int i = 0; i < FINAL_MEALS.length; i++) {
                                    if (FINAL_MEALS[i].equals(""))
                                        continue;
                                    StringBuffer curData = new StringBuffer(FINAL_MEALS[i].toString());
                                    int idx = -1;

                                    boolean tempBob = false;
                                    idx = curData.indexOf("흰밥");
                                    if (idx != -1) {
                                        tempBob = true;
                                    }
                                    idx = -1;

                                    idx = curData.indexOf("흰");
                                    if (idx != -1) {
                                        if (!tempBob)
                                            curData.insert(idx + 1, " 밥");
                                        else
                                            curData.insert(1, " ");
                                    }

                                    switch (mealDataIdx) {
                                        case 0:
                                            listData.data1 = curData.toString();
                                            break;
                                        case 1:
                                            listData.data2 = curData.toString();
                                            break;
                                        case 2:
                                            listData.data3 = curData.toString();
                                            break;
                                        case 3:
                                            listData.data4 = curData.toString();
                                            break;
                                        case 4:
                                            listData.data5 = curData.toString();
                                            break;
                                        case 5:
                                            listData.data6 = curData.toString();
                                            break;
                                    }
                                    mealDataIdx += 1;
                                    IsMeal = true;
                                }

                                if (IsMeal) {
                                    mListData.add(listData);
                                    mealTime += 1;
                                }
                            }
                        }
                    }

                    // ----------- 종료
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aq.id(R.id.NoMeal).text("");
                            // 급식이 없을 때
                            if (mListData.size() == 0) {
                                int randomText = new Random().nextInt(4);
                                switch (randomText) {
                                    case 0:
                                        aq.id(R.id.NoMeal).text("급식이 없는 것 같습니다.");
                                        break;
                                    case 1:
                                        aq.id(R.id.NoMeal).text("매점으로 달려갑시다.");
                                        break;
                                    case 2:
                                        aq.id(R.id.NoMeal).text("아직 급식이 업데이트되지 않았습니다.");
                                        break;
                                    case 3:
                                        aq.id(R.id.NoMeal).text("아직 급식이 업데이트되지 않았습니다.");
                                        break;
                                }
                            }

                            // 식단이 하나만 있을때 ( 저녁 )
                            if (mListData.size() == 1) {
                                mListData.get(0).morning = "저녁";
                            }

                            // 작업이 끝나고 아이템 갱신
                            mealAdapter.notifyDataSetChanged();
                            progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                        }
                    }, 0);
                }
            }
        }.start();
    }

    // -------------------------------------------> 날자 이동
    // 날자의 이동은 일 ~ 월
    private void MoveDays(){
        Button.OnClickListener mClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tomorrow: {
                        todayDD += 1;
                        if (todayDD >= todayOfMonth)
                            todayDD = todayOfMonth;

                        todayOfWeek += 1;

                        if (todayOfWeek > 7) {
                            todayOfWeek = 7;
                            todayDD -= 1;
                        }
                    }
                    break;

                    case R.id.yesterday: {
                        int temp = todayDD;
                        todayDD -= 1;
                        if (todayDD < 1)
                            todayDD = 1;

                        if (temp == todayDD)
                            todayOfWeek += 1;

                        todayOfWeek -= 1;

                        if (todayOfWeek <= 0) {
                            todayOfWeek = 1;
                            todayDD += 1;
                        }
                    }
                    break;
                }

                today = String.valueOf(todayMM) + "월";
                today += " ";

                if (todayDD < 10)
                    today += TenValue;
                today += String.valueOf(todayDD) + "일";
                today += "(" + Weeks[todayOfWeek - 1] + ")";


                todayZeroTen = String.valueOf(todayMM) + "월";
                todayZeroTen += " ";
                todayZeroTen += String.valueOf(todayDD) + "일";
                todayZeroTen += "(" + Weeks[todayOfWeek - 1] + ")";


                aq.id(R.id.todayDate).text(today);

                // 인터넷 연결상태 여부
                if (IsInternetConnect()) {
                    Toast.makeText(MealMainActivity.this, "인터넷과 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    aq.id(R.id.NoMeal).text("네트워크를 확인해 주세요").visible();
                    aq.id(R.id.NoMeal).getTextView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if(IsInternetConnect())
                                {
                                    Toast.makeText(MealMainActivity.this, "인터넷과 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                    aq.id(R.id.NoMeal).text("네트워크를 확인해 주세요").visible();
                                }
                                else
                                    MealProcess();
                            } catch (Exception e) {

                            }
                        }
                    });
                } else {
                    // 스레드 실행
                    try {
                        aq.id(R.id.NoMeal).invisible();

                        mListData.clear();
                        mealAdapter = new MealAdapter(MealMainActivity.this);
                        mealList.setAdapter(mealAdapter);

                        MealProcess();
                        mealAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.d("ERROR", e + "");
                    }
                }
            }
        };

        aq.id(R.id.yesterday).getButton().setOnClickListener(mClickListener);
        aq.id(R.id.tomorrow).getButton().setOnClickListener(mClickListener);
    }

    private boolean IsInternetConnect() {
        cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); //모바일 데이터 여부
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //와이파이 여부
        return !mobile.isConnected() && !wifi.isConnected(); //결과값을 리턴
    }
}

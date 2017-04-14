package com.pointstudio.kgshmealdataapp.activity

import android.app.Activity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ListView
import com.androidquery.AQuery
import com.pointstudio.kgshmealdataapp.R
import com.pointstudio.kgshmealdataapp.utils.MealManager
import java.text.SimpleDateFormat
import java.util.*

class MealActivity : Activity() {
    var aq: AQuery? = null
    var mealManager: MealManager? = null
    var mealList: ListView? = null
    var mealAdapter: MealManager.MealAdapter? = null

    var noMealLayout: LinearLayout? = null
    var progressMealLayout: LinearLayout? = null

    // 2017-04
    var yearMonth = ""
    // 15
    var day = ""
    // 2017-04-15
    var masterDay = ""
    var simpleDateFormat = SimpleDateFormat("yyyy-MM")
    var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_activity)

        aq = AQuery(this)
        mealManager = MealManager(this)
        mealAdapter = MealManager.MealAdapter(this)
        mealList = findViewById(R.id.kg_meal_list) as ListView
        mealList!!.adapter = mealAdapter

        noMealLayout = findViewById(R.id.kg_meal_nomeal) as LinearLayout
        noMealLayout!!.visibility = View.VISIBLE
        progressMealLayout = findViewById(R.id.kg_meal_progress) as LinearLayout
        progressMealLayout!!.visibility = View.INVISIBLE

        simpleDateFormat = SimpleDateFormat("yyyy-MM")
        yearMonth = simpleDateFormat.format(Date())
        Log.i("Year Month", yearMonth)

        todayCal()
    }

    fun todayCal() {
        simpleDateFormat = SimpleDateFormat("dd")
        day = simpleDateFormat.format(Date())
        Log.i("Day", day)
        masterDay = yearMonth + "-" + day
        Log.i("Master Day", masterDay)
        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = simpleDateFormat.parse(masterDay)
        calendar.time = date
        val dayNum = calendar.get(Calendar.DAY_OF_WEEK) - 1
    }

    fun tomorrowCal() {

    }

    fun yesterdayCal() {

    }
}
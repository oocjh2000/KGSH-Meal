package com.pointstudio.kgshmealdataapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus

import net.htmlparser.jericho.Element
import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.Source

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.util.ArrayList
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.HashMap
import java.util.Random

class MealActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_activity)
    }

    companion object {
        // 학교 급식 홈페이지 주소
        var URL_SCHOOL_MEAL = "http://www.game.hs.kr/~game/2013/inner.php?sMenu=E4100"
    }
}
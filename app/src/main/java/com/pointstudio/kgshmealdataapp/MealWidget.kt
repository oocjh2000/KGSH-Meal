package com.pointstudio.kgshmealdataapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.content.ComponentName
import android.util.Log
import android.widget.TextView


/**
 * Implementation of App Widget functionality.
 */
open class MealWidget : AppWidgetProvider() {
    val ACTION_CLICK_MORNING = "net.plzpoint.widget.MEALCLICK"

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val rv = RemoteViews(context.packageName, R.layout.meal_widget)
        val action = intent.action

        if (action.equals(ACTION_CLICK_MORNING)) {
            when (intent.getIntExtra("mealId", 0)) {
                0 -> {
                }
                1 -> {
                }
                2 -> {
                }
            }
        }

        rv.setOnClickPendingIntent(R.id.meal_widget_morning, getPendingIntent(context, R.id.meal_widget_morning, 0))
        rv.setOnClickPendingIntent(R.id.meal_widget_brunch, getPendingIntent(context, R.id.meal_widget_brunch, 1))
        rv.setOnClickPendingIntent(R.id.meal_widget_dinner, getPendingIntent(context, R.id.meal_widget_dinner, 2))

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val cpName = ComponentName(context, MealWidget::class.java)
        appWidgetManager.updateAppWidget(cpName, rv)
    }

    fun getPendingIntent(context: Context, id: Int, mealId: Int): PendingIntent {
        val intent = Intent(context, MealWidget::class.java)
        intent.setAction(ACTION_CLICK_MORNING)
        intent.putExtra("mealID", mealId)
        return PendingIntent.getBroadcast(context, id, intent, 0)
    }
}
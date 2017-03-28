package com.pointstudio.kgshmealdataapp

/**
 * Created by junsu on 2017-03-12.
 */

open class MealData(title: String, data0: String, data1: String, data2: String, data3: String, data4: String, data5: String) {

    val title: String
    val data0: String
    val data1: String
    val data2: String
    val data3: String
    val data4: String
    val data5: String

    init {
        this.title = title
        this.data0 = data0
        this.data1 = data1
        this.data2 = data2
        this.data3 = data3
        this.data4 = data4
        this.data5 = data5
    }
}
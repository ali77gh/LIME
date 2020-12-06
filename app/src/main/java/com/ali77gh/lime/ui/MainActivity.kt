package com.ali77gh.lime.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ali77gh.lime.R
import com.ali77gh.lime.data.Event
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Event.getRepo(this).Insert(Event("sleep",Event.EventType.TIME_BASE,8.0))
//        Event.getRepo(this).Insert(Event("weight",Event.EventType.VALUE_BASE,60.0))
//        Event.getRepo(this).Insert(Event("drink coffee",Event.EventType.COUNT_BASE))
        log(Event.getRepo(this).all)
    }

    fun log(any: Any){
        test.append(Gson().toJson(any))
    }
}
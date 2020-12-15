package com.ali77gh.lime.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.EventLog
import com.ali77gh.lime.data.model.Routine
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        log(Event.getRepo(this).all)
    }

    fun log(any: Any){
        test.append(Gson().toJson(any))
    }
}
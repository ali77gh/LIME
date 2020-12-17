package com.ali77gh.lime.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.EventLog
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event

class ReportActivity : AppCompatActivity() {

    companion object{
        var event:Event?=null;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //TODO show charts and more reports
        //TODO load note and type
        //TODO edit name FAB
    }
}
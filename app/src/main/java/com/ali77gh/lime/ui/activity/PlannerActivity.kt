package com.ali77gh.lime.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Task
import com.ali77gh.lime.ui.adapter.PlannerListAdapter
import com.ali77gh.lime.ui.adapter.TaskListAdapter
import kotlinx.android.synthetic.main.activity_planner.*
import kotlinx.android.synthetic.main.fragment_task.*

class PlannerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

        Toast.makeText(this,"if you want to plan tasks like you see press confirm",Toast.LENGTH_LONG).show()


        planner_recycler.setHasFixedSize(true)
        planner_recycler.layoutManager = LinearLayoutManager(this)
        planner_recycler.adapter = PlannerListAdapter(Task.getRepo(this).getUnPlannedTasks() as List<Task>,this)

        planner_confirm.setOnClickListener{
            Task.getRepo(this).updateAll(
                (planner_recycler.adapter as PlannerListAdapter).plannedTasks
            )
            Toast.makeText(this,"tasks planned ;)",Toast.LENGTH_LONG).show()
            finish()
        }


    }
}
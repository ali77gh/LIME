package com.ali77gh.lime.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Task
import com.ali77gh.lime.ui.activity.PlannerActivity
import com.ali77gh.lime.ui.adapter.TaskListAdapter
import com.ali77gh.lime.ui.dialog.AddTaskDialog
import kotlinx.android.synthetic.main.fragment_task.*

class TaskFragment :Fragment(), Backable{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_task,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        task_fab.setOnClickListener{fabClick()}
        task_plan_fab.setOnClickListener{plannerFabClick()}
        loadList()
    }

    private fun loadList() {

        //TODO filter by tag

        task_recycler!!.setHasFixedSize(true)
        task_recycler!!.layoutManager = LinearLayoutManager(activity)
        task_recycler!!.adapter = TaskListAdapter(Task.getRepo(activity!!).all as List<Task>,activity!!,fragmentManager!!)
    }

    private fun fabClick(){
        AddTaskDialog {
            loadList()
        }.show(fragmentManager!!,"")
    }

    private fun plannerFabClick(){
        startActivity(Intent(activity,PlannerActivity::class.java))
    }

    override fun onBack(): Boolean {
        return false;
    }
}
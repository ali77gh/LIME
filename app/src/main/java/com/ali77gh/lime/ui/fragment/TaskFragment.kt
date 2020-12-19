package com.ali77gh.lime.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Task
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

        //TODO tree showing filter planned-unplanned-all

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
        Toast.makeText(activity,"planner engine coming soon...",Toast.LENGTH_SHORT).show()
        //TODO call planner engineeeeeeeee
    }

    override fun onBack(): Boolean {
        return false;
    }
}
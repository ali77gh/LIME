package com.ali77gh.lime.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Task
import com.ali77gh.lime.ui.dialog.AddTaskDialog
import com.ali77gh.lime.ui.dialog.AreYouSureDialog

class TaskListAdapter(
    private var listdata: List<Task>,
    private val activity: Activity,
    private var fragmentManager: FragmentManager
)  : RecyclerView.Adapter<TaskListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_task, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = listdata[position]

        holder.name.text = task.name

        if (task.haveFixedTime){
            holder.name.append(" (planned)")
        }

        holder.delete.setOnClickListener{
            AreYouSureDialog {
                Task.getRepo(activity).Remove(task.id)
                deleteFromDataSet(task)
            }.show(fragmentManager,"")
        }
        holder.edit.setOnClickListener{
            Toast.makeText(activity,"coming soon...", Toast.LENGTH_SHORT).show()
            AddTaskDialog({
                for (i in listdata.indices){
                    if (listdata[i].id==it.id) {
                        (listdata as ArrayList<Task>)[i] = it
                        break
                    }
                }
                notifyDataSetChanged()
            },task).show(fragmentManager,"")
        }
    }


    private fun deleteFromDataSet(task: Task){
        (listdata as ArrayList).remove(task)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView = itemView.findViewById(R.id.item_task_name) as TextView
        var edit : Button = itemView.findViewById(R.id.item_task_edit) as Button
        var delete : Button = itemView.findViewById(R.id.item_task_delete) as Button;
    }
}
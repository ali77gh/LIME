package com.ali77gh.lime.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.ali77gh.lime.R
import com.ali77gh.lime.data.PlannerEngine
import com.ali77gh.lime.data.model.Task
import java.util.*

class PlannerListAdapter(tasks:List<Task>,var activity: Activity) : RecyclerView.Adapter<PlannerListAdapter.ViewHolder>() {

    var plannedTasks:List<Task> = PlannerEngine(activity,tasks).getPlannedTasks()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_planner, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plannedTask = plannedTasks[position]

        val startDate = JalaliDateTime((plannedTask.duoDate/1000).toInt(), TimeZone.getDefault())
        val endDate = JalaliDateTime(((plannedTask.duoDate+plannedTask.neededTimeInMilis)/1000).toInt(), TimeZone.getDefault())
        val dayOfWeek = activity.resources.getStringArray(R.array.day_of_week)[startDate.dayOfWeek.value]
        holder.text.text =
            "${plannedTask.name} $dayOfWeek at ${startDate.hour}:${startDate.min}->${endDate.hour}:${endDate.min}"
    }

    override fun getItemCount(): Int {
        return plannedTasks.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text : TextView = itemView.findViewById(R.id.item_planner_text) as TextView
    }
}
package com.ali77gh.lime.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.DayWork
import com.ali77gh.lime.data.model.Routine
import com.ali77gh.lime.data.model.Task

class MyDayListAdapter(
    routines: List<Routine?>,
    tasks: List<Task?>,
    private val activity: Activity,
    private var fragmentManager: FragmentManager
) : RecyclerView.Adapter<MyDayListAdapter.ViewHolder>() {

    private var dayWorks : ArrayList<DayWork> =ArrayList()

    init {
        for (i in routines) dayWorks.add(i!!)
        for (i in tasks) dayWorks.add(i!!)

        dayWorks.sortBy { it.getStartTime() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_my_day, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayWork = dayWorks[position]

        when (dayWork) {
            is Task -> holder.icon.setImageDrawable(activity.getDrawable(R.drawable.ic_task))
            is Routine -> holder.icon.setImageDrawable(activity.getDrawable(R.drawable.ic_routine))
        }

        holder.name.text = dayWork.getWorkName()

        holder.time.text = "${dayWork.getStartTimeString()} -> ${dayWork.getEndTimeString()}"
    }

    override fun getItemCount(): Int {
        return dayWorks.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById<View>(R.id.item_my_day_icon) as ImageView
        var name: TextView = itemView.findViewById<View>(R.id.item_my_day_name) as TextView
        var time: TextView = itemView.findViewById<View>(R.id.item_my_day_time) as TextView
    }

}
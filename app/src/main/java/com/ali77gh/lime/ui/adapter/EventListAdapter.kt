package com.ali77gh.lime.ui.adapter

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.EventLog
import com.ali77gh.lime.ui.activity.ReportActivity
import com.ali77gh.lime.ui.dialog.AddEventLogDialog


class EventListAdapter(
    private var listdata: List<Event>,
    private val activity: Activity,
    private var fragmentManager: FragmentManager
    ) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_event, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Event = listdata[position]

        fun loadName(){
            holder.name.text = "${item.name} (${Event.normalizeType(item.type)})"
            if (EventLog.getRepo(activity).getLastLogOfEvent(item.id)?.isStart == true)
                holder.name.append("-> is started...")
        }
        loadName()

        holder.report.setOnClickListener {

            ReportActivity.event = item
            activity.startActivity(Intent(activity,ReportActivity::class.java))
        }

        holder.addLog.setOnClickListener {
            val d =AddEventLogDialog(item,needUiUpdate = {
                loadName()
            })
            d.show(fragmentManager,"")

        }
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name :TextView = itemView.findViewById<View>(R.id.item_event_name) as TextView
        var report: Button = itemView.findViewById<View>(R.id.item_event_report) as Button
        var addLog: Button = itemView.findViewById<View>(R.id.item_event_add_log) as Button
    }

}
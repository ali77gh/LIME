package com.ali77gh.lime.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Routine
import com.ali77gh.lime.data.model.Task
import com.ali77gh.lime.ui.dialog.AddRoutineDialog
import com.ali77gh.lime.ui.dialog.AddTaskDialog
import com.ali77gh.lime.ui.dialog.AreYouSureDialog

class RoutineListAdapter(
    private var listdata: List<Routine>,
    private val activity: Activity,
    private var fragmentManager: FragmentManager
) : RecyclerView.Adapter<RoutineListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_routine, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val routine = listdata[position]

        holder.name.text = routine.name

        holder.enable.isChecked = routine.enable

        holder.delete.setOnClickListener{
            AreYouSureDialog {
                Routine.getRepo(activity).Remove(routine.id)
                deleteFromDataSet(routine)
            }.show(fragmentManager,"")
        }

        holder.edit.setOnClickListener{
            AddRoutineDialog({
                for (i in listdata.indices){
                    if (listdata[i].id==it.id) {
                        (listdata as ArrayList<Routine>)[i] = it
                        break
                    }
                }
                notifyDataSetChanged()
            },routine).show(fragmentManager,"")
        }

        holder.enable.setOnCheckedChangeListener { _, b ->
            routine.enable = b
            Routine.getRepo(activity).Update(routine)
            notifyDataSetChanged()
        }

    }

    private fun deleteFromDataSet(routine: Routine){
        (listdata as ArrayList).remove(routine)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView = itemView.findViewById(R.id.item_routine_name) as TextView
        var edit : Button = itemView.findViewById(R.id.item_routine_edit) as Button
        var delete : Button = itemView.findViewById(R.id.item_routine_delete) as Button;
        var enable : SwitchCompat = itemView.findViewById(R.id.item_routine_enable) as SwitchCompat
    }
}
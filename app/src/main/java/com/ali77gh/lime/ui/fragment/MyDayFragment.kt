package com.ali77gh.lime.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali.uneversaldatetools.UDatePickerDialog
import com.ali.uneversaldatetools.date.DateSystem
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Routine
import com.ali77gh.lime.data.model.Task
import com.ali77gh.lime.ui.adapter.MyDayListAdapter
import kotlinx.android.synthetic.main.fragment_my_day.*
import java.util.*

class MyDayFragment :Fragment(), Backable{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_day,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        my_day_date.setOnClickListener {
            val datePicker = UDatePickerDialog()//TODO this is jalali (write dialog and get system from setting)
            datePicker.setListener(object : UDatePickerDialog.UDatePickerDialogListener{
                override fun onCancel() {}
                override fun onSelect(p0: Long, p1: DateSystem?) {
                    loadList(p0*1000)
                    my_day_date.text = "${p1!!.year}/${p1!!.month}/${p1!!.day}"
                }

            })
            datePicker.show(fragmentManager!!,"")
        }

        loadList()

        loadLisFunction = { loadList() }
    }

    private fun loadList(){
        val date = JalaliDateTime.Now()
        loadList(JalaliDateTime(date.year,date.month,date.day).toUnixTime().toLong() *1000L)
        my_day_date.text = "${date!!.year}/${date!!.month}/${date!!.day}"
    }

    companion object {
        private var loadLisFunction: (() -> Unit)? = null
        fun refresh(){
            loadLisFunction?.invoke()
        }
    }


    private fun loadList(firstOfDayInMilis:Long) {

        my_day_recycler!!.setHasFixedSize(true)
        my_day_recycler!!.layoutManager = LinearLayoutManager(activity)
        my_day_recycler!!.adapter = MyDayListAdapter(
            getDayRoutine(firstOfDayInMilis),
            getDayTasks(firstOfDayInMilis),
            activity!!,
            fragmentManager!!
        )
    }

    private fun getDayTasks(firstOfDayInMilis:Long):List<Task?>{
        return Task.getRepo(activity!!).getADayTasks(firstOfDayInMilis)
    }

    private fun getDayRoutine(firstOfDayInMIlis:Long):List<Routine?>{
        val dayOfWeek = JalaliDateTime((firstOfDayInMIlis/1000).toInt(), TimeZone.getDefault()).dayOfWeek.value
        return Routine.getRepo(activity!!).getADayEnableRoutines(dayOfWeek)
    }

    override fun onBack(): Boolean {
        return false;
    }
}
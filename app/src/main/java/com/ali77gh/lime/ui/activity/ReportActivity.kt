package com.ali77gh.lime.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.EventLog
import com.ali77gh.lime.ui.dialog.TimeIntervalSelectorDialog
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity() {

    companion object{
        var event:Event?=null;

        const val WEEK_IN_MILIS :Long = 604800000L // 1000*60*60*24*7
        const val MONTH_IN_MILIS :Long = 2592000000L // 1000*60*60*24*30
        const val YEAR_IN_MILIS :Long = 31536000000L // 1000*60*60*24*365
    }

    private var logs :List<EventLog?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        logs = EventLog.getRepo(this).getEventLogs(event!!.id)

        report_title.text = "${event!!.name} (${Event.normalizeType(event!!.type)})"

        if (event!!.note=="") report_note.visibility = GONE
        report_note.text = event!!.note

        when(event!!.type){
            Event.EventType.TIME_BASE -> {
                report_count_and_time_base_layout.visibility = VISIBLE
                report_value_base_layout.visibility = GONE
                loadTimeBaseReport()
            }
            Event.EventType.COUNT_BASE -> {
                report_count_and_time_base_layout.visibility = VISIBLE
                report_value_base_layout.visibility = GONE
                loadCountBaseReport()
            }
            Event.EventType.VALUE_BASE -> {
                report_value_base_layout.visibility = VISIBLE
                report_count_and_time_base_layout.visibility = GONE
                loadValueBaseReport()
            }
        }

        report_edit_event.setOnClickListener {
            //TODO edit name FAB
        }
        report_delete_event.setOnClickListener {
            //TODO edit name FAB
        }


    }

    private fun loadTimeBaseReport(){
        //TODO show charts and more reports
    }

    private fun loadCountBaseReport(){
        //TODO show charts and more reports
    }

    private fun loadValueBaseReport(){

        fun render(records: List<EventLog?>?){

            fun average(items:List<EventLog?>?):Double{
                var sum = 0.0;
                for (item in items!!)
                    sum+=item!!.value
                return sum/items.size
            }

            val entries: ArrayList<Entry> = ArrayList()
            for (data in records!!) entries.add(Entry(data!!.time.toFloat(), data!!.value.toFloat()))
            report_value_base_line_chart.data = LineData(LineDataSet(entries, ""))
            report_value_base_line_chart.invalidate() // refresh

            report_value_base_average_part.text ="average: ${average(records)}"
        }

        report_value_base_radio_group.setOnCheckedChangeListener { p0, p1 ->
            val now: Long = System.currentTimeMillis()

            when (p1) {
                R.id.report_value_base_radio_week ->
                    render(logs!!.filter { it!!.time > (now-WEEK_IN_MILIS) })
                R.id.report_value_base_radio_month ->
                    render(logs!!.filter { it!!.time > (now-MONTH_IN_MILIS) })
                R.id.report_value_base_radio_year ->
                    render(logs!!.filter { it!!.time > (now-YEAR_IN_MILIS) })
                R.id.report_value_base_radio_custom -> {
                    TimeIntervalSelectorDialog(onSelect =  {from:Long,to:Long ->
                        render(logs!!.filter { (to>it!!.time) && (it!!.time>from) })
                    }).show(supportFragmentManager,"")
                }
            }
        }

        report_value_base_radio_group.postDelayed(Runnable {
            report_value_base_radio_group.check(R.id.report_value_base_radio_week)
        },200)
    }
}
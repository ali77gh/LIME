package com.ali77gh.lime.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.EventLog
import com.ali77gh.lime.ui.dialog.AreYouSureDialog
import com.ali77gh.lime.ui.dialog.EditEventDialog
import com.ali77gh.lime.ui.dialog.TimeIntervalSelectorDialog
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity() {

    companion object {
        var event: Event? = null;

        const val WEEK_IN_MILIS: Long = 604800000L // 1000*60*60*24*7
        const val MONTH_IN_MILIS: Long = 2592000000L // 1000*60*60*24*30
        const val YEAR_IN_MILIS: Long = 31536000000L // 1000*60*60*24*365
    }

    private var logs: List<EventLog?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        logs = EventLog.getRepo(this).getEventLogs(event!!.id)

        loadEventInfo()

        report_value_base_radio_group.setOnCheckedChangeListener { p0, p1 ->
            val now: Long = System.currentTimeMillis()

            when (p1) {
                R.id.report_value_base_radio_week ->
                    render(logs!!.filter { it!!.time > (now - WEEK_IN_MILIS) })
                R.id.report_value_base_radio_month ->
                    render(logs!!.filter { it!!.time > (now - MONTH_IN_MILIS) })
                R.id.report_value_base_radio_year ->
                    render(logs!!.filter { it!!.time > (now - YEAR_IN_MILIS) })
                R.id.report_value_base_radio_custom -> {
                    TimeIntervalSelectorDialog(onSelect = { from: Long, to: Long ->
                        render(logs!!.filter { (to > it!!.time) && (it!!.time > from) })
                    }).show(supportFragmentManager, "")
                }
            }
        }

        report_value_base_radio_group.postDelayed(Runnable {
            report_value_base_radio_group.check(R.id.report_value_base_radio_week)
        }, 200)

        report_edit_event.setOnClickListener {
            EditEventDialog(event!!, refreshCb = {
                loadEventInfo()
            }).show(supportFragmentManager, "")
        }
        report_delete_event.setOnClickListener {
            AreYouSureDialog(cb = {
                Event.getRepo(this).Remove(event!!.id)
                finish()
            }).show(supportFragmentManager, "")
        }
    }

    private fun loadEventInfo() {
        report_title.text = "${event!!.name} (${Event.normalizeType(event!!.type)})"

        report_note.text = "${event!!.note} \n goal:${event!!.goal}"
    }

    private fun render(records: List<EventLog?>?) {
        when (event!!.type) {
            Event.EventType.TIME_BASE -> {
                report_count_and_time_base_layout.visibility = VISIBLE
                report_value_base_layout.visibility = GONE
                loadTimeBaseReport(records)
            }
            Event.EventType.COUNT_BASE -> {
                report_count_and_time_base_layout.visibility = VISIBLE
                report_value_base_layout.visibility = GONE
                loadCountBaseReport(records)
            }
            Event.EventType.VALUE_BASE -> {
                report_value_base_layout.visibility = VISIBLE
                report_count_and_time_base_layout.visibility = GONE
                loadValueBaseReport(records)
            }
        }
    }

    private fun loadTimeBaseReport(records: List<EventLog?>?) {
        //TODO show charts and more reports
    }

    private fun loadCountBaseReport(records: List<EventLog?>?) {
        //TODO show charts and more reports
    }

    private fun loadValueBaseReport(records: List<EventLog?>?) {

        fun averageAndSum(items: List<EventLog?>?): Array<Double> {
            var sum = 0.0;
            for (item in items!!)
                sum += item!!.value
            return arrayOf(sum / items.size, sum)
        }

        val entries: ArrayList<Entry> = ArrayList()
        for (data in records!!)
            entries.add(Entry(data!!.time.toFloat(), data.value.toFloat()))
        report_value_base_line_chart.data = LineData(LineDataSet(entries, ""))
        report_value_base_line_chart.invalidate() // refresh

        val (average, sum) = averageAndSum(records)
        report_value_base_average_part.text =
            "average: ${normalizeNumber(average)} \n sum: ${normalizeNumber(sum)} \n count: ${normalizeNumber(records.size)}"

    }

    // ye ragham ashar :)
    private fun normalizeNumber(number: Number):String{
        val numStr = number.toString()
        if (numStr.indexOf(".")==-1) return numStr
        if (numStr.indexOf(".0")!=-1) return numStr.substring(0,numStr.indexOf("."))
        return numStr.substring(0,numStr.indexOf(".")+2)
    }
}
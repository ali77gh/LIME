package com.ali77gh.lime.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.EventLog
import com.ali77gh.lime.ui.dialog.AreYouSureDialog
import com.ali77gh.lime.ui.dialog.EditEventDialog
import com.ali77gh.lime.ui.dialog.TimeIntervalSelectorDialog
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.activity_report.*
import java.util.*
import kotlin.collections.ArrayList


class ReportActivity : AppCompatActivity() {

    companion object {
        var event: Event? = null;

        const val DAY_IN_MILIS: Long = 86400000L // 1000*60*60*24
        const val WEEK_IN_MILIS: Long = 604800000L // 1000*60*60*24*7
        const val MONTH_IN_MILIS: Long = 2592000000L // 1000*60*60*24*30
        const val YEAR_IN_MILIS: Long = 31536000000L // 1000*60*60*24*365

        fun getDayOfWeek(milis: Long):Int{
            return JalaliDateTime((milis / 1000).toInt(), TimeZone.getDefault()).dayOfWeek.value
        }
        fun getHourPartFrom4(milis: Long):Int{
            return when(JalaliDateTime((milis / 1000).toInt(), TimeZone.getDefault()).hour){
                in 1..3 -> 0
                in 3..6 -> 1
                in 6..9 -> 2
                in 9..12 -> 3
                in 12..15 -> 4
                in 15..18 -> 5
                in 18..21 -> 6
                in 21..24 -> 7
                else -> throw RuntimeException("WTF?? getHourPartFrom4")
            }
        }
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
                    render(now - WEEK_IN_MILIS, System.currentTimeMillis())
                R.id.report_value_base_radio_month ->
                    render(now - MONTH_IN_MILIS, System.currentTimeMillis())
                R.id.report_value_base_radio_year ->
                    render(now - YEAR_IN_MILIS, System.currentTimeMillis())
                R.id.report_value_base_radio_custom -> {
                    TimeIntervalSelectorDialog(onSelect = { from: Long, to: Long ->
                        render(from, to)
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

    private fun render(start: Long, end: Long) {

        var records = logs!!.filter { (end > it!!.time) && (it!!.time > start) }

        when (event!!.type) {
            Event.EventType.TIME_BASE -> {
                report_count_and_time_base_layout.visibility = VISIBLE
                report_value_base_layout.visibility = GONE

                if (records.first()!!.isEnd)
                    records = records.drop(0) // remove first item if its end
                if (records.last()!!.isStart)
                    records = records.drop(records.lastIndex) // remove last item if its start

                loadTimeBaseReport(records, start, end)
            }
            Event.EventType.COUNT_BASE -> {
                report_count_and_time_base_layout.visibility = VISIBLE
                report_value_base_layout.visibility = GONE
                loadCountBaseReport(records, start, end)
            }
            Event.EventType.VALUE_BASE -> {
                report_value_base_layout.visibility = VISIBLE
                report_count_and_time_base_layout.visibility = GONE
                loadValueBaseReport(records)
            }
        }
    }

    private fun loadTimeBaseReport(records: List<EventLog?>?, start: Long, end: Long) {

        fun getSum():Long{
            var sum = 0L
            for (i in records!!.indices){
                if (i%2==1){
                    sum += records[i]!!.time - records[i-1]!!.time
                }
            }
            return (sum/1000)/60
        }

        fun loadDayAverage(){
            val diff = (end-start).toDouble()
            val diffInDays = (diff/ DAY_IN_MILIS)

            val averageTimeInDays=(getSum()/diffInDays)
            report_count_and_time_base_text.text = "this event happend ${records!!.size} minutes in seleced interval \n"
            report_count_and_time_base_text.append("average ${normalizeNumber(averageTimeInDays)} minutes in day\n")
            report_count_and_time_base_text.append("average ${normalizeNumber(averageTimeInDays * 7)} minutes in week\n")
            report_count_and_time_base_text.append("average ${normalizeNumber(averageTimeInDays * 30)} minutes in month")
        }

        fun loadChart1(){
            //TODO show chart
            report_count_and_time_base_line_chart.visibility = GONE
            report_count_and_time_base_line_chart_title.visibility = GONE
        }

        fun loadChart2(){
            //TODO show chart
            report_count_and_time_base_bar_chart.visibility = GONE
            report_count_and_time_base_bar_chart_title.visibility = GONE
        }

        fun loadChart3(){
            //TODO show chart
            report_count_and_time_base_bar_chart2.visibility = GONE
            report_count_and_time_base_bar_chart2_title.visibility = GONE
        }

        loadDayAverage()
        loadChart1()
        loadChart2()
        loadChart3()
    }

    private fun loadCountBaseReport(records: List<EventLog?>?, start: Long, end: Long) {

        //Text
        fun loadDayAverage(){
            val diff = (end-start).toDouble()
            val diffInDays = (diff/ DAY_IN_MILIS)

            val averageTimeInDays=(records!!.size/diffInDays)
            report_count_and_time_base_text.text = "this event happend ${records!!.size} \n"
            report_count_and_time_base_text.append("average ${normalizeNumber(averageTimeInDays)} times in day\n")
            report_count_and_time_base_text.append("average ${normalizeNumber(averageTimeInDays * 7)} times in week\n")
            report_count_and_time_base_text.append("average ${normalizeNumber(averageTimeInDays * 30)} times in month")
        }


        fun loadChart1(){
            //TODO show chart
            report_count_and_time_base_line_chart.visibility = GONE
            report_count_and_time_base_line_chart_title.visibility = GONE
        }

        fun loadChart2(){
            val entries: ArrayList<BarEntry> = ArrayList()

            for (i in 0..6)
                entries.add(BarEntry((i).toFloat(), records!!.filter { getDayOfWeek(it!!.time) == i }.size.toFloat()))

            report_count_and_time_base_bar_chart.data = BarData(BarDataSet(entries, "day of weeks")) //TODO translator

            val labels = arrayOf("sat", "sun", "mon", "tue", "wed", "thu", "fri") //TODO get from traslator class for farsi
            report_count_and_time_base_bar_chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            report_count_and_time_base_bar_chart.xAxis.granularity = 1f
            report_count_and_time_base_bar_chart.xAxis.isGranularityEnabled = true

            report_count_and_time_base_bar_chart.invalidate() // refresh
        }

        fun loadChart3(){
            val entries: ArrayList<BarEntry> = ArrayList()

            for (i in 0..7)
                entries.add(BarEntry((i).toFloat(), records!!.filter { getHourPartFrom4(it!!.time) == i }.size.toFloat()))

            report_count_and_time_base_bar_chart2.data = BarData(BarDataSet(entries, "hours of day")) //TODO translator

            val labels = arrayOf("0-3","3-6", "6-9","9-12","12-15","15-18", "18-21","21-24") //TODO get from traslator class for farsi
            report_count_and_time_base_bar_chart2.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            report_count_and_time_base_bar_chart2.xAxis.granularity = 1f
            report_count_and_time_base_bar_chart2.xAxis.isGranularityEnabled = true

            report_count_and_time_base_bar_chart2.invalidate() // refresh
        }

        loadDayAverage()
        loadChart1()
        loadChart2()
        loadChart3()

    }

    private fun loadValueBaseReport(records: List<EventLog?>?) {

        fun loadAverageAndSum(){
            var sum = 0.0;
            for (item in records!!)
                sum += item!!.value

            report_value_base_text.text =
                "average: ${normalizeNumber(sum / records.size)} \n" +
                        " sum: ${normalizeNumber(sum)} \n" +
                        " count: ${normalizeNumber(records!!.size)}"
        }

        //text
        loadAverageAndSum()

        //chart
        val entries: ArrayList<Entry> = ArrayList()
        for (data in records!!)
            entries.add(Entry(data!!.time.toFloat(), data.value.toFloat()))

        report_value_base_line_chart.data = LineData(LineDataSet(entries, ""))
        report_value_base_line_chart.invalidate() // refresh
    }

    // ye ragham ashar :)
    private fun normalizeNumber(number: Number):String{
        val numStr = number.toString()
        if (numStr.indexOf(".")==-1) return numStr
        if (numStr.indexOf(".0")!=-1) return numStr.substring(0, numStr.indexOf("."))
        return numStr.substring(0, numStr.indexOf(".") + 2)
    }
}
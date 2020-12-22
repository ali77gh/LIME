package com.ali77gh.lime.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.ali.uneversaldatetools.UDatePickerDialog
import com.ali.uneversaldatetools.date.DateSystem
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_task.*
import kotlinx.android.synthetic.main.dialog_time_interval_selector.*
import java.util.*
import kotlin.collections.ArrayList

class AddTaskDialog(
    private val cb: (task:Task) -> Unit,
    private val task: Task? = null
) : BottomSheetDialogFragment() {


    private val isEditMode = task != null


    private var eventId: String? = null;
    private var isFixedTime: Boolean? = null

    private var selectedFixedDay: Long? = null
    private var selectedDeadLineDay: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(activity, "enter start time in 24-hour time system", Toast.LENGTH_LONG)
            .show()

        setupLayoutSwitching()

        new_task_event_relation.setOnClickListener {
            SelectTimeBaseEventDialog { id, name ->
                eventId = id;
                new_task_event_relation.text = "Relation with event(optional): $name"
            }.show(fragmentManager!!, "")
        }

        setupDayPickers()

        if (isEditMode) loadCurrent()

        new_task_add_btn.setOnClickListener {

            if (!validation()) return@setOnClickListener

            var newTask: Task? = null
            when (new_task_type.checkedRadioButtonId) {
                R.id.new_task_type_fixed_time -> {
                    newTask = Task.newFixedTaskInstance(
                        new_task_name.text.toString(),
                        new_task_note.text.toString(),
                        ArrayList(),//TODO get tags
                        getNeededTime(),
                        getDuoDate(),
                        eventId
                    )
                }
                R.id.new_task_type_planable -> {
                    newTask = Task(
                        new_task_name.text.toString(),
                        new_task_note.text.toString(),
                        ArrayList(),//TODO get tags
                        getNeededTime(),
                        eventId,
                        new_task_priority.progress,
                        getDeadLine(),
                        new_task_mental_difficulty.progress,
                        new_task_physical_difficulty.progress,
                        System.currentTimeMillis()
                    )
                }
            }
            if (isEditMode) {
                newTask!!.id = task!!.id
                Task.getRepo(activity!!).Update(newTask)
            } else {
                Task.getRepo(activity!!).Insert(newTask)
            }

            dismiss()
            cb(newTask!!)
        }
    }

    private fun setupDayPickers() {

        //start time
        new_task_start_time_day.setOnClickListener {
            val datePicker =
                UDatePickerDialog()//TODO this is jalali (write dialog and get system from setting)
            datePicker.setListener(object : UDatePickerDialog.UDatePickerDialogListener {
                override fun onCancel() {}

                override fun onSelect(p0: Long, p1: DateSystem?) {
                    selectedFixedDay = p0 * 1000
                    new_task_start_time_day.text = "${p1!!.year}/${p1!!.month}/${p1!!.day}"
                }

            })
            datePicker.show(fragmentManager!!, "")
        }

        //deadline
        new_task_dead_line_day.setOnClickListener {
            val datePicker =
                UDatePickerDialog()//TODO this is jalali (write dialog and get system from setting)
            datePicker.setListener(object : UDatePickerDialog.UDatePickerDialogListener {
                override fun onCancel() {}

                override fun onSelect(p0: Long, p1: DateSystem?) {
                    selectedDeadLineDay = p0 * 1000
                    new_task_dead_line_day.text = "${p1!!.year}/${p1!!.month}/${p1!!.day}"
                }

            })
            datePicker.show(fragmentManager!!, "")
        }
    }

    private fun getNeededTime(): Long {
        return (new_task_need_time_min.text.toString().toLong() * 60 * 1000) +
                (new_task_need_time_hour.text.toString().toLong() * 60 * 60 * 1000)
    }

    private fun getDuoDate(): Long {
        return (new_task_start_time_min.text.toString().toLong() * 60 * 1000) +
                (new_task_start_time_hour.text.toString().toLong() * 60 * 60 * 1000) +
                selectedFixedDay!!
    }

    private fun getDeadLine(): Long {

        if (selectedDeadLineDay == null) return 0
        if (new_task_dead_line_min.text.toString() == "") new_task_dead_line_min.setText("0")
        if (new_task_dead_line_hour.text.toString() == "") new_task_dead_line_hour.setText("0")

        return (new_task_dead_line_min.text.toString().toInt() * 60 * 1000) +
                (new_task_dead_line_hour.text.toString().toInt() * 60 * 60 * 1000) +
                selectedDeadLineDay!!
    }

    private fun setupLayoutSwitching() {
        new_task_type_planable_layout.visibility = GONE
        new_task_type_fixed_time_layout.visibility = GONE

        new_task_type_planable.setOnClickListener {
            isFixedTime = false
            new_task_type_planable_layout.visibility = VISIBLE
            new_task_type_fixed_time_layout.visibility = GONE
        }

        new_task_type_fixed_time.setOnClickListener {
            isFixedTime = true
            new_task_type_planable_layout.visibility = GONE
            new_task_type_fixed_time_layout.visibility = VISIBLE
        }
    }


    private fun validation(): Boolean {

        //TODO check time conflict
        //TODO more validation
        when {
            new_task_name.text.toString() == "" -> {
                Toast.makeText(activity, "enter name", Toast.LENGTH_SHORT).show()
                return false
            }
            isFixedTime == null -> {
                Toast.makeText(activity, "select type", Toast.LENGTH_SHORT).show()
                return false
            }
            new_task_need_time_hour.text.toString() == "" -> {
                Toast.makeText(activity, "enter needed time hour", Toast.LENGTH_SHORT).show()
                return false
            }
            new_task_need_time_min.text.toString() == "" -> {
                Toast.makeText(activity, "enter needed time min", Toast.LENGTH_SHORT).show()
                return false
            }
            new_task_start_time_hour.text.toString() == "" && isFixedTime!! -> {
                Toast.makeText(activity, "enter fixed start time hour", Toast.LENGTH_SHORT).show()
                return false
            }
            new_task_start_time_min.text.toString() == "" && isFixedTime!! -> {
                Toast.makeText(activity, "enter fixed start time min", Toast.LENGTH_SHORT).show()
                return false
            }
            selectedFixedDay == null && isFixedTime!! -> {
                Toast.makeText(activity, "select fixed day", Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return true
    }

    private fun loadCurrent() {

        new_task_add_btn.text = "Edit"

        //TODO use post if layout switching not worked

        task!!
        new_task_type_fixed_time.postDelayed({
            if (task.haveFixedTime) {
                new_task_type_fixed_time.isChecked = true
                new_task_type_fixed_time.callOnClick()
            }
            else {
                new_task_type_planable.isChecked = true
                new_task_type_planable.callOnClick()
            }
        },200)


        //tests
        new_task_name.setText(task.name)
        new_task_note.setText(task.note)

        //progress
        new_task_priority.progress = task.priority
        new_task_mental_difficulty.progress = task.mentalDifficulty
        new_task_physical_difficulty.progress = task.physicalDifficulty

        //event
        eventId = task.eventId
        if (eventId!=null){
            val eventName = Event.getRepo(activity!!).getById(eventId)
            new_task_event_relation.text = "Relation with event(optional): $eventName"
        }

        //dates
        if (task.duoDate!=0L){
            val date = JalaliDateTime((task.duoDate/1000).toInt(), TimeZone.getDefault())
            new_task_start_time_hour.setText(date.hour.toString())
            new_task_start_time_min.setText(date.min.toString())
            new_task_start_time_day.text = "${date!!.year}/${date!!.month}/${date!!.day}"
            selectedFixedDay=JalaliDateTime(date.year,date.month,date.day).toUnixTime().toLong() * 1000L
        }
        if (task.deadLine!=0L){
            val date = JalaliDateTime((task.deadLine/1000).toInt(), TimeZone.getDefault())
            new_task_dead_line_hour.setText(date.hour.toString())
            new_task_dead_line_min.setText(date.min.toString())
            new_task_dead_line_day.text = "${date!!.year}/${date!!.month}/${date!!.day}"
            selectedDeadLineDay=JalaliDateTime(date.year,date.month,date.day).toUnixTime().toLong() * 1000L
        }
        if (task.neededTimeInMilis!=0L){
            val date = JalaliDateTime((task.deadLine/1000).toInt(), TimeZone.getDefault())
            new_task_need_time_hour.setText(date.hour.toString())
            new_task_need_time_min.setText(date.min.toString())
        }
    }
}
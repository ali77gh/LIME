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
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_task.*
import kotlinx.android.synthetic.main.dialog_time_interval_selector.*

class AddTaskDialog(private val cb: ()->Unit) : BottomSheetDialogFragment() {

    var eventId:String? = null;
    var isFixedTime:Boolean?=null

    var selectedFixedDay:Long?=null
    var selectedDeadLine:Long?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_task,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(activity,"enter start time in 24-hour time system", Toast.LENGTH_LONG).show()

        setupLayoutSwitching()

        new_task_event_relation.setOnClickListener {
            SelectTimeBaseEventDialog { id, name ->
                eventId = id;
                new_task_event_relation.text = "Relation with event(optional): $name"
            }.show(fragmentManager!!,"")
        }

        setupDayPickers()


        new_task_add_btn.setOnClickListener{

            if (!validation()) return@setOnClickListener

            when(new_task_type.checkedRadioButtonId){
                R.id.new_task_type_fixed_time->{
                    Task.getRepo(activity!!).Insert(
                        Task.newFixedTaskInstance(
                            new_task_name.text.toString(),
                            new_task_note.text.toString(),
                            ArrayList(),//TODO get tags
                            getNeededTime(),
                            getDuoDate(),
                            eventId
                        )
                    )
                }
                R.id.new_task_type_planable->{
                    Task.getRepo(activity!!).Insert(
                        Task(
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
                    )
                }
            }

            dismiss()
            cb()
        }
    }

    private fun setupDayPickers(){

        //start time
        new_task_start_time_day.setOnClickListener {
            val datePicker = UDatePickerDialog()//TODO this is jalali (write dialog and get system from setting)
            datePicker.setListener(object : UDatePickerDialog.UDatePickerDialogListener{
                override fun onCancel() {}

                override fun onSelect(p0: Long, p1: DateSystem?) {
                    selectedFixedDay = p0*1000
                    new_task_start_time_day.text = "${p1!!.year}/${p1!!.month}/${p1!!.day}"
                }

            })
            datePicker.show(fragmentManager!!,"")
        }

        //deadline
        new_task_dead_line_day.setOnClickListener {
            val datePicker = UDatePickerDialog()//TODO this is jalali (write dialog and get system from setting)
            datePicker.setListener(object : UDatePickerDialog.UDatePickerDialogListener{
                override fun onCancel() {}

                override fun onSelect(p0: Long, p1: DateSystem?) {
                    selectedDeadLine = p0*1000
                    new_task_dead_line_day.text = "${p1!!.year}/${p1!!.month}/${p1!!.day}"
                }

            })
            datePicker.show(fragmentManager!!,"")
        }
    }

    private fun getNeededTime():Int{
        return (new_task_need_time_min.text.toString().toInt() *60*1000) +
                (new_task_need_time_hour.text.toString().toInt() *60*60*1000)
    }
    private fun getDuoDate():Long{
        return (new_task_start_time_min.text.toString().toInt() *60*1000) +
                (new_task_start_time_hour.text.toString().toInt() *60*60*1000) +
                selectedFixedDay!!
    }
    private fun getDeadLine():Long{

        if (selectedDeadLine==null) return 0
        if (new_task_dead_line_min.text.toString()=="") new_task_dead_line_min.setText("0")
        if (new_task_dead_line_hour.text.toString()=="") new_task_dead_line_hour.setText("0")

        return (new_task_dead_line_min.text.toString().toInt() *60*1000) +
                (new_task_dead_line_hour.text.toString().toInt() *60*60*1000) +
                selectedDeadLine!!
    }

    private fun setupLayoutSwitching(){
        new_task_type_planable_layout.visibility=GONE
        new_task_type_fixed_time_layout.visibility=GONE

        new_task_type_planable.setOnClickListener {
            new_task_type_planable_layout.visibility=VISIBLE
            new_task_type_fixed_time_layout.visibility=GONE
            isFixedTime = false
        }

        new_task_type_fixed_time.setOnClickListener {
            isFixedTime = true
            new_task_type_planable_layout.visibility=GONE
            new_task_type_fixed_time_layout.visibility=VISIBLE
        }
    }


    private fun validation():Boolean{

        //TODO check time conflect
        when {
            new_task_name.text.toString()=="" -> {
                Toast.makeText(activity,"enter name", Toast.LENGTH_SHORT).show()
                return false
            }
            isFixedTime==null->{
                Toast.makeText(activity,"select type", Toast.LENGTH_SHORT).show()
                return false
            }
            new_task_need_time_hour.text.toString()=="" -> {
                Toast.makeText(activity,"enter needed time hour", Toast.LENGTH_SHORT).show()
                return false
            }
            new_task_need_time_min.text.toString()=="" -> {
                Toast.makeText(activity,"enter needed time min", Toast.LENGTH_SHORT).show()
                return false
            }
            new_task_start_time_hour.text.toString()=="" && isFixedTime!! -> {
                Toast.makeText(activity,"enter fixed start time hour", Toast.LENGTH_SHORT).show()
                return false
            }
            new_task_start_time_min.text.toString()=="" && isFixedTime!! -> {
                Toast.makeText(activity,"enter fixed start time min", Toast.LENGTH_SHORT).show()
                return false
            }
            selectedFixedDay==null && isFixedTime!! -> {
                Toast.makeText(activity,"select fixed day", Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return true
    }
}
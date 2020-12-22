package com.ali77gh.lime.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.Routine
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_routine.*
import kotlinx.android.synthetic.main.dialog_add_task.*
import java.util.*
import kotlin.collections.ArrayList

class AddRoutineDialog(
    private val cb: (routine:Routine)->Unit,
    private val routine:Routine? = null
    ) : BottomSheetDialogFragment() {


    private val isEditMode = routine != null

    var eventId:String? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_routine,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(activity,"enter start time in 24-hour time system",Toast.LENGTH_LONG).show()


        new_routine_event_relation.setOnClickListener {
            SelectTimeBaseEventDialog { id, name ->
                eventId = id;
                new_routine_event_relation.text = "Relation with event(optional): $name"
            }.show(fragmentManager!!,"")
        }

        if (isEditMode) loadCurrent()


        new_routine_add_btn.setOnClickListener{

            if (!validation()) return@setOnClickListener

            val newRoutine =Routine(
                new_routine_name.text.toString(),
                new_routine_note.text.toString(),
                ArrayList(),//TODO get tags
                getNeededTime(),
                eventId,
                true,
                getRoutineTime(),
                getRoutineDays()
            )
            if (isEditMode) {
                newRoutine.id = routine!!.id
                Routine.getRepo(activity!!).Update(newRoutine)
            } else {
                Routine.getRepo(activity!!).Insert(newRoutine)
            }
            cb(newRoutine)
            dismiss()
        }
    }

    private fun getNeededTime():Long{
        return (new_routine_need_time_min.text.toString().toLong() *60*1000) +
                (new_routine_need_time_hour.text.toString().toLong() *60*60*1000)
    }

    private fun getRoutineTime():Int{
        return (new_routine_start_time_min.text.toString().toInt() *60*1000) +
                (new_routine_start_time_hour.text.toString().toInt() *60*60*1000)
    }

    private fun getRoutineDays():List<Int>{

        val result = ArrayList<Int>()
        if (new_routine_week_0.isChecked) result.add(0)
        if (new_routine_week_1.isChecked) result.add(1)
        if (new_routine_week_2.isChecked) result.add(2)
        if (new_routine_week_3.isChecked) result.add(3)
        if (new_routine_week_4.isChecked) result.add(4)
        if (new_routine_week_5.isChecked) result.add(5)
        if (new_routine_week_6.isChecked) result.add(6)
        return result
    }

    private fun validation():Boolean{

        //TODO check time conflict
        when {
            new_routine_name.text.toString()=="" -> {
                Toast.makeText(activity,"enter name",Toast.LENGTH_SHORT).show()
                return false
            }
            new_routine_need_time_hour.text.toString()=="" -> {
                Toast.makeText(activity,"enter needed time hour",Toast.LENGTH_SHORT).show()
                return false
            }
            new_routine_need_time_min.text.toString()=="" -> {
                Toast.makeText(activity,"enter needed time min",Toast.LENGTH_SHORT).show()
                return false
            }
            new_routine_start_time_hour.text.toString()=="" -> {
                Toast.makeText(activity,"enter start time hour",Toast.LENGTH_SHORT).show()
                return false
            }
            new_routine_start_time_min.text.toString()=="" -> {
                Toast.makeText(activity,"enter start time min",Toast.LENGTH_SHORT).show()
                return false
            }
            getRoutineDays().isEmpty() -> {
                Toast.makeText(activity,"select one day of week minimum",Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return true
    }

    private fun loadCurrent() {

        new_routine_add_btn.text = "Edit"

        routine!!

        //texts
        new_routine_name.setText(routine.name)
        new_routine_note.setText(routine.note)

        //event
        eventId = routine.eventId
        if (eventId!=null){
            val eventName = Event.getRepo(activity!!).getById(eventId)
            new_routine_event_relation.text = "Relation with event(optional): $eventName"
        }

        //dates
        if (routine.routineTime!=0){
            val date = JalaliDateTime((routine.routineTime/1000), TimeZone.getDefault())
            new_routine_start_time_hour.setText(date.hour.toString())
            new_routine_start_time_min.setText(date.min.toString())
        }
        if (routine.neededTimeInMilis!=0L){
            val date = JalaliDateTime((routine.neededTimeInMilis/1000).toInt(), TimeZone.getDefault())
            new_routine_need_time_hour.setText(date.hour.toString())
            new_routine_need_time_min.setText(date.min.toString())
        }

        //day of weeks
        if (routine.routineDays.contains(0)) new_routine_week_0.isChecked = true
        if (routine.routineDays.contains(1)) new_routine_week_1.isChecked = true
        if (routine.routineDays.contains(2)) new_routine_week_2.isChecked = true
        if (routine.routineDays.contains(3)) new_routine_week_3.isChecked = true
        if (routine.routineDays.contains(4)) new_routine_week_4.isChecked = true
        if (routine.routineDays.contains(5)) new_routine_week_5.isChecked = true
        if (routine.routineDays.contains(6)) new_routine_week_6.isChecked = true

    }
}
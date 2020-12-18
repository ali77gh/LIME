package com.ali77gh.lime.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.EventLog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_event_log.*

class AddEventLogDialog(private val event: Event,private val needUiUpdate:()->Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_event_log,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        new_event_log_name.text = "${event.name} (${Event.normalizeType(event.type)})"

        when(event.type){
            Event.EventType.COUNT_BASE->{
                new_event_log_value.visibility = GONE

                new_event_log_add_btn.setOnClickListener {

                    EventLog.getRepo(activity!!).Insert(
                        EventLog.CountBaseEventLog(
                            event.id
                        )
                    )
                    dismiss()
                }
            }
            Event.EventType.TIME_BASE->{
                new_event_log_value.visibility = GONE

                val lastEventLog = EventLog.getRepo(activity!!).getLastLogOfEvent(event.id)
                val isStarted =
                    if (lastEventLog==null) false//first log
                    else lastEventLog.isStart

                new_event_log_add_btn.text=
                    if (isStarted) "stop"
                    else "start"

                new_event_log_add_btn.setOnClickListener {
                    EventLog.getRepo(activity!!).Insert(
                        EventLog.TimeBaseEventLog(
                            event.id,
                            !isStarted
                        )
                    )
                    needUiUpdate()
                    dismiss()
                }
            }
            Event.EventType.VALUE_BASE->{
                new_event_log_add_btn.setOnClickListener {

                    if (new_event_log_value.text.toString()==""){
                        Toast.makeText(activity,"enter value",LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    EventLog.getRepo(activity!!).Insert(
                        EventLog.ValueBaseEventLog(
                            event.id,
                            new_event_log_value.text.toString().toDouble()
                        )
                    )
                    dismiss()
                }
            }
        }
    }
}
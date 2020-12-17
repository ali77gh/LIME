package com.ali77gh.lime.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.ui.activity.ReportActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_event.*

class EditEventDialog(private var event:Event,private var refreshCb:()->Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_event,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        new_event_type_title.visibility = GONE
        new_event_type.visibility = GONE
        new_event_add_btn.text = "Edit"

        new_event_name.setText(event.name)
        new_event_goal.setText(event.goal.toString())
        new_event_note.setText(event.note)

        new_event_add_btn.setOnClickListener {

            //validation
            if (new_event_name.text.toString() == ""){
                Toast.makeText(activity,"enter name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (new_event_goal.text.toString() == ""){
                new_event_goal.setText("-1");
            }

            event.name = new_event_name.text.toString()
            event.goal = new_event_goal.text.toString().toDouble()
            event.note = new_event_note.text.toString()

            Event.getRepo(activity!!).Update(event)

            ReportActivity.event = event

            refreshCb()
            dismiss()
        }
    }
}
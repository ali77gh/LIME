package com.ali77gh.lime.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_event.*

class AddEventDialog(private val cb: ()->Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_event,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        new_event_add_btn.setOnClickListener {

            val type = when(new_event_type.checkedRadioButtonId){
                R.id.new_event_type_tb -> Event.EventType.TIME_BASE;
                R.id.new_event_type_cb -> Event.EventType.COUNT_BASE;
                R.id.new_event_type_vb -> Event.EventType.VALUE_BASE;
                else -> {
                    Toast.makeText(activity,"select type",LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            //validation
            if (new_event_name.text.toString() == ""){
                Toast.makeText(activity,"enter name",LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (new_event_goal.text.toString() == ""){
                new_event_goal.setText("-1");
            }

            Event.getRepo(activity!!).Insert(Event(
                    new_event_name.text.toString()
                    ,type
                    ,new_event_note.text.toString()
                    ,new_event_goal.text.toString().toDouble())
                )
            cb()
            dismiss()
        }
    }
}
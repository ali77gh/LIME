package com.ali77gh.lime.ui.dialog

import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_event_dialog.*

class SelectTimeBaseEventDialog(private val cb:(id:String,name:String)->Unit) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_select_event_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load
        for (i in Event.getRepo(activity!!).getTimeBaseEvents()){

            val item = TextView(activity)
            item.textSize = 18F
            item.gravity = CENTER
            item.setPadding(30,30,30,30)

            item.text = i!!.name

            item.setOnClickListener {
                dismiss()
                cb(i.id,i.name)
            }

            dialog_select_event_parent.addView(item)
        }

    }
}
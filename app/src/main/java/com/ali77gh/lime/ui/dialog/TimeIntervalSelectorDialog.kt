package com.ali77gh.lime.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.ali77gh.lime.R
import kotlinx.android.synthetic.main.dialog_time_interval_selector.*
import com.ali.uneversaldatetools.UDatePickerDialog
import com.ali.uneversaldatetools.date.DateSystem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TimeIntervalSelectorDialog(private var onSelect:(from:Long,to:Long)->Unit) : BottomSheetDialogFragment() {

    private var from :Long?=null
    private var to :Long?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_time_interval_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        time_interval_selector_from.setOnClickListener{
            val datePicker = UDatePickerDialog()//TODO this is jalali (write dialog and get system from setting)
            datePicker.setListener(object :UDatePickerDialog.UDatePickerDialogListener{
                override fun onCancel() {}

                override fun onSelect(p0: Long, p1: DateSystem?) {
                    from = p0*1000
                    time_interval_selector_from.text = "from day: ${p1!!.year}:${p1!!.month}:${p1!!.day}"
                }

            })
            datePicker.show(fragmentManager!!,"")
        }

        time_interval_selector_to.setOnClickListener{
            val datePicker = UDatePickerDialog()//TODO this is jalali (write dialog and get system from setting)
            datePicker.setListener(object :UDatePickerDialog.UDatePickerDialogListener{
                override fun onCancel() {}

                override fun onSelect(p0: Long, p1: DateSystem?) {
                    to = p0*1000
                    time_interval_selector_to.text = "from day: ${p1!!.year}:${p1!!.month}:${p1!!.day}"
                }

            })
            datePicker.show(fragmentManager!!,"")
        }

        time_interval_selector_confirm.setOnClickListener{
            if (from==null || to==null){
                Toast.makeText(activity,"select dates first",LENGTH_SHORT).show()
                return@setOnClickListener
            }
            onSelect(from!!,to!!)
            dismiss()
        }
    }
}
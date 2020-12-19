package com.ali77gh.lime.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.data.model.Routine
import com.ali77gh.lime.ui.adapter.EventListAdapter
import com.ali77gh.lime.ui.adapter.RoutineListAdapter
import com.ali77gh.lime.ui.dialog.AddEventDialog
import com.ali77gh.lime.ui.dialog.AddRoutineDialog
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_routine.*

class RoutineFragment :Fragment(), Backable{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_routine,container,false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routine_fab.setOnClickListener{fabClick()}
        loadList()
    }

    private fun loadList() {

        //TODO filter by tag

        routine_recycler!!.setHasFixedSize(true)
        routine_recycler!!.layoutManager = LinearLayoutManager(activity)
        routine_recycler!!.adapter = RoutineListAdapter(Routine.getRepo(activity!!).all as List<Routine>,activity!!,fragmentManager!!)
    }

    private fun fabClick(){
        AddRoutineDialog(cb = {
            loadList()
        }).show(fragmentManager!!,"")
    }


    override fun onBack(): Boolean {
        return false;
    }
}
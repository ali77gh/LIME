package com.ali77gh.lime.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali77gh.lime.R
import com.ali77gh.lime.data.model.Event
import com.ali77gh.lime.ui.adapter.EventListAdapter
import com.ali77gh.lime.ui.dialog.AddEventDialog
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment :Fragment(), Backable{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_event,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event_fab.setOnClickListener{fabClick()}
        loadList()
    }

    private fun loadList() {
        event_recycler!!.setHasFixedSize(true)
        event_recycler!!.layoutManager = LinearLayoutManager(activity)
        event_recycler!!.adapter = EventListAdapter(Event.getRepo(activity!!).all as List<Event>,activity!!,fragmentManager!!)
    }

    private fun fabClick(){
        AddEventDialog(cb = {
            loadList()
        }).show(fragmentManager!!,"")
    }

    override fun onBack(): Boolean {
        return false;
    }
}
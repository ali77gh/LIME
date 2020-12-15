package com.ali77gh.lime.data.model

import android.content.Context
import com.example.easyrepolib.sqlite.GenericDAO
import com.example.easyrepolib.sqlite.Model

class Event(
    var name: String,
    var type: String,
    var note: String,
    var goal: Double = -1.0,
    var tags: List<String> = ArrayList()
) : Model {

    //name should not be PK because name can edit
    // so we use auto generation id system in orm
    private var id: String? = null
    override fun setId(s: String) { id = s }
    override fun getId(): String { return this.id!! }

    fun haveGoal(): Boolean {
        return goal != -1.0;
    }


    class EventRepo(context: Context?) :
        GenericDAO<Event?>(context, Event::class.java, "event", true) {

        //custom queries here

        fun customQuerySample(): List<Event?> {
            return getWithCondition { p0 -> (p0 as Event).name == "felan"; }
        }

        fun getByTag(tag: String): List<Event?> {
            return getWithCondition { p0 -> (p0 as Event).tags.indexOf(tag) != -1; }
        }
    }

    // repo singleton
    companion object {
        private var repo: EventRepo? = null
        fun getRepo(context: Context): EventRepo {
            if (repo == null) repo = EventRepo(context)
            return repo!!
        }
    }

    object EventType {
        val COUNT_BASE = "cb"
        val TIME_BASE = "tb"
        val VALUE_BASE = "vb"
    }
}
package com.ali77gh.lime.data.model

import android.content.Context
import com.ali77gh.lime.data.model.Event.EventType.COUNT_BASE
import com.ali77gh.lime.data.model.Event.EventType.TIME_BASE
import com.ali77gh.lime.data.model.Event.EventType.VALUE_BASE
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

        fun getByTag(tag: String): List<Event?> {
            return getWithCondition { p0 -> (p0 as Event).tags.indexOf(tag) != -1; }
        }

        fun getTimeBaseEvents(): List<Event?> {
            return getWithCondition { p0 -> (p0 as Event).type==TIME_BASE; }
        }
    }

    // repo singleton
    companion object {
        private var repo: EventRepo? = null
        fun getRepo(context: Context): EventRepo {
            if (repo == null) repo = EventRepo(context)
            return repo!!
        }

        fun normalizeType(value:String):String{
            return when(value){
                COUNT_BASE->"count base"
                TIME_BASE->"time base"
                VALUE_BASE->"value base"
                else->"un know"
            }
        }
    }

    object EventType {
        val COUNT_BASE = "cb"
        val TIME_BASE = "tb"
        val VALUE_BASE = "vb"
    }
}
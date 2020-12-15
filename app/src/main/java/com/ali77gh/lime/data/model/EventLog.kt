package com.ali77gh.lime.data.model

import android.content.Context
import com.example.easyrepolib.sqlite.GenericDAO
import com.example.easyrepolib.sqlite.Model

open class EventLog(
    var eventId: String,
    var eventType: String,
    protected var value: Double,
    var time: Long = System.currentTimeMillis()
) : Model {

    //name is primary key ;)
    private var id: String? = null;
    override fun setId(s: String) { id = s }
    override fun getId(): String { return this.id!! }

    class EventLogRepo(context: Context?) :
        GenericDAO<EventLog?>(context, EventLog::class.java, "eventlog", true) {

        //custom queries here
        fun customQuerySample(): List<EventLog?> {
            return getWithCondition { p0 -> (p0 as TimeBaseEventLog).isEnd; }
        }
    }

    class CountBaseEventLog(
        eventId: String,
        time: Long = System.currentTimeMillis()
    ) : EventLog(
        eventId,
        Event.EventType.TIME_BASE,
        -1.0,
        time
    ) {
        //nothing to do :)
    }

    class TimeBaseEventLog(
        eventId: String,
        isStart: Boolean,
        time: Long = System.currentTimeMillis()
    ) : EventLog(
        eventId,
        Event.EventType.TIME_BASE,
        if (isStart) 1.0 else 0.0,
        time
    ) {

        val isStart = value == 1.0;
        val isEnd = value == 0.0;
    }

    class ValueBaseEventLog(
        eventId: String,
        value: Double,
        time: Long = System.currentTimeMillis()
    ) : EventLog(
        eventId,
        Event.EventType.TIME_BASE,
        value,
        time
    ) {

        val eventValue: Double get() = value;
    }

    // repo singleton
    companion object {
        private var repo: EventLogRepo? = null
        fun getRepo(context: Context): EventLogRepo {
            if (repo == null) repo = EventLogRepo(context)
            return repo!!
        }
    }

}
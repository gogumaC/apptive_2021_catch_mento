package com.example.catch_mentor.mentoringRecord

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class RecordCalendarDecorator:DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5f, Color.parseColor("#0000FF")))
        view?.addSpan(DotSpan(10f,Color.parseColor("#66474a")))
    }
}
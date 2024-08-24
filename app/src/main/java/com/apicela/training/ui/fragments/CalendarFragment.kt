package com.apicela.training.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.apicela.training.R
import com.apicela.training.services.ExecutionService
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Date

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private lateinit var calendarView: CalendarView
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val executionService: ExecutionService = ExecutionService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        linkViewFields(view)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarTodayInfo()
        calendarOnClickGetInfo()
    }

    private fun calendarOnClickGetInfo() {
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            adapter.clear()
            val clickedDate = Calendar.getInstance()
            clickedDate.set(year, month, dayOfMonth)
            val date: Date = (clickedDate.time)
            val listOfExecutions =
                runBlocking { executionService.groupExercisesExecutionByDateIntoString(date) }
            adapter.addAll(listOfExecutions)
            adapter.notifyDataSetChanged()
        }
    }

    private fun calendarTodayInfo() {
        val today = Date()
        val listOfExecutions =
            runBlocking { executionService.groupExercisesExecutionByDateIntoString(today) }
        adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item, listOfExecutions)
        listView.adapter = adapter
    }

    private fun linkViewFields(v: View) {
        calendarView = v.findViewById(R.id.calendarView)
        listView = v.findViewById(R.id.listView)
    }


}

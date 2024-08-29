package com.apicela.training.ui.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.apicela.training.Apicela
import com.apicela.training.R


class TimerImpl(private val context: Context, private val container: ViewGroup) {

    private val timerLayout: View =
        LayoutInflater.from(context).inflate(R.layout.timer_layout, container, true)
    private val textViewTimer: TextView = timerLayout.findViewById(R.id.textViewTimer)
    private val timerButton: ImageButton = timerLayout.findViewById(R.id.timerButton)
    private val timer: Timer = Timer.getInstance(context)

    init {
        if(!timer.counting) textViewTimer.text = Apicela.REST_TIMING
        toggleIcon()
        timer.onTickCallback = { seconds ->
            textViewTimer.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
        }

        timer.onCountingChangedCallback = { isCounting ->
            toggleIcon()
            textViewTimer.text = Apicela.REST_TIMING
        }

        timerButton.setOnClickListener {
            toggle()
        }
    }

    fun refreshTimeValue(){
        if(!timer.counting) textViewTimer.text = Apicela.REST_TIMING
    }

    fun toggle() {
        if (timer.counting) timer.stopTimer()
        else timer.startTimer()
    }

    private fun toggleIcon() {
        if (timer.counting) timerButton.setImageResource(R.drawable.icon_stop)
        else timerButton.setImageResource(R.drawable.icon_play)
    }
}

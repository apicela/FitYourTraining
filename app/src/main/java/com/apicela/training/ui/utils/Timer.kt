package com.apicela.training.ui.utils

import android.content.Context
import android.os.CountDownTimer
import com.apicela.training.Apicela
import com.apicela.training.R


class Timer(private val context: Context) {

    var secondsLeft: Long = 0
        private set(value) {
            field = value
            onTickCallback?.invoke(value)
        }

    private var countDownTimer: CountDownTimer? = null
    public var counting: Boolean = false
        set(value) {
            field = value
            onCountingChangedCallback?.invoke(value) // Chama o callback sempre que `counting` mudar
        }
    private val soundManager: SoundManager = SoundManager(context, R.raw.beep)

    var onTickCallback: ((Long) -> Unit)? = null
    var onCountingChangedCallback: ((Boolean) -> Unit)? = null

     fun startTimer() {
        if (!isValidTimeFormat(Apicela.REST_TIMING)) return
        val milliseconds: Long = getMillisecondsFromTimeString(Apicela.REST_TIMING)
        counting = true
        countDownTimer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsLeft = millisUntilFinished / 1000
            }

            override fun onFinish() {
                beepSound()
                counting = false
            }
        }.start()
    }

     fun stopTimer() {
        countDownTimer?.cancel()
        counting = false
    }

    private fun beepSound() {
        soundManager.playSound()
    }

    private fun getMillisecondsFromTimeString(clockTime: String): Long {
        val (minutes, seconds) = clockTime.split(":").map { it.toInt() }
        return minutes * 60 * 1000L + seconds * 1000L
    }

    fun isValidTimeFormat(input: String): Boolean {
        val regex = Regex("^\\d{2}:\\d{2}$")
        return regex.matches(input)
    }

    companion object {
        @Volatile
        private var instance: Timer? = null

        fun getInstance(context: Context): Timer {
            return instance ?: synchronized(this) {
                instance ?: Timer(context.applicationContext).also { instance = it }
            }
        }
    }
}

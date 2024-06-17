package com.isa.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var tvTime: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnReset: Button

    private var handler: Handler = Handler()
    private var startTime: Long = 0L
    private var timeInMilliseconds: Long = 0L
    private var timeSwapBuff: Long = 0L
    private var updatedTime: Long = 0L

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds

            val secs = (updatedTime / 1000).toInt()
            val mins = secs / 60
            val hrs = mins / 60
            val milliseconds = (updatedTime % 1000).toInt()

            tvTime.text = String.format("%02d:%02d:%02d:%03d", hrs, mins % 60, secs % 60, milliseconds)
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTime = findViewById(R.id.tv_time)
        btnStart = findViewById(R.id.btn_start)
        btnStop = findViewById(R.id.btn_stop)
        btnReset = findViewById(R.id.btn_reset)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        btnStart.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimerThread, 0)
        }

        btnStop.setOnClickListener {
            timeSwapBuff += timeInMilliseconds
            handler.removeCallbacks(updateTimerThread)
        }

        btnReset.setOnClickListener {
            startTime = 0L
            timeInMilliseconds = 0L
            timeSwapBuff = 0L
            updatedTime = 0L
            tvTime.text = "00:00:00:000"
        }
    }
}

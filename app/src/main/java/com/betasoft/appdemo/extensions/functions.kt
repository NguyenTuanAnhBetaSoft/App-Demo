package com.betasoft.appdemo.extensions

import com.betasoft.appdemo.utils.AppConfig
import java.util.*
import java.util.concurrent.TimeUnit

fun Long.getDuration() = if (TimeUnit.MILLISECONDS.toMinutes(this) < 60) {
    String.format(
        AppConfig.FORMAT_DURATION_UNDER_HOUR,
        millisecondToMinute(),
        millisecondToSecond() - millisecondToMinute() * 60
    )
} else {
    String.format(
        AppConfig.FORMAT_DURATION_UPPER_HOUR,
        millisecondToHour(),
        millisecondToMinute() - millisecondToHour() * 60,
        millisecondToSecond() - millisecondToMinute() * 60
    )
}

fun Long.millisecondToHour() = TimeUnit.MILLISECONDS.toHours(this)
fun Long.millisecondToMinute() = TimeUnit.MILLISECONDS.toMinutes(this)
fun Long.millisecondToSecond() = TimeUnit.MILLISECONDS.toSeconds(this)

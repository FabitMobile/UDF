package ru.fabit.viewcontroller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

fun awaitDebug(timeMillis: Long = 10_000) {
    runBlocking {
        awaitAll(CoroutineScope(Dispatchers.IO).async {
            Thread.sleep(timeMillis)
        })
    }
}

fun interval(timeInMillis: Long, timeUnit: TimeUnit): Flow<Long> = flow {

    var counter: Long = 0

    val delayTime = when (timeUnit) {
        TimeUnit.MICROSECONDS -> timeInMillis / 1000
        TimeUnit.NANOSECONDS -> timeInMillis / 1_000_000
        TimeUnit.SECONDS -> timeInMillis * 1000
        TimeUnit.MINUTES -> 60 * timeInMillis * 1000
        TimeUnit.HOURS -> 60 * 60 * timeInMillis * 1000
        TimeUnit.DAYS -> 24 * 60 * 60 * timeInMillis * 1000
        else -> timeInMillis
    }

    while (true) {
        delay(delayTime)
        emit(counter++)
    }
}
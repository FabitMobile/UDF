package ru.fabit.viewcontroller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration

fun awaitDebug(timeMillis: Long = 10_000) {
    runBlocking {
        awaitAll(CoroutineScope(Dispatchers.IO).async {
            Thread.sleep(timeMillis)
        })
    }
}

fun interval(duration: Duration): Flow<Long> = flow {

    var counter: Long = 0

    while (true) {
        delay(duration)
        emit(counter++)
    }
}
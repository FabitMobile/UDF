package ru.fabit.udf.store.internal

internal inline fun log(message: Any?) {
    if (LOG_ENABLED)
        println("UDF.Store.Coroutines: $message")
}

private const val LOG_ENABLED = false
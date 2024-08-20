package ru.fabit.udf.store.coroutines.internal

internal fun log(message: Any?) {
    if (LOG_ENABLED)
        println("UDF.Store.Coroutines: $message")
}

private const val LOG_ENABLED = true
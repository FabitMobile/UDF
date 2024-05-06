package ru.fabit.udf.store.internal

internal fun log(message: Any?) {
    if (LOG_ENABLED)
        println("UDF.Store.RxJava: $message")
}

private const val LOG_ENABLED = false
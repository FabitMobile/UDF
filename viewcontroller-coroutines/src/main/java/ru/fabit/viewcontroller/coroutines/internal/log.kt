package ru.fabit.viewcontroller.coroutines.internal

internal fun log(message: Any?) {
    if (LOG_ENABLED)
        println("UDF.ViewController.View.Coroutines: $message")
}

private const val LOG_ENABLED = false
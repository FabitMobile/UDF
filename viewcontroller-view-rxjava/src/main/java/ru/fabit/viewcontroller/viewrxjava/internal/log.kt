package ru.fabit.viewcontroller.viewrxjava.internal

internal fun log(message: Any?) {
    if (LOG_ENABLED)
        println("UDF.ViewController.View.RxJava: $message")
}

private const val LOG_ENABLED = false
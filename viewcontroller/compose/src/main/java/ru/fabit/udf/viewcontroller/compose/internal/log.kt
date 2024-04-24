package ru.fabit.udf.viewcontroller.compose.internal

internal inline fun log(message: Any?) {
    if (LOG_ENABLED)
        println("UDF.ViewController.Compose: $message")
}

private const val LOG_ENABLED = false
package ru.fabit.udf.store.coroutines

fun interface ErrorHandler {
    fun handle(t: Throwable)
}
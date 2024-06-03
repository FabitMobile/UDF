package ru.fabit.udf.store.coroutines

interface ErrorHandler {
    fun handle(t: Throwable)
}
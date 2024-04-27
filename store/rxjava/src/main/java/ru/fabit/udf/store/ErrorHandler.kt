package ru.fabit.udf.store

fun interface ErrorHandler {
    fun handle(t: Throwable)
}
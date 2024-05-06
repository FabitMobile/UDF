package ru.fabit.udf.store

interface ErrorHandler {
    fun handle(t: Throwable)
}
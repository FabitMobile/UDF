package ru.fabit.udf.store

interface ErrorHandler {
    fun handleError(t: Throwable)
}
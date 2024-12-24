package ru.fabit.udf.example.data

import ru.fabit.udf.store.coroutines.ErrorHandler
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor() : ErrorHandler {
    override fun handle(t: Throwable) {
        t.printStackTrace()
    }
}
package ru.fabit.udf.viewcontroller.compose.test

import androidx.compose.runtime.Immutable

@Immutable
data class TestState(
    val value: String,
    val value2: String,
)
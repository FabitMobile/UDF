package ru.fabit.udf.store

inline fun <reified Class> actionIs(): (Any, Any) -> Boolean = { _, a -> a is Class }
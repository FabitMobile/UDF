package ru.fabit.udf.store.coroutines

inline fun <reified Class> actionIs(): (Any, Any) -> Boolean = { _, a -> a is Class }
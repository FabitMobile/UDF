package ru.fabit.udf.store

import io.reactivex.disposables.Disposable

class SourceDisposable(
    private val nameStore: String
) {
    private var resources = hashMapOf<String, Disposable>()

    fun add(key: String, disposable: Disposable) {
        synchronized(this) {
            val map = resources
            if (map.containsKey(key))
                dispose(key)
            map[key] = disposable
        }
    }

    private fun dispose(key: String) {
        val map = resources
        map[key]?.let { disposable ->
            if (!disposable.isDisposed) {
                disposable.dispose()
                map.remove(key)
            }
        }
    }

    fun dispose() {
        synchronized(this) {
            try {
                val map = resources
                for (disposable in map) {
                    if (!disposable.value.isDisposed) {
                        disposable.value.dispose()
                    }
                }
                map.clear()
            } catch (ex: Exception) {
                throw Exception(nameStore, ex)
            }
        }
    }
}
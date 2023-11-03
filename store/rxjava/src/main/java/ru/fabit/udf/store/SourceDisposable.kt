package ru.fabit.udf.store

import io.reactivex.disposables.Disposable

class SourceDisposable {
    private val map = hashMapOf<String, Disposable>()

    fun add(key: String, disposable: Disposable) {
        if (map.containsKey(key))
            dispose(key)
        map[key] = disposable
    }

    fun dispose(key: String) {
        map[key]?.let { disposable ->
            if (!disposable.isDisposed) {
                disposable.dispose()
                map.remove(key)
            }
        }
    }

    fun dispose() {
        for (disposable in map){
            if (!disposable.value.isDisposed){
                disposable.value.dispose()
            }
        }
        map.clear()
    }
}
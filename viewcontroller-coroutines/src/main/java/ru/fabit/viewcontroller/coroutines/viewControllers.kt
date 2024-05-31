package ru.fabit.viewcontroller.coroutines

import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

inline fun <reified T : ViewController<*, *, *>> Fragment.viewControllers() = viewModels<T>()

inline fun <reified T : ViewController<*, *, *>> ComponentActivity.viewControllers() =
    viewModels<T>()

inline fun <reified T : ViewController<*, *, *>> View.viewControllers(): Lazy<T> =
    LazyViewController(this, T::class)

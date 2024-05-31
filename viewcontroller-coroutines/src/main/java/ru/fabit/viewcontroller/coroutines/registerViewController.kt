package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.registerViewController(viewController: ViewController<*, *, *>) {
    lifecycle.addObserver(viewController)
}
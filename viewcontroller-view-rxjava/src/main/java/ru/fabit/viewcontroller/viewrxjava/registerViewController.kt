package ru.fabit.viewcontroller.viewrxjava

import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.registerViewController(viewController: ViewController<*, *>) {
    lifecycle.addObserver(viewController)
}
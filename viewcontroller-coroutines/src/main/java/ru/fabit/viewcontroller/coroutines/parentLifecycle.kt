package ru.fabit.viewcontroller.coroutines

import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner

fun View.parentLifecycle() = findViewTreeLifecycleOwner()
package ru.fabit.viewcontroller.coroutines

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import kotlin.reflect.KClass

class LazyViewController<T : ViewModel>(
    private val view: View,
    private val viewControllerClass: KClass<T>
) : Lazy<T> {
    private var cached: T? = null

    override val value: T
        get() {
            val controller = cached
            return controller ?: ViewModelProvider(
                ViewTreeViewModelStoreOwner.get(view)!!
            )[viewControllerClass.java].also {
                cached = it
            }
        }

    override fun isInitialized() = cached != null
}

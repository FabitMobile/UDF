package ru.fabit.viewcontroller.viewrxjava

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.fabit.udf.store.Store
import ru.fabit.viewcontroller.viewrxjava.internal.log
import java.util.concurrent.atomic.AtomicReference

abstract class ViewController<State : Any, Action : Any, View : StateView<State>>(
    private val store: Store<State, Action>,
    protected val statePayload: StatePayload<State>? = null
) : LifecycleEventObserver {

    init {
        store.start()
    }

    protected open fun attach() {}

    protected open fun firstViewAttach() {}

    private var createdView: LifecycleOwner? = null
    protected var resumedView: View? = null
    protected var isAttached = false
    protected var isFirstAttach = false

    private val sharedState: AtomicReference<State> = AtomicReference()

    private var stateObserver: DisposableObserver<State>? = null

    protected fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        log("onStateChanged source=$source event=$event")
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate(source)
            Lifecycle.Event.ON_RESUME -> onResume(source)
            Lifecycle.Event.ON_PAUSE -> onPause(source)
            Lifecycle.Event.ON_DESTROY -> onDestroy(source)

            else -> {}
        }
    }

    protected open fun onCreate(lifecycleOwner: LifecycleOwner) {
        createdView = lifecycleOwner
        sharedState.set(null)
    }

    protected open fun onDestroy(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner is Fragment) {
            lifecycleOwner.activity?.let { activity ->
                if (activity.isFinishing) {
                    destroy(lifecycleOwner)
                } else {
                    var anyParentIsRemoving = false
                    var parent = lifecycleOwner.parentFragment
                    while (!anyParentIsRemoving && parent != null) {
                        anyParentIsRemoving = parent.isRemoving
                        parent = parent.parentFragment
                    }
                    if (lifecycleOwner.isRemoving || anyParentIsRemoving) {
                        destroy(lifecycleOwner)
                    }
                }
            }
        }
        if (lifecycleOwner is Activity) {
            if (lifecycleOwner.isFinishing) {
                destroy(lifecycleOwner)
            }
        }
    }

    protected open fun onResume(lifecycleOwner: LifecycleOwner) {
        resumedView = lifecycleOwner as View
        isAttached = true
        stateObserver = object : DisposableObserver<State>() {

            override fun onComplete() {
            }

            override fun onNext(state: State) {
                val prevState = sharedState.get()
                if (isAttached && (prevState != state)) {
                    sharedState.set(state)
                    if (statePayload == null) {
                        resumedView?.renderState(state, null)
                    } else {
                        val payload = statePayload.payload(prevState, state)
                        resumedView?.renderState(state, payload)
                    }
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }

        store.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stateObserver!!)

        attach()
        if (!isFirstAttach) {
            isFirstAttach = true
            firstViewAttach()
        }
    }

    protected open fun onPause(lifecycleOwner: LifecycleOwner) {
        isAttached = false
        stateObserver?.dispose()
    }

    private fun destroy(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner == this.createdView) {
            store.dispose()
        }
    }
}
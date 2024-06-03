package ru.fabit.viewcontroller.viewrxjava

import androidx.lifecycle.LifecycleOwner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.fabit.udf.store.EventsStore
import ru.fabit.udf.store.StateWithEvents
import java.util.concurrent.atomic.AtomicReference

abstract class EventsViewController<State : Any, Action : Any, Event : Any>(
    private val store: EventsStore<State, Action, Event>,
    statePayload: StatePayload<State>? = null
) : ViewController<State, Action>(store, statePayload) {

    private val sharedState: AtomicReference<State> = AtomicReference()

    private var stateWithEventsObserver: DisposableObserver<StateWithEvents<State, Event>>? = null

    override fun onResume(lifecycleOwner: LifecycleOwner) {
        val resumedView = lifecycleOwner as? EventsView<State, Event>
        this.resumedView = resumedView
        isAttached = true

        stateWithEventsObserver = object : DisposableObserver<StateWithEvents<State, Event>>() {
            override fun onComplete() {
            }

            override fun onNext(stateWithEvents: StateWithEvents<State, Event>) {
                val prevState = sharedState.get()
                if (isAttached && (prevState != stateWithEvents.state)) {
                    sharedState.set(stateWithEvents.state)
                    if (statePayload == null) {
                        resumedView?.renderState(
                            stateWithEvents.state,
                            stateWithEvents.events,
                            null
                        )
                    } else {
                        val payload = statePayload.payload(prevState, stateWithEvents.state)
                        resumedView?.renderState(
                            stateWithEvents.state,
                            stateWithEvents.events,
                            payload
                        )
                    }
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }

        store.stateWithEvents
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stateWithEventsObserver!!)

        attach()
        if (!isFirstAttach) {
            isFirstAttach = true
            firstViewAttach()
        }
    }

    override fun onPause(lifecycleOwner: LifecycleOwner) {
        super.onPause(lifecycleOwner)
        stateWithEventsObserver?.dispose()
    }
}
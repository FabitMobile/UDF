package ru.fabit.udf.example.presentation.viewcontroller

import ru.fabit.udf.example.presentation.store.state.MainChange
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.viewcontroller.core.StatePayload

/**
 * StatePayload реализован только для AndroidView.
 */
object MainPayload : StatePayload<MainState> {
    override fun payload(prevState: MainState?, newState: MainState): List<MainChange> {
        val changes = mutableListOf<MainChange>()
        if (prevState?.counter == null)
            changes.add(MainChange.Text("Loading"))
        else
            changes.add(MainChange.Text("Ready"))
        if (prevState?.counter == 3 && prevState.featureEnabled != newState.featureEnabled)
            changes.add(MainChange.Text("counter == 3 && featureEnabled = ${newState.featureEnabled}"))
        return changes
    }
}
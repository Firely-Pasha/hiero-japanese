package space.compoze.hiero.ui.shared.utils

inline fun <State, reified ScopedState : State> State.with(block: (ScopedState) -> Unit) {
    if (this is ScopedState) {
        block(this)
    }
}

inline fun <State, reified ScopedState : State> State.applyState(block: (ScopedState) -> ScopedState): State {
    if (this is ScopedState) {
        return block(this)
    }
    return this
}
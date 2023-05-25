package space.compoze.hiero.ui.shared.utils

inline fun <State, reified ScopedState : State> State.with(block: (ScopedState) -> Unit) {
    if (this is ScopedState) {
        block(this)
    }
}

inline fun <State, reified SomeState : State> State.apply(block: SomeState.() -> Unit) {
    if (this is SomeState) {
        block(this)
    }
}
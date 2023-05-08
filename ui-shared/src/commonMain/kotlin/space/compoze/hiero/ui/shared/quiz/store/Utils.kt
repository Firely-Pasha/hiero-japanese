package space.compoze.hiero.ui.shared.quiz.store

inline fun <reified T : QuizState>QuizState.withState(block: T.() -> Unit) {
    if (this is T) {
        block(this)
    }
}

inline fun <reified T : QuizState>QuizState.applyState(block: T.() -> QuizState): QuizState {
    if (this is T) {
        return block(this)
    }
    return this
}
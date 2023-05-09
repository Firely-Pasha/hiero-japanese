package space.compoze.hiero.ui.shared.quiz.store

sealed interface QuizIntent {
    object NextItem : QuizIntent
    object BookmarkCurrentItem : QuizIntent
}

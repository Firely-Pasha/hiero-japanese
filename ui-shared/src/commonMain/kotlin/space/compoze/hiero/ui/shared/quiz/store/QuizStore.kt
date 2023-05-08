package space.compoze.hiero.ui.shared.quiz.store

import com.arkivanov.mvikotlin.core.store.Store

interface QuizStore : Store<QuizIntent, QuizState, Nothing>
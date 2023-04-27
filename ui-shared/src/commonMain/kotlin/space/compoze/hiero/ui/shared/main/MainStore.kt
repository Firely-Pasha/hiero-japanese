package space.compoze.hiero.ui.shared.main

import com.arkivanov.mvikotlin.core.store.Store


data class MainState(
    val tab: Int,
)

sealed interface MainIntent {
    data class ChangeTab(val index: Int) : MainIntent
}

sealed interface MainMessage {
    data class Tab(val value: Int) : MainMessage
}

sealed interface MainLabel {
    data class ChangeTab(val value: Int) : MainLabel
}

interface MainStore : Store<MainIntent, MainState, MainLabel>
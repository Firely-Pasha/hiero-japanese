package space.compoze.hiero.ui.shared.main.store

import com.arkivanov.mvikotlin.core.store.Store

interface MainStore : Store<MainStore.Intent, MainStore.State, MainStore.Label> {

    data class State(
        val tab: Int,
    )

    sealed interface Intent {
        data class ChangeTab(val index: Int) : Intent
    }

    sealed interface Message {
        data class Tab(val value: Int) : Message
    }

    sealed interface Label {
        data class ChangeTab(val value: Int) : Label
    }

}
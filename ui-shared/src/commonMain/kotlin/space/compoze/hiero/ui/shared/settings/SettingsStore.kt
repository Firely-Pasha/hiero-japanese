package space.compoze.hiero.ui.shared.settings

import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError

interface SettingsStore: Store<SettingsStore.Intent, SettingsStore.State, Nothing> {

    sealed interface State {

        object Loading : State

        data class Content(
            val theme: String,
        ) : State

        data class Error(
            val error: DomainError,
        ) : State

    }

    sealed interface Action {

        data class Loaded(
            val theme: String,
        ): Action

    }

    sealed interface Intent {

        data class ChangeTheme(
            val theme: String,
        ): Intent

    }

    sealed interface Message {

        data class Init(
            val theme: String,
        ): Message

        data class UpdateTheme(
            val theme: String,
        ): Message

    }

}
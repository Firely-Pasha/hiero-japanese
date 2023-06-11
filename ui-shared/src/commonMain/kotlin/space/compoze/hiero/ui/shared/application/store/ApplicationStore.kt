package space.compoze.hiero.ui.shared.application.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import space.compoze.hiero.domain.base.exceptions.DomainError

interface ApplicationStore: Store<ApplicationStore.Intent, ApplicationStore.State, Nothing> {

    sealed interface State {

        @Immutable
        object Loading : State

        @Immutable
        data class Content(
            val theme: String,
        ) : State

        @Immutable
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

    }

}
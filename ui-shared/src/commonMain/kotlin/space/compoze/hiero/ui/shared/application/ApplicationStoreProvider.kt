package space.compoze.hiero.ui.shared.application

import arrow.core.raise.either
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.settings.interactor.SettingsGetTheme

class ApplicationStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val settingsGetTheme: SettingsGetTheme by inject()

    fun create(scope: CoroutineScope): ApplicationStore =
        object : ApplicationStore,
            Store<ApplicationStore.Intent, ApplicationStore.State, Nothing>
            by storeFactory.create<ApplicationStore.Intent, ApplicationStore.Action, ApplicationStore.Message, ApplicationStore.State, Nothing>(
                name = "ApplicationStore",
                initialState = ApplicationStore.State.Loading,
                bootstrapper = coroutineBootstrapper {
                    either {
                        val theme = settingsGetTheme().bind()
                        dispatch(
                            ApplicationStore.Action.Loaded(
                                theme = theme
                            )
                        )
                    }.onLeft {
                        it.printStackTrace()
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<ApplicationStore.Action.Loaded> {
                        dispatch(
                            ApplicationStore.Message.Init(
                                theme = it.theme
                            )
                        )
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is ApplicationStore.Message.Init -> ApplicationStore.State.Content(
                            theme = msg.theme
                        )
                    }
                }
            ) {}

}
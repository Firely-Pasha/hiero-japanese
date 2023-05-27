package space.compoze.hiero.ui.shared.application.store

import arrow.core.raise.either
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.domain.settings.interactor.SettingsGetTheme

class ApplicationStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val dispatchers: AppDispatchers by inject()

    private val settingsGetTheme: SettingsGetTheme by inject()

    fun create(): ApplicationStore =
        object : ApplicationStore,
            Store<ApplicationStore.Intent, ApplicationStore.State, Nothing>
            by storeFactory.create<ApplicationStore.Intent, ApplicationStore.Action, ApplicationStore.Message, ApplicationStore.State, Nothing>(
                name = "ApplicationStore",
                initialState = ApplicationStore.State.Loading,
                bootstrapper = coroutineBootstrapper(dispatchers.main) {
                    launch {
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
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<ApplicationStore.Action.Loaded> {
                        dispatch(
                            ApplicationStore.Message.Init(
                                theme = it.theme
                            )
                        )
                        settingsGetTheme.asFlow().onRight {
                            launch {
                                it.collect {
                                    withContext(dispatchers.main) {
                                        dispatch(
                                            ApplicationStore.Message.Init(
                                                theme = it
                                            )
                                        )
                                    }
                                }
                            }
                        }
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
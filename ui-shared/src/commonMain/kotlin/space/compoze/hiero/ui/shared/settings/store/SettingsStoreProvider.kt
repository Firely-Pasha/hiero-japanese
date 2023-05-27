package space.compoze.hiero.ui.shared.settings.store

import arrow.core.raise.either
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.domain.settings.interactor.SettingsGetTheme
import space.compoze.hiero.domain.settings.interactor.SettingsSetTheme
import space.compoze.hiero.ui.shared.utils.applyState

class SettingsStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val dispatchers: AppDispatchers by inject()

    private val settingsGetTheme: SettingsGetTheme by inject()
    private val settingsSetTheme: SettingsSetTheme by inject()

    fun create(): SettingsStore =
        object : SettingsStore,
            Store<SettingsStore.Intent, SettingsStore.State, Nothing>
            by storeFactory.create<SettingsStore.Intent, SettingsStore.Action, SettingsStore.Message, SettingsStore.State, Nothing>(
                name = "SettingsStore",
                initialState = SettingsStore.State.Loading,
                bootstrapper = coroutineBootstrapper(dispatchers.main) {
                    launch {
                        either {
                            val theme = settingsGetTheme().bind()
                            dispatch(
                                SettingsStore.Action.Loaded(
                                    theme = theme
                                )
                            )
                        }.onLeft {
                            it.printStackTrace()
                        }
                    }
                },
                executorFactory = coroutineExecutorFactory(dispatchers.main) {
                    onAction<SettingsStore.Action.Loaded> {
                        dispatch(
                            SettingsStore.Message.Init(
                                theme = it.theme
                            )
                        )
                    }
                    onIntent<SettingsStore.Intent.ChangeTheme> {
                        launch {
                            either {
                                val theme = it.theme
                                settingsSetTheme(theme).bind()
                                dispatch(
                                    SettingsStore.Message.UpdateTheme(
                                        theme = theme
                                    )
                                )
                            }.onLeft {
                                it.printStackTrace()
                            }
                        }
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is SettingsStore.Message.Init -> SettingsStore.State.Content(
                            theme = msg.theme
                        )

                        is SettingsStore.Message.UpdateTheme -> applyState { content: SettingsStore.State.Content ->
                            content.copy(
                                theme = msg.theme
                            )
                        }
                    }
                }
            ) {}

}
package space.compoze.hiero.ui.shared.main

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

@OptIn(ExperimentalMviKotlinApi::class)
class MainStoreProvider(private val storeFactory: StoreFactory) {
    fun create(): MainStore = object : MainStore,
        Store<MainIntent, MainState, MainLabel> by storeFactory.create<MainIntent, Nothing, MainMessage, MainState, MainLabel>(
            name = "KEK",
            initialState = MainState(tab = 0),
            executorFactory = coroutineExecutorFactory {
                onIntent<MainIntent.ChangeTab> {
                    dispatch(MainMessage.Tab(it.index))
                    publish(MainLabel.ChangeTab(it.index))
                }
            },
            reducer = { msg ->
                when (msg) {
                    is MainMessage.Tab -> copy(
                        tab = msg.value
                    )
                }
            }
        ) {}
}
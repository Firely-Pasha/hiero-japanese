package space.compoze.hiero.ui.shared.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.AppDispatchers

@OptIn(ExperimentalMviKotlinApi::class)
class MainStoreProvider(
    private val storeFactory: StoreFactory
) : KoinComponent {

    private val dispatchers: AppDispatchers by inject()

    fun create(): MainStore = object : MainStore,
        Store<MainStore.Intent, MainStore.State, MainStore.Label> by storeFactory.create<MainStore.Intent, Nothing, MainStore.Message, MainStore.State, MainStore.Label>(
            name = "MainStore",
            initialState = MainStore.State(tab = 0),
            executorFactory = coroutineExecutorFactory(dispatchers.main) {
                onIntent<MainStore.Intent.ChangeTab> {
                    dispatch(MainStore.Message.Tab(it.index))
                    publish(MainStore.Label.ChangeTab(it.index))
                }
            },
            reducer = { msg ->
                when (msg) {
                    is MainStore.Message.Tab -> copy(
                        tab = msg.value
                    )
                }
            }
        ) {}
}
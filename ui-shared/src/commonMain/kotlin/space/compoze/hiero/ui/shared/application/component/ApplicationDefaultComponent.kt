package space.compoze.hiero.ui.shared.application.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.compoze.hiero.ui.shared.application.store.ApplicationStore
import space.compoze.hiero.ui.shared.application.store.ApplicationStoreProvider
import space.compoze.hiero.ui.shared.utils.inheritScope

class ApplicationDefaultComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
) : ApplicationComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        ApplicationStoreProvider(storeFactory).create()
    }

    override val state = MutableValue(store.state)

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, Dispatchers.Unconfined) {
            store.states bindTo ::onStateChange
        }
    }

    private fun onStateChange(newState: ApplicationStore.State) {
        state.value = newState
    }

}
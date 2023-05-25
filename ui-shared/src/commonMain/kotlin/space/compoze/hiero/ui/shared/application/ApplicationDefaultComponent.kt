package space.compoze.hiero.ui.shared.application

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.launch
import space.compoze.hiero.ui.shared.utils.inheritScope

class ApplicationDefaultComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
) : ApplicationComponent, ComponentContext by componentContext {

    private val componentScope = inheritScope()

    private val store = instanceKeeper.getStore {
        ApplicationStoreProvider(storeFactory).create(componentScope)
    }

    override val state = MutableValue(store.state)

    init {
        componentScope.launch {
            store.states.collect { state.value = it }
        }
    }

}
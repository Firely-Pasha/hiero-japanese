package space.compoze.hiero.ui.shared.application.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.mp.KoinPlatformTools
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.ui.shared.application.store.ApplicationStore
import space.compoze.hiero.ui.shared.application.store.ApplicationStoreProvider
import space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope

class ApplicationDefaultComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
) : ApplicationComponent, KoinComponent, ComponentContext by componentContext {

    private val dispatchers: AppDispatchers by inject()

    private val store = instanceKeeper.getStore {
        ApplicationStoreProvider(storeFactory).create()
    }

    override val navigator = DefaultStackNavigationComponent(
        componentContext = componentContext,
        startConfig = StackNavigationComponent.Config.Main,
        storeFactory = storeFactory
    )

    override val state = MutableValue(store.state)

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            store.states bindTo ::onStateChange
        }
    }

    private fun onStateChange(newState: ApplicationStore.State) {
        state.value = newState
    }

}
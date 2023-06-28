package space.compoze.hiero.ui.shared.settings.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.domain.settings.enums.AppSettings
import space.compoze.hiero.ui.shared.settings.store.SettingsStore
import space.compoze.hiero.ui.shared.settings.store.SettingsStoreProvider
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope
import space.compoze.hiero.ui.shared.utils.with

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
) : SettingsComponent, KoinComponent, ComponentContext by componentContext{

    private val dispatchers: AppDispatchers by inject()

    private val store = instanceKeeper.getStore {
        SettingsStoreProvider(storeFactory).create()
    }

    override val state = MutableValue(store.state)

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            store.states bindTo ::onStateChange
        }
    }

    private fun onStateChange(newState: SettingsStore.State) {
        state.value = newState
    }

    override fun toggleTheme() {
        state.value.with { content: SettingsStore.State.Content ->
            when (content.theme) {
                AppSettings.Theme.DARK -> store.accept(SettingsStore.Intent.ChangeTheme(AppSettings.Theme.LIGHT))
                AppSettings.Theme.LIGHT -> store.accept(SettingsStore.Intent.ChangeTheme(AppSettings.Theme.SYSTEM))
                AppSettings.Theme.SYSTEM -> store.accept(SettingsStore.Intent.ChangeTheme(AppSettings.Theme.DARK))
            }
        }
    }

    override fun setTheme(theme: String) {
        store.accept(SettingsStore.Intent.ChangeTheme(theme))
    }
}
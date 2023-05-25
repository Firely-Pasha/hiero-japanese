package space.compoze.hiero.ui.shared.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import space.compoze.hiero.domain.settings.enums.AppSettings
import space.compoze.hiero.ui.shared.main.MainComponent
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope
import space.compoze.hiero.ui.shared.utils.with

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
) : SettingsComponent, ComponentContext by componentContext{

    private val componentScope = inheritScope()

    private val store = instanceKeeper.getStore {
        SettingsStoreProvider(storeFactory).create(componentScope)
    }

    override val state = MutableValue(store.state)

    init {
        componentScope.launch {
            store.states.collect { state.value = it }
        }
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
}
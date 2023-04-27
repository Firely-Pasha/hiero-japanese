package space.compoze.hiero.ui.shared.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.CoroutineScope
import space.compoze.hiero.ui.shared.main.MainComponent
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
) : SettingsComponent {
    override val state = MutableValue(SettingsComponent.Model(""))
}
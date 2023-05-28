package space.compoze.hiero.ui.shared.application.component

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.ui.shared.application.store.ApplicationStore
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent

interface ApplicationComponent {
    val state: Value<ApplicationStore.State>
    val navigator: StackNavigationComponent
}
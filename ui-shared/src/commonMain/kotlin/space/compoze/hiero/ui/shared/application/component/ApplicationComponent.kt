package space.compoze.hiero.ui.shared.application.component

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.ui.shared.application.store.ApplicationStore

interface ApplicationComponent {
    val state: Value<ApplicationStore.State>
}
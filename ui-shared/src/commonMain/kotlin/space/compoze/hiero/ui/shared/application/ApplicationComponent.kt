package space.compoze.hiero.ui.shared.application

import com.arkivanov.decompose.value.Value

interface ApplicationComponent {
    val state: Value<ApplicationStore.State>
}
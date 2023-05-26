package space.compoze.hiero.ui.shared.settings.component

import com.arkivanov.decompose.value.Value
import space.compoze.hiero.ui.shared.settings.store.SettingsStore

interface SettingsComponent {

    val state: Value<SettingsStore.State>

    fun toggleTheme()
}
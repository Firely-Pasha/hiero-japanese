package space.compoze.hiero.ui.shared.settings

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import space.compoze.hiero.ui.shared.main.component.MainComponent

interface SettingsComponent {

    val state: Value<SettingsStore.State>

    fun toggleTheme()
}
package space.compoze.hiero.ui.shared.settings

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import space.compoze.hiero.ui.shared.main.MainComponent

interface SettingsComponent {

    val state: Value<SettingsComponent.Model>

    data class Model(
        val id: String
    )
}
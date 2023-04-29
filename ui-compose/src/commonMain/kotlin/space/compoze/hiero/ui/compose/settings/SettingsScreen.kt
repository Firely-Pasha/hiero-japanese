@file:OptIn(ExperimentalMaterial3Api::class)

package space.compoze.hiero.ui.compose.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.settings.SettingsComponent

@Composable
fun SettingsScreen(component: SettingsComponent) {

    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                }
            )
        }
    ) {
        Text("THIS IS SETTINGS SCREEN")
    }
}
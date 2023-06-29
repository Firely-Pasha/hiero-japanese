@file:OptIn(ExperimentalMaterial3Api::class)

package space.compoze.hiero.ui.compose.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import space.compoze.hiero.ui.compose.stacknavigation.StackNavigator
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.main.component.MainComponent

@Composable
fun MainScreen(component: MainComponent) {

    val state by component.state.subscribeAsState()

    MainScreenContent(
        state = state,
        hiragana = component.hiraganaTab,
        katakana = component.katakanaTab,
        settings = component.settingsTab,
        onChangeTab = component::changeTab,
    )
}

@Composable
private fun MainScreenContent(
    state: MainComponent.Model,
    hiragana: MainComponent.Child.Hiragana,
    katakana: MainComponent.Child.Katakana,
    settings: MainComponent.Child.Settings,
    onChangeTab: (tab: Int) -> Unit,
) {

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = state.tab == 0,
                    onClick = { onChangeTab(0) },
                    icon = { Text("ひ", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
//                    label = { Text("Hiragana") }
                )
                NavigationBarItem(
                    selected = state.tab == 1,
                    onClick = { onChangeTab(1) },
                    icon = { Text("カ", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
//                    label = { Text("Katakana") }
                )
                NavigationBarItem(
                    selected = state.tab == 2,
                    onClick = { onChangeTab(2) },
                    icon = { Icon(Icons.Outlined.Settings, "Settings") },
//                    label = { Text("Settings") }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            when (state.tab) {
                0 -> StackNavigator(hiragana.component)
                1 -> StackNavigator(katakana.component)
                2 -> StackNavigator(settings.component)
            }
        }
    }
}
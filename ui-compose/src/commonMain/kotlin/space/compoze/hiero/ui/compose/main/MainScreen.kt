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
        childStack = component.childStack,
        onChangeTab = component::changeTab
    )
}

@Composable
private fun MainScreenContent(
    state: MainComponent.Model,
    childStack: Value<ChildStack<*, MainComponent.Child>>,
    onChangeTab: (tab: Int) -> Unit,
) {


    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = state.tab == 0,
                    onClick = { onChangeTab(0) },
                    icon = {
                        Text("ひ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    },
                )
                NavigationBarItem(
                    selected = state.tab == 1,
                    onClick = { onChangeTab(1) },
                    icon = {
                        Text("カ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    },
                )
                NavigationBarItem(
                    selected = state.tab == 2,
                    onClick = { onChangeTab(2) },
                    icon = {
                        Icon(Icons.Outlined.Settings, "Settings")
                    },
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Children(
                stack = childStack,
            ) {
                when (val child = it.instance) {
                    is MainComponent.Child.Hiragana -> StackNavigator(child.component)
                    is MainComponent.Child.Katakana -> StackNavigator(child.component)
                    is MainComponent.Child.Settings -> StackNavigator(child.component)
                }
            }
        }
    }
}
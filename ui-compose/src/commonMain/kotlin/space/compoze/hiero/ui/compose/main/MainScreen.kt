package space.compoze.hiero.ui.compose.main

import CollectionScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import space.compoze.hiero.ui.compose.stacknavigation.StackNavigator
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.main.DefaultMainComponent
import space.compoze.hiero.ui.shared.main.MainComponent

@Composable
fun MainScreen(component: DefaultMainComponent, modifier: Modifier = Modifier) {

    val state by component.state.subscribeAsState()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    selected = state.tab == 0,
                    onClick = {
                        component.changeTab(0)
                    },
                    icon = {
                        Icon(Icons.Default.Home, "Hiragana")
                    },
                )
                BottomNavigationItem(
                    selected = state.tab == 1,
                    onClick = {
                        component.changeTab(1)
                    },
                    icon = {
                        Icon(Icons.Default.Person, "Katakana")
                    },
                )
                BottomNavigationItem(
                    selected = state.tab == 2,
                    onClick = {
                        component.changeTab(2)
                    },
                    icon = {
                        Icon(Icons.Default.Settings, "Settings")
                    },
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Children(
                stack = component.childStack,
                modifier = modifier,
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
package space.compoze.hiero.ui.compose.stacknavigation

import CollectionScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import space.compoze.hiero.ui.compose.settings.SettingsScreen
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.main.DefaultMainComponent
import space.compoze.hiero.ui.shared.main.MainComponent
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent

@Composable
fun StackNavigator(component: StackNavigationComponent, modifier: Modifier = Modifier) {

    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is StackNavigationComponent.Child.Hiragana -> CollectionScreen(child.component)
            is StackNavigationComponent.Child.Katakana -> CollectionScreen(child.component)
            is StackNavigationComponent.Child.Settings -> SettingsScreen(child.component)
        }
    }

}
package space.compoze.hiero.ui.compose.stacknavigation

import CollectionScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import space.compoze.hiero.ui.compose.section.SectionScreen
import space.compoze.hiero.ui.compose.settings.SettingsScreen
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent

@Composable
fun StackNavigator(component: StackNavigationComponent, modifier: Modifier = Modifier) {

    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is StackNavigationComponent.Child.Collection -> CollectionScreen(child.component)
            is StackNavigationComponent.Child.Section -> SectionScreen(child.component)
            is StackNavigationComponent.Child.Settings -> SettingsScreen(child.component)
        }
    }

}
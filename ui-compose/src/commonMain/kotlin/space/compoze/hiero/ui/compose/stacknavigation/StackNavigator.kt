@file:OptIn(ExperimentalAnimationApi::class)

package space.compoze.hiero.ui.compose.stacknavigation

import CollectionScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import space.compoze.hiero.ui.compose.main.MainScreen
import space.compoze.hiero.ui.compose.quiz.QuizScreen
import space.compoze.hiero.ui.compose.section.SectionScreen
import space.compoze.hiero.ui.compose.settings.SettingsScreen
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent

@Composable
fun StackNavigator(component: StackNavigationComponent, modifier: Modifier = Modifier) {

    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = MainStackAnimation()
    ) {
        when (val child = it.instance) {
            is StackNavigationComponent.Child.Collection -> CollectionScreen(child.component)
            is StackNavigationComponent.Child.Section -> SectionScreen(child.component)
            is StackNavigationComponent.Child.Settings -> SettingsScreen(child.component)
            is StackNavigationComponent.Child.Quiz -> QuizScreen(child.component)
            is StackNavigationComponent.Child.Main -> MainScreen(child.component)
        }
    }

}

/**
 * MAGIC CODE FOR COOL ANIMATIONS
 */
fun <C : Any, T : Any> MainStackAnimation() = StackAnimation<C, T> { stack, modifier, content ->
    var currChildrenCount by remember { mutableStateOf(stack.items.size) }
    LaunchedEffect(stack.backStack) {
        if (stack.items.size == currChildrenCount) {
            return@LaunchedEffect
        }
        currChildrenCount = stack.items.size
    }
    AnimatedContent(
        stack.active,
        transitionSpec = {
            if (currChildrenCount < stack.items.size) {
                (fadeIn() + slideIn(initialOffset = { IntOffset(it.width / 2, 0) })) with
                        fadeOut() + slideOut(targetOffset = { IntOffset(-it.width / 2, 0) })
            } else {
                (fadeIn() + slideIn(initialOffset = { -IntOffset(it.width / 2, 0) })) with
                        fadeOut() + slideOut(targetOffset = { IntOffset(it.width / 2, 0) })
            }
        }
    ) { state ->
        println("TRIGGER FORM ANIMAGTED CONTENT ${currChildrenCount} ${stack.items.size}")
        content(state)
        if (transition.isRunning || currChildrenCount != stack.items.size) {
            Overlay(modifier)
        }
    }
}

@Composable
private fun Overlay(modifier: Modifier) {
    Box(
        modifier = modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    event.changes.forEach { it.consume() }
                }
            }
        }
    )
}

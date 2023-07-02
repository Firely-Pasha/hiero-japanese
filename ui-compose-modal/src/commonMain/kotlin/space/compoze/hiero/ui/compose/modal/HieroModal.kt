@file:OptIn(ExperimentalAnimationApi::class)

package space.compoze.hiero.ui.compose.modal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.backhandler.BackDispatcher
import space.compoze.hiero.ui.compose.modal.types.HieroModalDialogue
import space.compoze.hiero.ui.compose.modal.types.HieroModalPopup

sealed interface HieroModal {

    val anchor: Anchor
    val alignment: Anchor

    data class Popup(
        override val anchor: Anchor,
        override val alignment: Anchor,
        val position: Offset,
        val containerSize: IntSize,
        val offset: Offset,
        val width: Dp?,
        val height: Dp?,
        val background: (@Composable () -> Unit)?,
        val content: @Composable () -> Unit,
    ) : HieroModal

    data class Dialog(
        override val anchor: Anchor,
        override val alignment: Anchor,
        val containerSize: IntSize,
        val width: Dp?,
        val height: Dp?,
        val background: (@Composable () -> Unit)?,
        val content: @Composable () -> Unit,
    ) : HieroModal
}

class HieroModalController {

    val popup = mutableStateOf<HieroModal.Popup?>(null)
    val dialog = mutableStateOf<HieroModal.Dialog?>(null)

    fun showDialog(data: HieroModal.Dialog) {
        dialog.value = data
    }

    fun showPopup(data: HieroModal.Popup) {
        popup.value = data
    }

    fun dismissDialog() {
        popup.value = null
        dialog.value = null
    }

    fun dismissPopup() {
        popup.value = null
    }
}

val LocalHieroModal = compositionLocalOf { HieroModalController() }

@Composable
fun HieroModalProvider(
    content: @Composable () -> Unit,
) {
    val controller = remember { HieroModalController() }
    CompositionLocalProvider(LocalHieroModal provides controller) {
        content()
    }
    HieroModalDialogue(controller)
    HieroModalPopup(controller)
}

class HieroModalConsumerScope(
    private val controller: HieroModalController,
    private val position: Offset,
    private val containerSize: IntSize,
) {
    fun showPopup(
        anchor: Anchor = Anchor.Center,
        alignment: Anchor = Anchor.Center,
        offset: Offset = Offset.Zero,
        width: Dp,
        height: Dp? = null,
        background: (@Composable () -> Unit)? = null,
        content: @Composable () -> Unit,
    ) {
        controller.showPopup(
            HieroModal.Popup(
                anchor = anchor,
                alignment = alignment,
                position = position,
                containerSize = containerSize,
                offset = offset,
                width = width,
                height = height,
                background = background,
                content = content
            )
        )
    }
    fun showDialogue(
        anchor: Anchor = Anchor.Center,
        alignment: Anchor = Anchor.Center,
        width: Dp? = null,
        height: Dp? = null,
        background: (@Composable () -> Unit)? = null,
        content: @Composable () -> Unit,
    ) {
        controller.showDialog(
            HieroModal.Dialog(
                anchor = anchor,
                alignment = alignment,
                containerSize = containerSize,
                width = width,
                height = height,
                background = background,
                content = content
            )
        )
    }

    fun dismissPopup() {
        controller.dismissPopup()
    }

    fun dismissDialogue() {
        controller.dismissDialog()
    }
}

@Composable
fun HieroModalConsumer(content: @Composable HieroModalConsumerScope.() -> Unit) {

    val modal = LocalHieroModal.current

    var position by remember { mutableStateOf(Offset.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .onGloballyPositioned {
                position = it.positionInRoot()
                size = it.size
            },
    ) {
        val scope = HieroModalConsumerScope(modal, position, size)
        scope.content()
    }
}

data class Anchor(
    val x: Float,
    val y: Float,
) {
    companion object {
        // 2D Alignments.
        @Stable
        val TopStart: Anchor = Anchor(-1f, -1f)

        @Stable
        val TopCenter: Anchor = Anchor(0f, -1f)

        @Stable
        val TopEnd: Anchor = Anchor(1f, -1f)

        @Stable
        val CenterStart: Anchor = Anchor(-1f, 0f)

        @Stable
        val Center: Anchor = Anchor(0f, 0f)

        @Stable
        val CenterEnd: Anchor = Anchor(1f, 0f)

        @Stable
        val BottomStart: Anchor = Anchor(-1f, 1f)

        @Stable
        val BottomCenter: Anchor = Anchor(0f, 1f)

        @Stable
        val BottomEnd: Anchor = Anchor(1f, 1f)
    }
}

fun Offset.Companion.horizontal(value: Float) = Offset(value, 0f)
fun Offset.Companion.vertical(value: Float) = Offset(0f, value)

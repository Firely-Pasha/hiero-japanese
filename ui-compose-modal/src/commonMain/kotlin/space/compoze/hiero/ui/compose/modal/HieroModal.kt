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

data class HieroModalData(
    val position: Offset,
    val containerSize: IntSize,
    val anchor: Anchor,
    val alignment: Anchor,
    val offset: Offset,
    val width: Dp?,
    val background: (@Composable () -> Unit)?,
    val content: @Composable () -> Unit,
)

class HieroModal {

    val state = mutableStateOf<HieroModalData?>(null)

    fun show(data: HieroModalData) {
        state.value = data
    }

    fun dismiss() {
        state.value = null
    }
}

val LocalHieroModal = compositionLocalOf { HieroModal() }

@Composable
fun HieroModalProvider(
    content: @Composable () -> Unit,
) {
    val modal = remember { HieroModal() }
    CompositionLocalProvider(LocalHieroModal provides modal) {
        content()
    }
    val modalData by modal.state
    var modalDataNotNullable by remember {
        mutableStateOf(modalData ?: HieroModalData(
            Offset.Zero,
            IntSize.Zero,
            Anchor.Center,
            Anchor.Center,
            Offset.Zero,
            0.dp,
            null
        ) {})
    }
    LaunchedEffect(modalData) {
        modalData?.let {
            modalDataNotNullable = it
        }
    }
    Box {
        AnimatedVisibility(
            visible = modalData != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            modal.dismiss()
                        }
                    }
            ) {
                modalDataNotNullable.background?.invoke()
            }
        }
        Layout(
            content = {
                AnimatedVisibility(
                    visible = modalData != null,
                    enter = fadeIn() + scaleIn(
                        animationSpec = spring(
                            Spring.DampingRatioMediumBouncy,
                            Spring.StiffnessMediumLow
                        ),
                        initialScale = 0.8f
                    ),
                    exit = fadeOut() + slideOut(targetOffset = { IntOffset(0, 50) }),
                ) {
                    Box(
                        modifier = Modifier
                            .run {
                                modalDataNotNullable.width?.let {
                                    width(it)
                                } ?: this
                            }
                    ) {
                        modalDataNotNullable.content()
                    }
                }
            },
            measurePolicy = { measurables, constraints ->
                val text =
                    measurables.getOrNull(0)?.measure(constraints) ?: return@Layout layout(0, 0) {}
                layout(
                    constraints.minWidth,
                    constraints.minWidth
                ) { //Change these per your needs
                    val anchorPosition = modalDataNotNullable.position
                    val containerSize = modalDataNotNullable.containerSize
                    val containerAnchor = modalDataNotNullable.anchor
                    val modalAnchor = modalDataNotNullable.alignment
                    val modalOffset = modalDataNotNullable.offset
                    val alignmentX = anchorPosition.x.toInt() - text.width / 2 - modalAnchor.x * text.width / 2 +
                            containerSize.width / 2 + containerSize.width / 2 * containerAnchor.x + modalOffset.x
                    text.placeRelative(
                        alignmentX.toInt(),
                        anchorPosition.y.toInt()
                    )
                }
            })

    }
}

class HieroModalConsumerScope(
    private val modal: HieroModal,
    private val position: Offset,
    private val containerSize: IntSize,
) {
    fun show(
        anchor: Anchor = Anchor.Center,
        alignment: Anchor = Anchor.Center,
        offset: Offset = Offset.Zero,
        width: Dp,
        background: (@Composable () -> Unit)? = null,
        content: @Composable () -> Unit,
    ) {
        modal.show(
            HieroModalData(
                position,
                containerSize,
                anchor,
                alignment,
                offset,
                width,
                background,
                content
            )
        )
    }

    fun dismiss() {
        modal.dismiss()
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

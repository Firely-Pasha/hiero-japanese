@file:OptIn(ExperimentalAnimationApi::class)

package space.compoze.hiero.ui.compose.modal.types

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import space.compoze.hiero.ui.compose.modal.Anchor
import space.compoze.hiero.ui.compose.modal.HieroModal
import space.compoze.hiero.ui.compose.modal.HieroModalController
import space.compoze.hiero.ui.compose.modal.ModalBackHandler

@Composable
fun HieroModalPopup(controller: HieroModalController) {
    val data by controller.popup
    var modalDataNotNullable by remember {
        mutableStateOf(data ?: HieroModal.Popup(
            anchor = Anchor.Center,
            alignment = Anchor.Center,
            position = Offset.Zero,
            containerSize = IntSize.Zero,
            offset = Offset.Zero,
            width = 0.dp,
            height = 0.dp,
            background = null,
            content = {}
        ))
    }
    LaunchedEffect(data) {
        data?.let {
            modalDataNotNullable = it
        }
    }
    ModalBackHandler(enabled = data != null) {
        controller.dismissPopup()
    }
    Box {
        AnimatedVisibility(
            visible = data != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            controller.dismissPopup()
                        }
                    }
            ) {
                modalDataNotNullable.background?.invoke()
            }
        }
        Layout(
            content = {
                AnimatedVisibility(
                    visible = data != null,
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
                    val alignmentX =
                        anchorPosition.x.toInt() - text.width / 2 - modalAnchor.x * text.width / 2 +
                                containerSize.width / 2 + containerSize.width / 2 * containerAnchor.x + modalOffset.x
                    text.placeRelative(
                        alignmentX.toInt(),
                        anchorPosition.y.toInt()
                    )
                }
            })

    }
}
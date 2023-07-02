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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
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
fun HieroModalDialogue(controller: HieroModalController) {
    val data by controller.dialog
    var modalDataNotNullable by remember {
        mutableStateOf(data ?: HieroModal.Dialog(
            anchor = Anchor.Center,
            alignment = Anchor.Center,
            containerSize = IntSize.Zero,
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
        controller.dismissDialog()
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
                            controller.dismissDialog()
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                )
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
                            .padding(16.dp)
                    ) {
                        modalDataNotNullable.content()
                    }
                }
            },
            measurePolicy = { measurables, constraints ->
                val text =
                    measurables.getOrNull(0)?.measure(constraints) ?: return@Layout layout(0, 0) {}
                layout(
                    constraints.maxWidth,
                    constraints.maxHeight
                ) { //Change these per your needs
                    val modalAnchor = modalDataNotNullable.alignment
                    val alignmentX = (constraints.maxWidth / 2 * modalAnchor.x + constraints.maxWidth / 2) - (text.width / 2 + (text.width / 2) * modalAnchor.x)
                    val alignmentY = (constraints.maxHeight / 2 * modalAnchor.y + constraints.maxHeight / 2) - (text.height / 2 + (text.height / 2) * modalAnchor.y)
                    text.place(
                        alignmentX.toInt(),
                        alignmentY.toInt()
                    )
                }
            })

    }
}
package space.compoze.hiero.ui.compose.modal

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun ModalBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(
        enabled = enabled,
        onBack = onBack
    )
}
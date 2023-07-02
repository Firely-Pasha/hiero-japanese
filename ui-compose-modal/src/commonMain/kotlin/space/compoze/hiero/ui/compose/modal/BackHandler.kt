package space.compoze.hiero.ui.compose.modal

import androidx.compose.runtime.Composable

@Composable
expect fun ModalBackHandler(enabled: Boolean, onBack: () -> Unit)
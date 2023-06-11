package space.compoze.hiero.ui.compose.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp



@Composable
operator fun PaddingValues.plus(padding: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return remember(padding) {
        PaddingValues(
            start = calculateStartPadding(layoutDirection) + padding.calculateStartPadding(layoutDirection),
            top = calculateTopPadding() + padding.calculateTopPadding(),
            end = calculateEndPadding(layoutDirection) + padding.calculateEndPadding(layoutDirection),
            bottom = calculateBottomPadding() + padding.calculateBottomPadding(),
        )
    }
}

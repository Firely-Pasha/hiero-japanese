package space.compoze.hiero.ui.compose.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp



@Composable
operator fun PaddingValues.plus(padding: PaddingValues): PaddingValues {
    return remember(padding) {
        PaddingValues(
            start = calculateStartPadding(LayoutDirection.Ltr) + padding.calculateStartPadding(LayoutDirection.Ltr),
            top = calculateTopPadding() + padding.calculateTopPadding(),
            end = calculateEndPadding(LayoutDirection.Ltr) + padding.calculateEndPadding(LayoutDirection.Ltr),
            bottom = calculateBottomPadding() + padding.calculateBottomPadding(),
        )
    }
}

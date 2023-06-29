@file:OptIn(ExperimentalMaterial3Api::class)

package space.compoze.hiero.ui.compose.popover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import space.compoze.hiero.ui.compose.modal.Anchor
import space.compoze.hiero.ui.compose.modal.HieroModalConsumerScope

class MenuScope {
    @Composable
    fun HieroModalConsumerScope.clickable(
        title: @Composable () -> Unit,
        onClick: () -> Unit,
    ) {
        ListItem(
            modifier = Modifier
                .clickable(
                    onClick = {
                        onClick()
                        dismiss()
                    }
                ),
            headlineText = title,
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        )
    }
}

fun HieroModalConsumerScope.showMenu(
    anchor: Anchor = Anchor.TopEnd,
    alignment: Anchor = Anchor.TopEnd,
    offset: Offset = Offset.Zero,
    width: Dp = 128.dp,
    showBackground: Boolean = false,
    content: @Composable MenuScope.() -> Unit,
) {
    show(
        anchor = anchor,
        alignment = alignment,
        offset = offset,
        width = width,
        background = showBackground.takeIf { it }?.let {{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
        }}
    ) {
        val scope = remember { MenuScope() }
        Card(
            modifier = Modifier
                .shadow(32.dp)
        ) {
            scope.content()
        }
    }
}
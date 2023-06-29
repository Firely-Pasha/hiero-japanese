@file:OptIn(ExperimentalMaterial3Api::class)

package space.compoze.hiero.ui.compose.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Javascript
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Superscript
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import space.compoze.hiero.domain.settings.enums.AppSettings
import space.compoze.hiero.ui.compose.modal.Anchor
import space.compoze.hiero.ui.compose.modal.HieroModalConsumer
import space.compoze.hiero.ui.compose.modal.horizontal
import space.compoze.hiero.ui.compose.popover.showMenu
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.settings.component.SettingsComponent
import space.compoze.hiero.ui.shared.settings.store.SettingsStore
import space.compoze.hiero.ui.shared.utils.with

@Composable
fun SettingsScreen(component: SettingsComponent) {

    val state by component.state.subscribeAsState()

    when (val state = state) {
        is SettingsStore.State.Error -> CircularProgressIndicator()
        SettingsStore.State.Loading -> CircularProgressIndicator()
        is SettingsStore.State.Content -> SettingsContent(
            state = state,
            onToggleTheme = component::toggleTheme,
            onSetTheme = component::setTheme,
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsStore.State.Content,
    onToggleTheme: () -> Unit,
    onSetTheme: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
                colors = TopAppBarDefaults.mediumTopAppBarColors(),
                title = {
                    Text("Settings")
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = "Appearance",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
            )
            HieroModalConsumer {
                ListItem(
                    headlineText = {
                        Text("Theme")
                    },
                    leadingContent = {
                        Icon(Icons.Outlined.Palette, "Appearance")
                    },
                    trailingContent = {
                        Box(
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                state.theme.replaceFirstChar { it.uppercaseChar() },
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    },
                    colors = ListItemDefaults.colors(
                        leadingIconColor = MaterialTheme.colorScheme.primary,
                        trailingIconColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                showMenu(
                                    offset = Offset.horizontal(-32f),
                                    showBackground = true
                                ) {
                                    clickable(
                                        title = { Text("System") },
                                        onClick = { onSetTheme(AppSettings.Theme.SYSTEM) }
                                    )
                                    clickable(
                                        title = { Text("Light") },
                                        onClick = { onSetTheme(AppSettings.Theme.LIGHT) }
                                    )
                                    clickable(
                                        title = { Text("Dark") },
                                        onClick = { onSetTheme(AppSettings.Theme.DARK) }
                                    )
                                }
                            }
                        )
                )
            }
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = "About app",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
            )
            ListItem(
                leadingContent = {
                    Icon(Icons.Outlined.Info, "Info")
                },
                headlineText = {
                    Text(
                        "Hiero"
                    )
                },
                trailingContent = {
                    Text(
                        "v0.1.0",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                colors = ListItemDefaults.colors(
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                    trailingIconColor = MaterialTheme.colorScheme.primary,
                ),
            )
            ListItem(
                leadingContent = {
                    Icon(Icons.Outlined.Code, "GitHub")
                },
                headlineText = {
                    Text(
                        "GitHub"
                    )
                },
                trailingContent = {
                    Text(
                        "by Firely-Pasha",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                colors = ListItemDefaults.colors(
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                    trailingIconColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .clickable {
                        uriHandler.openUri("https://github.com/Firely-Pasha/hiero-japanese")
                    }
            )
//                ListItem(
//                    leadingContent = {
//                        Icon(Icons.Outlined.Feedback, "Feedback")
//                    },
//                    headlineText = {
//                        Text("Report a problem")
//                    },
//                    trailingContent = {
//                        Icon(Icons.Outlined.ChevronRight, "Report a problem")
//                    },
//                    colors = ListItemDefaults.colors(
//                        leadingIconColor = MaterialTheme.colorScheme.primary,
//                        trailingIconColor = MaterialTheme.colorScheme.primary,
//                    ),
//                    modifier = Modifier
//                        .clickable {
//
//                        }
//                )
        }
    }
}
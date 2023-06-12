@file:OptIn(ExperimentalMaterial3Api::class)

package space.compoze.hiero.ui.compose.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
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
import androidx.compose.ui.unit.dp
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
            onToggleTheme = component::toggleTheme
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsStore.State.Content,
    onToggleTheme: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
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
                        onClick = onToggleTheme
                    )
            )
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
                        "Hiero Japanese"
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
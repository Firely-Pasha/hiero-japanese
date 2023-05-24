@file:OptIn(ExperimentalMaterial3Api::class)

package space.compoze.hiero.ui.compose.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.settings.SettingsComponent

@Composable
fun SettingsScreen(component: SettingsComponent) {

    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            ListItem(
                headlineText = {
                    Text("Appearance")
                },
                leadingContent = {
                     Icon(Icons.Outlined.Palette, "Appearance")
                },
                trailingContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "System",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                    trailingIconColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .clickable {

                    }
            )
        }
    }
}
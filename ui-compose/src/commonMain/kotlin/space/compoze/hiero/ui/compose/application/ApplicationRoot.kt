@file:OptIn(ExperimentalMaterial3Api::class)

package space.compoze.hiero.ui.compose.application

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import space.compoze.hiero.ui.compose.modal.HieroModalProvider
import space.compoze.hiero.ui.compose.stacknavigation.StackNavigator
import space.compoze.hiero.ui.shared.application.component.ApplicationComponent

@Composable
fun ApplicationRoot(component: ApplicationComponent) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        HieroModalProvider {
            StackNavigator(component = component.navigator)
        }
    }

}
package space.compoze.hiero.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import space.compoze.hiero.ui.compose.main.MainScreen
import space.compoze.hiero.ui.compose.stacknavigation.StackNavigator
import space.compoze.hiero.ui.shared.main.DefaultMainComponent
import space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Always create the root component outside Compose on the main thread
        val appNavigator = DefaultStackNavigationComponent(
            componentContext = defaultComponentContext(),
            startConfig = StackNavigationComponent.Config.Main,
            storeFactory = DefaultStoreFactory()
        )
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    StackNavigator(component = appNavigator)
                }
            }
        }
    }
}
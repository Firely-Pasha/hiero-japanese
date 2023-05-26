package space.compoze.hiero.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import space.compoze.hiero.ui.compose.stacknavigation.StackNavigator
import space.compoze.hiero.ui.compose.utils.subscribeAsState
import space.compoze.hiero.ui.shared.application.component.ApplicationDefaultComponent
import space.compoze.hiero.ui.shared.application.store.ApplicationStore
import space.compoze.hiero.ui.shared.stacknavigation.DefaultStackNavigationComponent
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Always create the root component outside Compose on the main thread
        val applicationComponent = ApplicationDefaultComponent(
            componentContext = defaultComponentContext(),
            storeFactory = DefaultStoreFactory()
        )
        val appNavigator = DefaultStackNavigationComponent(
            componentContext = applicationComponent,
            startConfig = StackNavigationComponent.Config.Main,
            storeFactory = DefaultStoreFactory()
        )
        setContent {
            val applicationState by applicationComponent.state.subscribeAsState()
            MyApplicationTheme(
                theme = (applicationState as? ApplicationStore.State.Content)?.theme ?: "system"
            ) {
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
package space.compoze.hiero.android

import android.app.Activity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.mp.KoinPlatformTools
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
        val content: View = findViewById(android.R.id.content)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (applicationComponent.state.value is ApplicationStore.State.Loading) {
                        false
                    } else {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    }
                }
            }
        )
        setContent {
            val applicationState by applicationComponent.state.subscribeAsState()
            val theme = (applicationState as? ApplicationStore.State.Content)?.theme ?: "system"
            MyApplicationTheme(
                theme = theme
            ) {
                StatusBarColorChanger(theme)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    StackNavigator(component = applicationComponent.navigator)
                }
            }
        }
    }
}

@Composable
private fun Activity.StatusBarColorChanger(theme: String) {
    val colorScheme = MaterialTheme.colorScheme
    LaunchedEffect(theme, colorScheme) {
        val statusBarsColor = colorScheme.surfaceColorAtElevation(3.dp)
        window.statusBarColor = resources.getColor(android.R.color.transparent)
        window.navigationBarColor = statusBarsColor.toArgb()
        window.decorView.systemUiVisibility = if (statusBarsColor.luminance() > 0.5) {
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}
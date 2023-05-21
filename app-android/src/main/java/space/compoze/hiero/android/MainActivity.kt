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
import space.compoze.hiero.ui.shared.main.DefaultMainComponent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Always create the root component outside Compose on the main thread
        val mainComponent = DefaultMainComponent(
            componentContext = defaultComponentContext(),
            storeFactory = DefaultStoreFactory(),
        )
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen(component = mainComponent)
//                    TabNavigator(HomeTab(UUID.randomUUID().toString())) {
//                        Scaffold(
//                            content = {
//                                it
//                                CurrentTab()
//                            },
//                            bottomBar = {
//                                BottomNavigation {
//                                    TabNavigationItem(HomeTab(UUID.randomUUID().toString()))
//                                    TabNavigationItem(ProfileTab)
//                                    TabNavigationItem(HomeTab(UUID.randomUUID().toString()))
//                                }
//                            }
//                        )
//                    }
                }
            }
        }
    }
}

//data class HomeTab(
//    private val text: String
//) : Tab {
//
//    private val defaultListComponent = DefaultListComponent({})
//
//    private val tabUuid = UUID.randomUUID()
//
//    override val options: TabOptions
//        @Composable
//        get() {
//            val title = "ASADASD"
//            val icon = rememberVectorPainter(Icons.Default.Home)
//
//            return remember {
//                TabOptions(
//                    index = 0u,
//                    title = title,
//                    icon = icon
//                )
//            }
//        }
//
//    @Composable
//    override fun Content() {
//        val model = defaultListComponent.model.subscribeAsState()
//        LaunchedEffect(null) {
//        }
//        LazyColumn() {
//            items(model.value.items) {
//                Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
//                    Text(text = it)
//                }
//            }
//        }
//    }
//}
//
//object ProfileTab : Tab {
//
//    override val options: TabOptions
//        @Composable
//        get() {
//            val title = "PRODILW"
//            val icon = rememberVectorPainter(Icons.Default.Person)
//
//            return remember {
//                TabOptions(
//                    index = 0u,
//                    title = title,
//                    icon = icon
//                )
//            }
//        }
//
//    @Composable
//    override fun Content() {
//    }
//}
//
//@Composable
//private fun RowScope.TabNavigationItem(tab: Tab) {
//    val tabNavigator = LocalTabNavigator.current
//
//    BottomNavigationItem(
//        selected = tabNavigator.current == tab,
//        onClick = { tabNavigator.current = tab },
//        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
//    )
//}
//
//@Composable
//fun GreetingView(text: String) {
//    Text(text = text)
//}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
//        GreetingView("Hello, Android!")
    }
}

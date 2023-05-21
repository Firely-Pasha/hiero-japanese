package space.compoze.hiero.android

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

fun supportsDynamic() : Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S


@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
//    val colors = if (darkTheme) {
//        darkColors(
//            primary = Color(0xFFBB86FC),
//            primaryVariant = Color(0xFF3700B3),
//            secondary = Color(0xFF03DAC5)
//        )
//    } else {
//        lightColors(
//            primary = Color(0xFF6200EE),
//            primaryVariant = Color(0xFF3700B3),
//            secondary = Color(0xFF03DAC5)
//        )
//    }
//    val typography = Typography(
//        body1 = TextStyle(
//            fontFamily = FontFamily.Default,
//            fontWeight = FontWeight.Normal,
//            fontSize = 16.sp
//        )
//    )
//    val shapes = Shapes(
//        small = RoundedCornerShape(4.dp),
//        medium = RoundedCornerShape(4.dp),
//        large = RoundedCornerShape(0.dp)
//    )
//
    val inDarkMode: Boolean = isSystemInDarkTheme()

    val colors = if (supportsDynamic()) {
        val context = LocalContext.current
        if (inDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (inDarkMode) darkColorScheme() else lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colors,
//        colors = colors,
//        typography = typography,
//        shapes = shapes,
        content = content,
    )
}

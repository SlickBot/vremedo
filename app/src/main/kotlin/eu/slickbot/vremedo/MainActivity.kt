package eu.slickbot.vremedo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.slickbot.vremedo.theme.VremedoTheme
import eu.slickbot.vremedo.screen.Screen
import eu.slickbot.vremedo.utils.AppLifecycle
import eu.slickbot.vremedo.utils.AppNavigation
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val appLifecycle: AppLifecycle by inject()
    private val appNavigation: AppNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appLifecycle.bind(this)

        installSplashScreen().apply {
            // remove system splash icon without animation
            setOnExitAnimationListener { it.remove() }
        }

        setContent {
            val navController = rememberNavController()
            appNavigation.bind(navController)

            VremedoTheme(darkTheme = true) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                ) {
                    screen(Screen.Splash)
                    screen(Screen.Weather)
//                    screen(Screen.Images)
//                    screen(Screen.Image)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appLifecycle.unbind(this)
    }

    /* Helpers */

    private fun NavGraphBuilder.screen(
        screen: Screen,
        arguments: List<NamedNavArgument> = emptyList(),
        deepLinks: List<NavDeepLink> = emptyList(),
    ) {
        composable(screen.route, arguments, deepLinks, screen.screen)
    }

}

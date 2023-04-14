package eu.slickbot.vremedo.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.R
import eu.slickbot.vremedo.composable.BaseScreen
import eu.slickbot.vremedo.composable.FullSizeBox
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    vm: SplashViewModel = koinViewModel(),
) {
    BaseScreen(vm, fitsSystemWindows = false) {
        FullSizeBox {
            Image(
                modifier = Modifier
                    .size(192.dp)
                    .align(Alignment.Center)
                    .rotate(180f),
                painter = painterResource(R.mipmap.ic_launcher_round),
                contentDescription = null,
            )
        }
    }
}

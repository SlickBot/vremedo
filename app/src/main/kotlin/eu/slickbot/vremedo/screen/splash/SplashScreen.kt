package eu.slickbot.vremedo.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.slickbot.vremedo.composable.ViewModelScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
  vm: SplashViewModel = koinViewModel(),
) {
  ViewModelScaffold(vm) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) {
//      Image(
//        modifier = Modifier
//            .size(192.dp)
//            .align(Alignment.Center)
//            .rotate(180f),
//        painter = painterResource(R.drawable.edo),
//        contentDescription = null,
//      )
    }
  }
}

package fireforestsoul.levelupsoul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.activity.compose.BackHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initStorage(applicationContext)

        setContent {
            val viewModel = remember { AppViewModel() }
            App(viewModel)

            BackHandler(enabled = true) {
                when (viewModel.appStatus.value) {
                    AppStatus.LOADING -> {}
                    AppStatus.TABLE -> {
                        viewModel.setStatus(AppStatus.HABITS_LIST)
                    }

                    AppStatus.HABITS_LIST -> {
                        viewModel.setStatus(AppStatus.TABLE)
                    }

                    else -> {
                        viewModel.setStatus(backStatus)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        saveAllValues()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val viewModel = remember { AppViewModel() }
    App(viewModel)
}
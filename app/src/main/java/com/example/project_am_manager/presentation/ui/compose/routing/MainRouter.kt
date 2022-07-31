package routing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.project_am_manager.R
import com.example.project_am_manager.presentation.ui.compose.routing.Router

sealed class MainScreen(val titleResId: Int){
    object Home : MainScreen(R.string.home)
    object Hierarchy : MainScreen(R.string.hierarchy)
    object Hierarchy2 : MainScreen(R.string.hierarchy)
    object History : MainScreen(R.string.history)
}

object MainRouter: Router<MainScreen> {
    override var currentScreen: MutableState<MainScreen> = mutableStateOf(MainScreen.Home)
    override var previousScreen: MutableState<MainScreen> = mutableStateOf(MainScreen.Home)

    override fun navigateTo(destination: MainScreen) {
        previousScreen.value = currentScreen.value
        currentScreen.value = destination
    }

    override fun goBack() {
        currentScreen.value = previousScreen.value
    }
}



package routing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.project_am_manager.R

sealed class Screen(val titleResId: Int){
    object Home : Screen(R.string.home)
    object Hierarchy : Screen(R.string.hierarchy)
    object Hierarchy2 : Screen(R.string.hierarchy)
    object History : Screen(R.string.history)
    object Task : Screen(R.string.task)
}

object TManagerRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(
        Screen.Home
    )

    private var previousScreen: MutableState<Screen> = mutableStateOf(
        Screen.Home
    )

    fun navigateTo(destination: Screen) {
        previousScreen.value = currentScreen.value
        currentScreen.value = destination
    }

    fun goBack() {
        currentScreen.value = previousScreen.value
    }
}



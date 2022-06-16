package routing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.project_am_manager.R


sealed class ScreenTask(val titleResId: Int){
    object View : ScreenTask(R.string.view)
    object Edit : ScreenTask(R.string.edit)
}

object TaskRouter {
    var currentScreen: MutableState<ScreenTask> = mutableStateOf(
        ScreenTask.View
    )

    private var previousScreen: MutableState<ScreenTask> = mutableStateOf(
        ScreenTask.View
    )

    fun navigateTo(destination: ScreenTask) {
        previousScreen.value = currentScreen.value
        currentScreen.value = destination
    }

    fun goBack() {
        currentScreen.value = previousScreen.value

    }
}
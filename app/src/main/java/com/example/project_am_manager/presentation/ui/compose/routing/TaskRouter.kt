package routing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.project_am_manager.R
import com.example.project_am_manager.presentation.ui.compose.routing.Router


sealed class TaskScreen(val titleResId: Int) {
    object View : TaskScreen(R.string.view)
    object Edit : TaskScreen(R.string.edit)
}

object TaskRouter: Router<TaskScreen> {
    override var currentScreen: MutableState<TaskScreen> = mutableStateOf(TaskScreen.View)
    override var previousScreen: MutableState<TaskScreen> = mutableStateOf(TaskScreen.View)

    override fun navigateTo(destination: TaskScreen) {
        previousScreen.value = currentScreen.value
        currentScreen.value = destination
    }

    override fun goBack() {
        currentScreen.value = previousScreen.value
    }
}
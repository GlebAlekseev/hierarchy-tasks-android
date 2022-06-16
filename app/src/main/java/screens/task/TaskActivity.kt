package screens.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.platform.LocalView
import com.example.project_am_manager.MainActivity
import com.example.project_am_manager.TManagerApplication

import viewmodel.MainViewModel
import viewmodel.MainViewModelFactory
import java.util.*

class TaskActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        MainViewModelFactory(
            this,
            (application as TManagerApplication).dependencyInjector.repositoryTask,
            (application as TManagerApplication).dependencyInjector.repositoryBoard
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id:Long = intent.getLongExtra(MainActivity.TRANSMITTED_ID,0).toLong()
        Log.d("GGGG id==", id.toString())


        setContent {

            TaskApp(viewModel,id)
        }


        @Composable
        fun onBackPressed_(){
                onBackPressed()
        }

    }
}
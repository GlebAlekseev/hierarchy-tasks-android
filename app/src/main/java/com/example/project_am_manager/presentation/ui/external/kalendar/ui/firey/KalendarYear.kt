package com.himanshoe.kalendar.ui.firey


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.himanshoe.kalendar.common.KalendarKonfig
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.data.KalendarEvent
import com.example.project_am_manager.presentation.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun KalendarView(
    viewModel: MainViewModel,
    yearMonth: YearMonth = YearMonth.now(),
    selectedDay: LocalDate,
    kalendarKonfig: KalendarKonfig,
    kalendarSelector: KalendarSelector,
    kalendarEvents: List<KalendarEvent>,
    onCurrentDayClick: (LocalDate, KalendarEvent?) -> Unit,
    errorMessageLogged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(Grid.Half)
    ) {
        KalendarMonth(
            viewModel,
            selectedDay,
            yearMonth,
            kalendarKonfig,
            onCurrentDayClick,
            errorMessageLogged,
            kalendarSelector,
            kalendarEvents
        )
    }
}

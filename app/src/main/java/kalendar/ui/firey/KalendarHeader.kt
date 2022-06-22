package com.himanshoe.kalendar.ui.firey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.theme.Grid

@Composable
internal fun KalendarHeader(
    text: String,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    kalendarSelector: KalendarSelector,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(62, 125, 250), //purple-blue grad
                        Color(134, 124, 247)
                    )
                )
            )
            ,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,

    ) {
        KalendarButton(
            kalendarSelector = kalendarSelector,
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Previous Month",
            onClick = onPreviousMonthClick
        )
        Text(
            modifier = Modifier
                .padding(Grid.Two),
            style = MaterialTheme.typography.h6,
            text = ruLocaleMonths(text),
            textAlign = TextAlign.Center,
            color=Color.White
        )
        KalendarButton(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Next Month",
            onClick = onNextMonthClick,
            kalendarSelector = kalendarSelector
        )
    }
}
fun ruLocaleMonths(name: String): String{
    val array = name.split(" ")
    when(array[0]){
        "June" -> return "Июнь " + array[1]
        "July" -> return "Июль " + array[1]
        "August" -> return "Август " + array[1]
        "September" -> return "Сентябрь " + array[1]
        "October" -> return "Октябрь " + array[1]
        "November" -> return "Ноябрь " + array[1]
        "December" -> return "Декабрь  " + array[1]
        "January" -> return "Январь " + array[1]
        "February" -> return "Февраль " + array[1]
        "March" -> return "Март " + array[1]
        "April" -> return "Апрель " + array[1]
        "May" -> return "Май " + array[1]
        else -> return "Unknown"
    }

}

@Composable
private fun KalendarButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    kalendarSelector: KalendarSelector,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(Grid.Three)
            .clip(CircleShape)
            .background(Color.White)
    ) {
        Icon(
            modifier = Modifier
                .padding(Grid.Half)
                .alpha(1F),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = Color.Black
        )
    }
}

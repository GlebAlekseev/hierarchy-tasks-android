package componentsUI

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Ты должен сделать Composable метод для элемента списка, который будет отображать
// 1) Название родителя Board
// 2) Дата
// 3) Название
// 4) ? Кол-во Символов
// 5) Содержание заметки
// 6) Цвет

@Preview
@Composable
fun TaskItem(){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)) {
        Row(modifier = Modifier) {
            Text(text = "Название")

        }

    }

}
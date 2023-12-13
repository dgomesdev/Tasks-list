package com.dgomesdev.taskslist.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.ui.theme.ActivePriorityButtonColor
import com.dgomesdev.taskslist.ui.theme.DisabledPriorityButtonColor

@Composable
fun PrioritySetter(
    setPriority: (Int) -> Unit,
) {
    var priorityLevel by rememberSaveable {
        mutableIntStateOf(1)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Set the priority", Modifier.padding(8.dp))
        Row(
            Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .size(48.dp)
                    .clickable { if (priorityLevel > 1) setPriority(priorityLevel--) }
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            if (priorityLevel != 1) ActivePriorityButtonColor
                            else DisabledPriorityButtonColor
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "-",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Card(
                Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .size(48.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$priorityLevel",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                        )
                }
            }
            Card(
                Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .size(48.dp)
                    .clickable { if (priorityLevel < 5) setPriority(priorityLevel++) }
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            if (priorityLevel != 5) ActivePriorityButtonColor
                            else DisabledPriorityButtonColor
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "+",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PriorityPreview() {
    PrioritySetter {}
}
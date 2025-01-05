package com.dgomesdev.taskslist.presentation.ui.features.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.taskslist.R
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(modifier: Modifier) {
    var isTakingTooLong by remember { mutableStateOf(false) }
    Surface {
        Column(
            modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
            Text(stringResource(R.string.loading))
            LaunchedEffect(null) {
                delay(5000)
                isTakingTooLong = true
            }
            if (isTakingTooLong) {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                Text(stringResource(R.string.too_long), textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingPrev() {
    LoadingScreen(Modifier.fillMaxSize().padding(16.dp))
}
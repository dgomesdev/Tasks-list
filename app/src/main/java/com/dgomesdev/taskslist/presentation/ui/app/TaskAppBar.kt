package com.dgomesdev.taskslist.presentation.ui.app

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.dgomesdev.taskslist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAppBar(
    onOpenDrawer: () -> Unit
) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.app_name))
            } },
        navigationIcon = {
          IconButton(onClick = { onOpenDrawer() }) {
              Icon(
                  imageVector = Icons.Filled.Menu,
                  contentDescription = "Navigation icon"
              )
          }
        },
        actions = {
            IconButton(onClick = {
                Toast.makeText(
                    context,
                    "Developed by Dgomes Dev",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Developer info icon button"
                )
            }
        })
}
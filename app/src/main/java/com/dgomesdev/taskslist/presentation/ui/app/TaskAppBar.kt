package com.dgomesdev.taskslist.presentation.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dgomesdev.taskslist.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAppBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    onShowInfo: () -> Unit
) {

    TopAppBar(
        title = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.app_name))
            } },
        navigationIcon = {
          IconButton(onClick = { scope.launch { drawerState.open() } }) {
              Icon(
                  imageVector = Icons.Filled.Menu,
                  contentDescription = stringResource(R.string.menu)
              )
          }
        },
        actions = {
            IconButton(onClick = { onShowInfo() }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(R.string.developer_info)
                )
            }
        })
}
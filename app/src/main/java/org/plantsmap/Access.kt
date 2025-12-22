package org.plantsmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Access(viewModel: AppViewModel, navController: NavController, innerPadding: PaddingValues) {

    val user by viewModel.user.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        if (user == null) {
            Button(onClick = { navController.navigate(Routes.LOGIN) }) { Text("Login") }
        } else {
            Button(onClick = { viewModel.logout() }) { Text("Logout") }
        }
    }
}
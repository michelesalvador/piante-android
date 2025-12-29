package org.plantsmap

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = AppViewModelFactory((application as PlantsMapApplication).appRepository)
        val viewModel: AppViewModel by viewModels { factory }
        setContent {
            val navController = rememberNavController()
            val toastMessage by viewModel.toastMessage.collectAsState()
            LaunchedEffect(toastMessage) {
                toastMessage?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                    viewModel.onToastShown()
                }
            }
            Theme {
                NavHost(navController, Routes.HOME) {
                    composable(Routes.HOME) { Home(viewModel, navController) }
                    composable(Routes.PLANT_DETAIL) { PlantDetail(viewModel) }
                    composable(Routes.LOGIN) { Login(viewModel, navController) }
                }
            }
        }
    }
}
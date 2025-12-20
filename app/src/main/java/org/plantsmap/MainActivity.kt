package org.plantsmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        setContent {
            val navController = rememberNavController()
            Theme {
                NavHost(navController = navController, startDestination = Routes.HOME, builder = {
                    composable(Routes.HOME) {
                        Home(navController, viewModel)
                    }
                    composable(Routes.PLANT_DETAIL) {
                        PlantDetail(viewModel)
                    }
                })
            }
        }
    }
}
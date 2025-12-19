package org.plantsmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        setContent {
            Theme {
                Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                    //LibreMap(viewModel, innerPadding)
                    MapGoogle(viewModel, innerPadding)
                }
            }
        }
    }
}
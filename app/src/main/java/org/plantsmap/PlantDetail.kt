package org.plantsmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun PlantDetail(viewModel: MapViewModel) {

    val selectedPlant by viewModel.selectedPlant.collectAsState()

    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                Modifier
                    .padding(innerPadding)
                    .padding(vertical = 32.dp, horizontal = 16.dp)
            ) {
                selectedPlant?.apply {
                    Text(species.scientificName, style = MaterialTheme.typography.headlineMedium)
                    Text(species.commonName, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    InfoRow("Numero", number.toString())
                    InfoRow("Diametri (cm)", diameters.toString())
                    InfoRow("Altezza (m)", height.toString())
                    InfoRow("Nota", note ?: "")
                    InfoRow("Data rilievo", date)
                    InfoRow("Inserita da", user.name)
                    Spacer(Modifier.height(16.dp))
                    GoogleMap(
                        modifier = Modifier.height(300.dp),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 18F)
                        },
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            zoomGesturesEnabled = false,
                            rotationGesturesEnabled = false,
                            scrollGesturesEnabled = false
                        ),
                        mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM
                    ) {
                        viewModel.plants.collectAsState().value.forEach { plant ->
                            val markerId = if (plant == this) R.drawable.marker_selected else R.drawable.marker
                            MarkerComposable(
                                state = MarkerState(position = LatLng(plant.latitude, plant.longitude)),
                                onClick = { true }
                            ) {
                                Image(
                                    painter = painterResource(markerId),
                                    contentDescription = plant.species.scientificName
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(label, Modifier.weight(1F), fontWeight = FontWeight.Bold)
        Text(value, Modifier.weight(2F))
    }
}
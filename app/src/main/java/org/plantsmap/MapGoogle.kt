package org.plantsmap

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.plantsmap.model.Plant

@Composable
fun MapGoogle(viewModel: MapViewModel, innerPadding: PaddingValues) {

    val plants by viewModel.plants.collectAsState()
    val isLoading by viewModel.isLoading.observeAsState()
    var selectedPlant by remember { mutableStateOf<Plant?>(null) }

    /*LaunchedEffect(Unit) {
        viewModel.getPlants()
    }*/

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(45.8869, 12.29733), 12F)
            },
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
                rotationGesturesEnabled = false
            ),
            onMapClick = {
                selectedPlant = null
            },
            onMapLoaded = {
                viewModel.getPlants()
            },
            contentPadding = innerPadding
        ) {
            val context = LocalContext.current
            plants.forEach { plant ->
                MarkerInfoWindowComposable(
                    keys = arrayOf(selectedPlant == plant),
                    state = MarkerState(position = LatLng(plant.latitude, plant.longitude)),
                    onClick = {
                        selectedPlant = plant
                        false
                    },
                    onInfoWindowClick = {
                        Toast.makeText(context, plant.species.scientificName, Toast.LENGTH_SHORT).show()
                    },
                    infoContent = {
                        Surface(
                            modifier = Modifier
                                .padding(12.dp)
                                .dropShadow(
                                    shape = RoundedCornerShape(8.dp),
                                    shadow = Shadow(
                                        radius = 8.dp,
                                        color = Color.Black,
                                        offset = DpOffset(0.dp, 4.dp)
                                    )
                                ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            val description = buildString {
                                plant.number?.let { appendLine("Numero: $it") }
                                plant.diameters?.let { appendLine("Diametri: $it") }
                                plant.height?.let { appendLine("Altezza: $it") }
                                plant.note?.takeIf { it.isNotBlank() }?.let { appendLine("Nota: $it") }
                                appendLine(plant.date)
                            }.trimEnd()
                            Column(Modifier.padding(16.dp, 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(plant.species.scientificName, style = MaterialTheme.typography.titleMedium)
                                Text(plant.species.commonName)
                                Text(description, style = MaterialTheme.typography.bodySmall)
                                Text(AnnotatedString.fromHtml("Inserita da <b>${plant.user.name}</b>"))
                            }
                        }
                    }
                ) {
                    if (plant == selectedPlant) Image(
                        painterResource(R.drawable.marker_selected), plant.species.scientificName
                    ) else Image(
                        painterResource(R.drawable.marker), plant.species.scientificName
                    )
                }
            }
        }
        if (isLoading == true) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}
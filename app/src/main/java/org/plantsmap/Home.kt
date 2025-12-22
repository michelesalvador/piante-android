package org.plantsmap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun Home(viewModel: AppViewModel, navController: NavController) {

    val plants by viewModel.plants.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedPlant by viewModel.selectedPlant.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(45.8869, 12.29733), 12F)
    }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }
    var showEnableLocationDialog by remember { mutableStateOf(false) }

    if (showEnableLocationDialog) {
        AlertDialog(
            onDismissRequest = { showEnableLocationDialog = false },
            title = { Text("Posizione non attiva") },
            text = { Text("Per usare questa funzionalità, per favore attiva la Posizione.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showEnableLocationDialog = false
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                ) {
                    Text("Impostazioni")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEnableLocationDialog = false }
                ) {
                    Text("Annulla")
                }
            }
        )
    }

    fun goToMyLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                coroutineScope.launch {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(latLng))
                }
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            goToMyLocation()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getPlants()
    }

    LaunchedEffect(hasLocationPermission) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (hasLocationPermission) {
                FloatingActionButton(
                    onClick = {
                        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            goToMyLocation()
                        } else {
                            showEnableLocationDialog = true
                        }
                    },
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.MyLocation, "La mia posizione")
                }
            }
        }
    ) { innerPadding ->
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission,
                mapType = MapType.HYBRID
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
                rotationGesturesEnabled = false
            ),
            onMapClick = {
                viewModel.onPlantSelected(null)
            },
            contentPadding = innerPadding,
            mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM
        ) {
            plants.forEach { plant ->
                MarkerInfoWindowComposable(
                    keys = arrayOf(selectedPlant == plant),
                    state = MarkerState(position = LatLng(plant.latitude, plant.longitude)),
                    anchor = Offset(.5F, .5F),
                    onClick = {
                        viewModel.onPlantSelected(plant)
                        false
                    },
                    onInfoWindowClick = { navController.navigate(Routes.PLANT_DETAIL) },
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
                            shape = RoundedCornerShape(8.dp)
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
                    val markerId = if (plant == selectedPlant) R.drawable.marker_selected else R.drawable.marker
                    Image(painterResource(markerId), plant.species.scientificName)
                }
            }
        }
        if (isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
        Access(viewModel, navController, innerPadding)
    }
}
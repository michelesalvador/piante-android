package org.plantsmap

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import kotlinx.serialization.json.JsonElement
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.Position

@Composable
fun LibreMap(viewModel: MapViewModel, innerPadding: PaddingValues) {

    val plants by viewModel.plants.collectAsState()
    val isLoading by viewModel.isLoading.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getPlants()
    }

    MaplibreMap(
        baseStyle = BaseStyle.Uri("https://api.maptiler.com/maps/basic-v2/style.json?key=q0JTbpRA51e0iiuvN2yC"),
        cameraState = rememberCameraState(
            firstPosition = CameraPosition(target = Position(latitude = 45.8869, longitude = 12.29733), zoom = 13.0)
        ),
        zoomRange = 10F..20F,
        boundingBox = BoundingBox(north = 46.05, east = 12.52, south = 45.7, west = 12.13),
        onMapLoadFinished = {
            Log.d("Map", "Map loaded onMapLoadFinished")
            //style.addImage("marker", R.drawable.marker)
        },
        onMapLoadFailed = { Log.e("Map", "Map load failed: $it") },
        onMapClick = { pos, point ->
            Log.d("Map", "Map clicked on $pos with $point")
            return@MaplibreMap ClickResult.Pass
        },
        options = MapOptions(
            gestureOptions = GestureOptions(isRotateEnabled = false),
            ornamentOptions = OrnamentOptions(
                padding = innerPadding,
                isLogoEnabled = true,
                logoAlignment = Alignment.BottomStart,
                isAttributionEnabled = false,
                isCompassEnabled = false,
                isScaleBarEnabled = false
            ),
        )
    ) {
        Log.d("Mappa plants", plants.toString())
        val properties = mutableMapOf<String, JsonElement>()

        /*val features = plants.map { plant ->     Feature(
                //geometry = Point.fromJson("{\"lng\":${plant.longitude},\"lat\":${plant.latitude}]}"),
                geometry = Geometry(),
                properties = JsonObject(properties),
                id = JsonPrimitive(plant.id),
                bbox = BoundingBox(1.1,2.2,3.3,4.4)
            )
        }*/

        /*   val source: GeoJsonSource = rememberGeoJsonSource(
               id = "plants-source",
               featureCollection = FeatureCollection.from(features),
               data = TODO(),
               options = TODO()
           )
           val layer: SymbolLayer = rememberSymbolLayer(
               id = "plants-layer",
               source = source,
               iconImage = "marker",
           )*/
    }
}
package com.noobexon.xposedfakelocation.manager.ui.map.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noobexon.xposedfakelocation.data.DEFAULT_MAP_ZOOM
import com.noobexon.xposedfakelocation.data.WORLD_MAP_ZOOM
import com.noobexon.xposedfakelocation.manager.ui.map.MapViewModel
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult

@Composable
fun MapViewContainer(
    mapViewModel: MapViewModel
) {
    val uiState by mapViewModel.uiState.collectAsStateWithLifecycle()

    // Extract state from uiState
    val lastClickedLocation = uiState.lastClickedLocation
    val mapZoom = uiState.mapZoom ?: DEFAULT_MAP_ZOOM.toDouble()

    // Initial camera position
    val initialPosition = lastClickedLocation ?: LatLng(10.8231, 106.6297)
    val initialZoom = if (lastClickedLocation != null) mapZoom else WORLD_MAP_ZOOM.toDouble()

    val cameraState = rememberCameraState()
    val coroutineScope = rememberCoroutineScope()

    // Set initial camera position
    LaunchedEffect(Unit) {
        cameraState.animateTo(
            position = Position(longitude = initialPosition.longitude, latitude = initialPosition.latitude),
            zoom = initialZoom,
            duration = 0
        )
    }

    // Update ViewModel when camera moves (zoom level)
    LaunchedEffect(cameraState.position.zoom) {
        mapViewModel.updateMapZoom(cameraState.position.zoom.toFloat())
    }

    // Handle "Go To Point" events
    LaunchedEffect(Unit) {
        mapViewModel.goToPointEvent.collectLatest { latLng ->
            cameraState.animateTo(
                position = Position(longitude = latLng.longitude, latitude = latLng.latitude),
                zoom = DEFAULT_MAP_ZOOM.toDouble(),
                duration = 1000
            )
            mapViewModel.updateClickedLocation(latLng)
        }
    }

    // Handle initial loading finished
    LaunchedEffect(Unit) {
        mapViewModel.setLoadingFinished()
    }

    // Marker Data Source - create geo json for marker
    val markerGeoJson = remember(lastClickedLocation) {
        lastClickedLocation?.let {
            FeatureCollection(
                features = listOf(
                    Feature(
                        geometry = Point(Position(longitude = it.longitude, latitude = it.latitude)),
                        properties = mapOf("title" to "Selected Location")
                    )
                )
            ).json()
        } ?: FeatureCollection().json()
    }

    val markerSource = rememberGeoJsonSource(
        data = GeoJsonData.JsonString(markerGeoJson)
    )

    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        baseStyle = BaseStyle.Uri("https://demotiles.maplibre.org/style.json"),
        cameraState = cameraState,
        onMapClick = { pos, offset ->
            if (!uiState.isPlaying) {
                val clickedLatLng = LatLng(pos.latitude, pos.longitude)
                mapViewModel.updateClickedLocation(clickedLatLng)
            }
            ClickResult.Consume
        }
    ) {
        // Add Marker Circle Layer
        if (lastClickedLocation != null) {
            CircleLayer(
                id = "marker-layer",
                source = markerSource,
                circleColor = const(Color.Red),
                circleRadius = const(8.dp),
                circleStrokeWidth = const(2.dp),
                circleStrokeColor = const(Color.White)
            )
        }
    }
}

package com.noobexon.xposedfakelocation.manager.ui.map.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noobexon.xposedfakelocation.data.DEFAULT_MAP_ZOOM
import com.noobexon.xposedfakelocation.data.WORLD_MAP_ZOOM
import com.noobexon.xposedfakelocation.manager.ui.map.MapViewModel
import kotlinx.coroutines.flow.collectLatest
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.compose.MaplibreMap
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraPositionState
import org.maplibre.compose.layer.SymbolLayer
import org.maplibre.compose.source.GeoJsonSource
import org.maplibre.compose.style.MapStyle

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

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(target = initialPosition, zoom = initialZoom)
    }

    // Update ViewModel when camera moves (zoom level)
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            mapViewModel.updateMapZoom(cameraPositionState.position.zoom.toFloat())
        }
    }

    // Handle "Go To Point" events
    LaunchedEffect(Unit) {
        mapViewModel.goToPointEvent.collectLatest { latLng ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_MAP_ZOOM.toDouble()),
                durationMs = 1000
            )
            mapViewModel.updateClickedLocation(latLng)
        }
    }

    // Handle initial loading finished
    LaunchedEffect(Unit) {
        mapViewModel.setLoadingFinished()
    }

    // Marker Data Source
    // We create a GeoJSON feature string for the marker if it exists
    val markerGeoJson = remember(lastClickedLocation) {
        lastClickedLocation?.let {
            """
            {
                "type": "FeatureCollection",
                "features": [{
                    "type": "Feature",
                    "geometry": {
                        "type": "Point",
                        "coordinates": [${it.longitude}, ${it.latitude}]
                    },
                    "properties": {
                        "title": "Selected Location"
                    }
                }]
            }
            """.trimIndent()
        }
    }

    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        style = MapStyle("https://demotiles.maplibre.org/style.json"), // Free demo style
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            if (!uiState.isPlaying) {
                mapViewModel.updateClickedLocation(latLng)
            }
            true // Consume event
        }
    ) {
        // Add Marker Source and Layer
        if (markerGeoJson != null) {
            GeoJsonSource(
                id = "marker-source",
                data = markerGeoJson
            )
            
            // Note: In a real app, you would load an icon image first. 
            // For now, we use a simple circle layer to represent the point to avoid asset complexity.
            // Or if MapLibre provided default markers, we'd use them.
            // Since we don't have a marker icon loaded in the style, we'll use a specific circle representation.
            
            org.maplibre.compose.layer.CircleLayer(
                id = "marker-layer",
                sourceId = "marker-source",
                block = {
                    circleColor("red")
                    circleRadius(8.0f)
                    circleStrokeWidth(2.0f)
                    circleStrokeColor("white")
                }
            )
        }
    }
}



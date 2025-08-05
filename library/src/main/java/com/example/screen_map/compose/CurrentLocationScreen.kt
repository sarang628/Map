package com.example.screen_map.compose

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.android.catalog.framework.annotations.Sample
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@Sample(
    name = "Location - Getting Current Location",
    description = "This Sample demonstrate how to request of current location",
    documentation = "https://developer.android.com/training/location/retrieve-current",
)
@Composable
fun CurrentLocationScreen(onLocation: (Location) -> Unit, content : @Composable (() -> Unit)->Unit = {}) {
    val permissions = listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,)
    PermissionBox(permissions = permissions, requiredPermissions = listOf(permissions.first()),
        onGranted = {
            CurrentLocationContent(usePreciseLocation = it.contains(Manifest.permission.ACCESS_FINE_LOCATION), onLocation = onLocation, content = {
                content.invoke(it)
            })
        }
    )
}

@RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
@Composable
fun CurrentLocationContent(usePreciseLocation: Boolean, onLocation: (Location) -> Unit, content : @Composable (() -> Unit)->Unit = {}) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    content.invoke {
        scope.launch(Dispatchers.IO) {
            val priority = if (usePreciseLocation) { Priority.PRIORITY_HIGH_ACCURACY } else { Priority.PRIORITY_BALANCED_POWER_ACCURACY }
            val result = locationClient.getCurrentLocation(priority, CancellationTokenSource().token,).await()
            result?.let { fetchedLocation -> onLocation.invoke(fetchedLocation) } }
    }
}
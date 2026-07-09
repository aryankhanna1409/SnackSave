// MainActivity.kt

package com.example.snacksave

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.snacksave.ui.theme.SnackSaveTheme
import com.google.android.gms.location.*
import androidx.navigation.compose.*
import com.example.snacksave.components.PushbackDialog
import com.example.snacksave.screens.HomeScreen

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    // Debounce flag to prevent spammy API calls
    private var lastCafeCheckTime = 0L
    private val cafeDetectionInterval = 15000L // 15 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            SnackSaveTheme {
                val navController = rememberNavController()
                var latitude by remember { mutableStateOf(0.0) }
                var longitude by remember { mutableStateOf(0.0) }
                var showDialog by remember { mutableStateOf(false) }
                var detectedCafeName by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startLocationUpdates { lat, lon ->
                            latitude = lat
                            longitude = lon

                            val now = System.currentTimeMillis()
                            if (now - lastCafeCheckTime > cafeDetectionInterval) {
                                lastCafeCheckTime = now
                                CafeDetector.checkNearbyCafes(this@MainActivity, lat, lon) { isNearCafe, cafeName ->
                                    runOnUiThread {
                                        try {
                                            if (isNearCafe) {
                                                detectedCafeName = cafeName ?: "a nearby café"
                                                showDialog = true
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(this@MainActivity, "Error detecting cafés.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            1001
                        )
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                latitude = latitude,
                                longitude = longitude,
                                onTrackCafeClick = {
                                    getCurrentLocation { lat, lon ->
                                        CafeDetector.checkNearbyCafes(this@MainActivity, lat, lon) { isNearCafe, cafeName ->
                                            runOnUiThread {
                                                if (isNearCafe) {
                                                    detectedCafeName = cafeName ?: "a nearby café"
                                                    showDialog = true
                                                } else {
                                                    Toast.makeText(this@MainActivity, "No cafés nearby. You're safe!", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                },
                                onNavigateToRecipes = { navController.navigate(Screen.Recipes.route) },
                                onNavigateToSpendingLog = { navController.navigate(Screen.SpendingLog.route) }
                            )

                            if (showDialog) {
                                PushbackDialog(
                                    cafeName = detectedCafeName,
                                    onDismiss = { showDialog = false },
                                    onReject = {
                                        showDialog = false
                                        Toast.makeText(this@MainActivity, "Opening café info...", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }

                        composable(Screen.Recipes.route) {
                            RecipeListScreen(
                                recipes = sampleRecipes,
                                onRecipeClick = {
                                    // future use
                                }
                            )
                        }

                        composable(Screen.SpendingLog.route) {
                            var spendingLogs by remember { mutableStateOf(listOf<Pair<String, String>>()) }
                            SpendingLogScreen(
                                spendingLogs = spendingLogs,
                                onAddLog = { date, amount ->
                                    spendingLogs = spendingLogs + (date to amount)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startLocationUpdates(onLocationUpdated: (Double, Double) -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                onLocationUpdated(location.latitude, location.longitude)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                mainLooper
            )
        }
    }

    private fun getCurrentLocation(onLocationReceived: (Double, Double) -> Unit) {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let {
                onLocationReceived(it.latitude, it.longitude)
            } ?: run {
                mainHandler.post {
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates { _, _ -> }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }
}

package com.x218.basalt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

import android.content.Context
import android.location.LocationManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var locationManager : LocationManager? = null

    locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    Location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    override fun onCreate(savedInstanceState: Bundle?) {
	enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }

    }
}

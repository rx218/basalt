package com.x218.basalt

import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.x218.basalt.data.PermissionState
import com.x218.basalt.data.checkPermissions
import com.x218.basalt.data.checkGpsProvider
import com.x218.basalt.data.getLocation
import com.x218.basalt.data.requestPermission
import com.x218.basalt.ui.MainScreen
import com.x218.basalt.ui.MainViewModel
import com.x218.basalt.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import kotlin.coroutines.ContinuationInterceptor

class MainActivity : ComponentActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()

        val lm = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val perms = checkPermissions(this)
        viewModel.updatePerms(perms)

        // Request permissions if not granted
        if( perms == PermissionState(coarse = false, fine = false)) {
            requestPermission(perms, this)
            viewModel.updatePerms(perms)
        }

        // get location asynchronously if permissions are granted and location is enabled
        if ( perms != PermissionState(coarse = false, fine = false) && checkGpsProvider(lm) ) {
            // use lifecycle scope to call suspendable function
            lifecycleScope.launch {
                viewModel.updateLocation(
                    getLocation(
                        lm,
                        // Get executor from current coroutine
                        (this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher).asExecutor()
                    )
                )
            }
        }

        setContent {
            AppTheme() {
                MainScreen(viewModel)
            }
        }
    }
}
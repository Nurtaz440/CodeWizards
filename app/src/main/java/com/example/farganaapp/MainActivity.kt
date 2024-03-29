package com.example.farganaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.farganaapp.databinding.ActivityMainBinding
import com.example.farganaapp.util.SharedPreferencesManager
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    private lateinit var _dataStore: DataStore<Preferences>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        _dataStore = createDataStore(name = "onBoard")
        lifecycleScope.launch {
            _dataStore.data.collectLatest {
                // Check if user is registered
                val isRegistered = SharedPreferencesManager.isRegistered(this@MainActivity)

                if (isRegistered) {
                    // User is registered, navigate to HomeFragment
                    //  navController.navigate(R.id.signInFragment)
                    navController.navigate(R.id.clientHomeFragment)
                } else {
                    // User is not registered, stay on HomeFragment and show appropriate content
                    // ..
                    if (it[preferencesKey<Boolean>("onBoardAdmin")] == true) {
                        navController.navigate(R.id.adminHomeFragment)
                    } else if (it[preferencesKey<Boolean>("onBoardClient")] == true)
                        navController.navigate(R.id.clientHomeFragment)
                    else {
                        navController.navigate(R.id.signUpFragment)
                        //  navController.navigate(R.id.clientHomeFragment)
                    }
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
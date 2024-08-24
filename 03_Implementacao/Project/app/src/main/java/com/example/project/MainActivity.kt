package com.example.project

import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.generated.GetPersonQuery
import com.example.project.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
// Passing each menu ID as a set of Ids because each
// menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_games, R.id.nav_users, R.id.nav_ranking), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //DEBUG PERPUSES
        lifecycleScope.launch {
            try {
                val response = MyApolloClient.instance.query(GetPersonQuery("Pedro")).execute()
                val person = response.data?.getPerson

                if (person != null) {
                    Log.d("MainActivity", "UserName: ${person.UserName}")
                    Log.d("MainActivity", "Biography: ${person.Biography}")
                } else {
                    Log.d("MainActivity", "Person not found")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error executing GraphQL query", e)
            }
        }
        //END OF DEBUG
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.
        onSupportNavigateUp()
    }
}
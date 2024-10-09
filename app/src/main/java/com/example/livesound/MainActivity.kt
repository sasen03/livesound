package com.example.livesound

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.livesound.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Verifica la conexión a Internet
        if (!isInternetAvailable()) {
            showAlertDialog() // Muestra el AlertDialog si no hay conexión
        }

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val headerView = navView.getHeaderView(0)
        val closeButton = headerView.findViewById<FloatingActionButton>(R.id.close_main_menu)
        val homeMenuItem = navView.menu.findItem(R.id.nav_home)
        val profileMenuItem = navView.menu.findItem(R.id.nav_profile)
        val playerMenuItem = navView.menu.findItem(R.id.nav_player)
        val playListMenuItem = navView.menu.findItem(R.id.nav_playlist)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_player, R.id.nav_playlist
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        closeButton.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        homeMenuItem.setOnMenuItemClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_home)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        profileMenuItem.setOnMenuItemClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_profile)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        playerMenuItem.setOnMenuItemClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_player)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        playListMenuItem.setOnMenuItemClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_playlist)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false) // Impide que se cierre al hacer clic fuera

        // Crear un TextView para el título
        val titleView = TextView(this)
        titleView.text = "Sin conexión a Internet"
        titleView.setTextColor(Color.WHITE)
        titleView.textSize = 24f
        titleView.setPadding(20, 20, 20, 0)

        // Crear un TextView para el mensaje
        val messageView = TextView(this)
        messageView.text = "Por favor, conéctese a Internet para utilizar nuestros servicios."
        messageView.textSize = 18f
        messageView.setTextColor(Color.WHITE)
        messageView.setPadding(50, 30, 50, 50)

        // Crear el AlertDialog
        builder.setView(titleView)
            .setView(titleView)
            .setView(messageView)
            .setPositiveButton("OK") { dialog, which ->
                finish() // Cierra la aplicación al hacer clic en OK
            }

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.black) // Fondo negro
        }
        alertDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

package com.example.livesound.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.livesound.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebView
    private val TAG = "HomeFragment"

    // GestureDetector to intercept gestures
    private lateinit var gestureDetector: GestureDetector

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Initialize and configure the WebView
        webView = binding.webview
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.setBackgroundColor(Color.TRANSPARENT)

        // Disable zoom
        webView.settings.setSupportZoom(false)
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false

        // Initialize the GestureDetector
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                // Return false to allow scrolling within the WebView
                return false // Allow the WebView to scroll
            }

            override fun onDown(e: MotionEvent): Boolean {
                // This allows the gesture detector to recognize the touch
                return true
            }
        })

        // Intercept touch events on the WebView
        webView.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            // Return false to allow the WebView to handle scrolling
            false
        }

        // Check internet connection before loading the URL
        if (isInternetAvailable()) {
            Log.d(TAG, "Loading URL")
            webView.loadUrl("https://osmandigitalmedia.com/livesound/app.html")
        } else {
            Log.d(TAG, "No Internet connection")
            Toast.makeText(context, "Por favor, conéctese a Internet", Toast.LENGTH_LONG).show()
        }

        return root
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

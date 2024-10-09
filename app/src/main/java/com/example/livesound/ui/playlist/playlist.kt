package com.example.livesound.ui.playlist

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.livesound.R

class PlaylistFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private var songList: MutableList<String> = mutableListOf()

    companion object {
        private const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1
        private const val REQUEST_PERMISSION_READ_MEDIA_AUDIO = 123
        private const val TAG = "PlaylistFragment"
        fun newInstance() = PlaylistFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        songAdapter = SongAdapter(songList)
        recyclerView.adapter = songAdapter

        Log.d(TAG, "RecyclerView setup complete")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
        checkAndLoadSongs()
    }

    private fun checkAndLoadSongs() {
        Log.d(TAG, "checkAndLoadSongs called")
        when {
            checkPermission() -> {
                Log.d(TAG, "Permission already granted")
                loadSongs()
            }
            shouldShowRequestPermissionRationale(getRequiredPermission()) -> {
                Log.d(TAG, "Should show permission rationale")
                Toast.makeText(requireContext(), "Se necesita acceso a los archivos para mostrar tus canciones", Toast.LENGTH_LONG).show()
                requestPermission()
            }
            else -> {
                Log.d(TAG, "Requesting permission")
                requestPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), getRequiredPermission()) == PackageManager.PERMISSION_GRANTED
    }

    private fun getRequiredPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(getRequiredPermission()),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) REQUEST_PERMISSION_READ_MEDIA_AUDIO else REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult called")
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE || requestCode == REQUEST_PERMISSION_READ_MEDIA_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted")
                loadSongs()
            } else {
                Log.d(TAG, "Permission denied")
                Toast.makeText(requireContext(), "Permiso denegado para acceder a las canciones", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadSongs() {
        Log.d(TAG, "loadSongs called")
        songList.clear()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        requireContext().contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                songList.add(name)
                Log.d(TAG, "Song found: $name, URI: $contentUri")
            }
        }

        updateUI()
    }

    private fun updateUI() {
        Log.d(TAG, "updateUI called, songList size: ${songList.size}")
        activity?.runOnUiThread {
            songAdapter.notifyDataSetChanged()
            if (songList.isEmpty()) {
                Log.d(TAG, "No songs found")
                Toast.makeText(requireContext(), "No se encontraron canciones", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "Songs loaded successfully")
            }
        }
    }
}